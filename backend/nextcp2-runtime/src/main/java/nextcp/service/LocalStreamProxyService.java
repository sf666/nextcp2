package nextcp.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextcp.dto.Config;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

/**
 * Server-side pass-through proxy that streams a media server (UMS) resource to the browser for
 * local HTML5 {@code <audio>} playback (the "This Device" virtual renderer).
 * <p>
 * The browser cannot set a custom {@code User-Agent} on an {@code <audio src>}, so it cannot make
 * UMS recognize a browser-specific renderer profile. This proxy fetches the media on the browser's
 * behalf and tags every request with {@link #PROXY_USER_AGENT}, which the UMS
 * {@code nextcp2webplayer.conf} renderer profile matches. UMS then streams browser-native formats
 * untouched and transcodes the rest to MP3. It also sidesteps CORS / mixed-content issues when the
 * UI is served over HTTPS.
 * <p>
 * <b>Two delivery modes (adaptive):</b>
 * <ul>
 * <li><b>Pass-through</b> - for resources UMS serves with a known length and range support (native
 * formats, direct stream). The client {@code Range} header is forwarded verbatim and the upstream
 * status/headers are relayed unchanged, so seeking works natively with no added latency.</li>
 * <li><b>Pre-transcode &amp; cache</b> - for resources UMS transcodes on the fly (e.g. ALAC to MP3),
 * which it streams chunked without a {@code Content-Length}. The browser cannot determine the
 * duration (audio.duration becomes {@code Infinity}) or seek in such a stream. When the General
 * setting {@code localPlayerPreTranscodeEnabled} is on, the whole transcoded output is buffered
 * once into a cache file and then served with a real {@code Content-Length} and full {@code Range}
 * support. First playback of a transcoded track pays the transcode latency; subsequent plays, seeks
 * and reloads are served instantly from the cache. The cache location and size limit come from the
 * General settings ({@code localPlayerCacheDir}, {@code localPlayerCacheMaxMb}).</li>
 * </ul>
 * <p>
 * Only hosts currently known as media servers are proxied, so the endpoint cannot be abused as an
 * open relay (SSRF).
 */
@Component
public class LocalStreamProxyService {

	private static final Logger log = LoggerFactory.getLogger(LocalStreamProxyService.class.getName());

	/**
	 * Sent on every proxied upstream request; the UMS {@code nextcp2webplayer.conf} profile matches this via
	 * {@code UserAgentSearch = next_cp_webplayer}. The token deliberately avoids the substring
	 * "nextcp" so it does not also match the nextCP control-point renderer profile (Nextcp2.conf).
	 */
	public static final String PROXY_USER_AGENT = "next_cp_webplayer/1.0";

	private static final int COPY_BUFFER_SIZE = 64 * 1024;

	private final DeviceRegistry deviceRegistry;
	// General settings bean (produced by ConfigPersistence); read per-request so changes saved in the
	// UI take effect without a restart. See config.applicationConfig.localPlayer* fields.
	private final Config config;

	// Cache directory, resolved lazily from config.applicationConfig.localPlayerCacheDir and
	// re-resolved when that value changes.
	private volatile Path cacheDir;
	private volatile String resolvedCacheDirConfig;

	// Guards per-URL cache builds so concurrent requests for the same track transcode it only once.
	private final ConcurrentHashMap<String, Object> cacheBuildLocks = new ConcurrentHashMap<>();

	private final HttpClient httpClient = HttpClient.newBuilder()
		.followRedirects(HttpClient.Redirect.NORMAL)
		.connectTimeout(Duration.ofSeconds(15))
		.build();

	public LocalStreamProxyService(DeviceRegistry deviceRegistry, Config config) {
		this.deviceRegistry = deviceRegistry;
		this.config = config;
	}

	private boolean isPreTranscodeEnabled() {
		return config != null && config.applicationConfig != null
			&& Boolean.TRUE.equals(config.applicationConfig.localPlayerPreTranscodeEnabled);
	}

	/** @return the cache size limit in bytes, or {@code -1} for unlimited (config null, empty or {@code <= 0}). */
	private long getMaxCacheBytes() {
		Long mb = (config != null && config.applicationConfig != null) ? config.applicationConfig.localPlayerCacheMaxMb : null;
		if (mb == null || mb <= 0) {
			return -1;
		}
		return mb * 1024L * 1024L;
	}

	/** Resolves (creating if needed) the cache directory from the current config; {@code null} if unavailable. */
	private synchronized Path getCacheDir() {
		String configured = StringUtils.trimToEmpty(
			(config != null && config.applicationConfig != null) ? config.applicationConfig.localPlayerCacheDir : null);
		if (cacheDir != null && configured.equals(resolvedCacheDirConfig)) {
			return cacheDir;
		}
		try {
			Path dir = StringUtils.isNotBlank(configured)
				? Path.of(configured)
				: Path.of(System.getProperty("java.io.tmpdir"), "nextcp-localstream-cache");
			Files.createDirectories(dir);
			cacheDir = dir;
			resolvedCacheDirConfig = configured;
			log.info("local stream pre-transcode cache directory: {}", cacheDir);
			return cacheDir;
		} catch (IOException | RuntimeException e) {
			log.warn("could not initialize local stream cache dir '{}': {}", configured, e.getMessage());
			return null;
		}
	}

	/**
	 * Clears the transcode cache on startup: cached files from a previous run are stale (the media may
	 * have changed) and browser sessions do not survive a backend restart anyway, so we start clean.
	 * Also removes any {@code .tmp} files orphaned by a crash during a previous cache build.
	 */
	@PostConstruct
	void clearCacheOnStartup() {
		Path dir = getCacheDir();
		if (dir == null) {
			return;
		}
		int deleted = 0;
		try (var stream = Files.list(dir)) {
			for (Path p : (Iterable<Path>) stream::iterator) {
				if (Files.isRegularFile(p) && deleteQuietly(p)) {
					deleted++;
				}
			}
		} catch (IOException e) {
			log.debug("could not clear local stream cache on startup: {}", e.getMessage());
		}
		if (deleted > 0) {
			log.info("cleared {} local stream cache file(s) on startup", deleted);
		}
	}

	/**
	 * Periodically removes cache entries that have not been used for longer than the configured TTL
	 * ({@code localPlayerCacheTtlHours}, 0 = disabled). The last-modified time is refreshed on every
	 * read (see {@link #touch(Path)}), so this evicts by idle time rather than age.
	 */
	@Scheduled(fixedDelay = 3600000L)
	void sweepIdleCacheFiles() {
		long ttlMillis = getCacheTtlMillis();
		if (ttlMillis <= 0 || cacheDir == null) {
			return;
		}
		long cutoff = System.currentTimeMillis() - ttlMillis;
		int deleted = 0;
		try (var stream = Files.list(cacheDir)) {
			for (Path p : (Iterable<Path>) stream::iterator) {
				String name = p.getFileName().toString();
				if (!name.endsWith(".cache")) {
					continue;
				}
				if (lastModifiedMillis(p) < cutoff && deleteQuietly(p)) {
					deleteQuietly(cacheDir.resolve(name.substring(0, name.length() - ".cache".length()) + ".meta"));
					deleted++;
				}
			}
		} catch (IOException e) {
			log.debug("idle cache sweep failed: {}", e.getMessage());
		}
		if (deleted > 0) {
			log.info("evicted {} idle local stream cache entry/entries (ttl exceeded)", deleted);
		}
	}

	/** @return the idle time-to-live in milliseconds, or {@code -1} when disabled (config null / <= 0). */
	private long getCacheTtlMillis() {
		Long hours = (config != null && config.applicationConfig != null) ? config.applicationConfig.localPlayerCacheTtlHours : null;
		if (hours == null || hours <= 0) {
			return -1;
		}
		return hours * 3600000L;
	}

	/**
	 * Fetches {@code targetUrl} from a known media server and streams it back through {@code response}.
	 * Chooses pass-through or pre-transcode-and-cache delivery based on whether UMS serves the resource
	 * with a known length and range support.
	 *
	 * @param targetUrl the media server resource URL (must belong to a discovered media server)
	 * @param request   the incoming browser request (used for the {@code Range} header)
	 * @param response  the response the media bytes are written to
	 */
	public void proxy(String targetUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
		URI uri = validateTarget(targetUrl);

		if (!isPreTranscodeEnabled() || getCacheDir() == null) {
			passThrough(uri, request, response);
			return;
		}

		String key = cacheKey(uri);
		if (Files.isRegularFile(cacheFile(key))) {
			serveFromCache(key, request, response);
			return;
		}

		boolean needsTranscode;
		try {
			needsTranscode = probeNeedsTranscode(uri);
		} catch (IOException e) {
			log.debug("range probe failed for {}, falling back to pass-through: {}", uri, e.getMessage());
			passThrough(uri, request, response);
			return;
		}

		if (!needsTranscode) {
			// Native / seekable resource: UMS already provides length + ranges, no need to buffer.
			passThrough(uri, request, response);
			return;
		}

		try {
			ensureCached(key, uri);
		} catch (IOException e) {
			log.warn("pre-transcode caching failed for {}, falling back to pass-through: {}", uri, e.getMessage());
			passThrough(uri, request, response);
			return;
		}
		serveFromCache(key, request, response);
	}

	/**
	 * Streams the resource straight from UMS to the client, forwarding the {@code Range} header and
	 * relaying the upstream status and relevant headers unchanged. Used for resources UMS serves with a
	 * known length (native formats).
	 */
	private void passThrough(URI uri, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpRequest.Builder upstreamRequest = HttpRequest.newBuilder(uri)
			.header("User-Agent", PROXY_USER_AGENT)
			.GET();
		String range = request.getHeader("Range");
		if (StringUtils.isNotBlank(range)) {
			upstreamRequest.header("Range", range);
		}

		HttpResponse<InputStream> upstream;
		try {
			upstream = httpClient.send(upstreamRequest.build(), HttpResponse.BodyHandlers.ofInputStream());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("proxied request was interrupted", e);
		}

		if (log.isDebugEnabled()) {
			log.debug("proxy pass-through: status={} Content-Type={} Content-Length={} Accept-Ranges={} Content-Range={} (request Range={}) url={}",
				upstream.statusCode(),
				upstream.headers().firstValue("Content-Type").orElse("<none>"),
				upstream.headers().firstValue("Content-Length").orElse("<none>"),
				upstream.headers().firstValue("Accept-Ranges").orElse("<none>"),
				upstream.headers().firstValue("Content-Range").orElse("<none>"),
				StringUtils.defaultIfBlank(range, "<none>"),
				uri);
		}

		response.setStatus(upstream.statusCode());
		relayHeader(upstream, response, "Content-Type");
		relayHeader(upstream, response, "Content-Length");
		relayHeader(upstream, response, "Content-Range");
		relayHeader(upstream, response, "Accept-Ranges");

		try (InputStream in = upstream.body(); OutputStream out = response.getOutputStream()) {
			in.transferTo(out);
			out.flush();
		} catch (IOException e) {
			// The browser routinely aborts the connection when seeking or switching tracks; that is
			// expected and must not be logged as an error.
			log.debug("proxied stream closed by client: {}", e.getMessage());
		}
	}

	/**
	 * Probes whether UMS serves this resource as a seekable, known-length stream (native format) or
	 * transcodes it on the fly (chunked, no length). Sends a cheap 1-byte range request and inspects
	 * only the response headers.
	 *
	 * @return {@code true} if the resource must be pre-transcoded (not natively seekable)
	 */
	private boolean probeNeedsTranscode(URI uri) throws IOException {
		HttpRequest probe = HttpRequest.newBuilder(uri)
			.header("User-Agent", PROXY_USER_AGENT)
			.header("Range", "bytes=0-0")
			.GET()
			.build();

		HttpResponse<InputStream> resp;
		try {
			resp = httpClient.send(probe, HttpResponse.BodyHandlers.ofInputStream());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("range probe was interrupted", e);
		}

		try (InputStream body = resp.body()) {
			// Closing the body here cancels the transfer (and any UMS transcode started for the probe).
			boolean honoredRange = resp.statusCode() == 206;
			boolean knownLength = resp.headers().firstValue("Content-Length").isPresent();
			boolean advertisesRanges = resp.headers().firstValue("Accept-Ranges")
				.map(v -> !"none".equalsIgnoreCase(v.trim()))
				.orElse(false);
			boolean seekable = honoredRange || (knownLength && advertisesRanges);
			return !seekable;
		}
	}

	/**
	 * Ensures the resource is fully buffered into the cache, transcoding it via UMS exactly once even
	 * under concurrent requests.
	 */
	private void ensureCached(String key, URI uri) throws IOException {
		Path target = cacheFile(key);
		if (Files.isRegularFile(target)) {
			touch(target);
			return;
		}

		Object lock = cacheBuildLocks.computeIfAbsent(key, k -> new Object());
		synchronized (lock) {
			try {
				if (Files.isRegularFile(target)) {
					touch(target);
					return;
				}

				HttpRequest request = HttpRequest.newBuilder(uri)
					.header("User-Agent", PROXY_USER_AGENT)
					.GET()
					.build();
				HttpResponse<InputStream> upstream;
				try {
					upstream = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new IOException("caching request was interrupted", e);
				}
				if (upstream.statusCode() != 200 && upstream.statusCode() != 206) {
					throw new IOException("upstream returned status " + upstream.statusCode() + " while caching " + uri);
				}

				String contentType = upstream.headers().firstValue("Content-Type").orElse("application/octet-stream");
				Path tmp = cacheDir.resolve(key + "." + UUID.randomUUID() + ".tmp");
				try (InputStream in = upstream.body()) {
					Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					deleteQuietly(tmp);
					throw e;
				}

				// Write the sidecar type file before publishing the data file, then atomically move the
				// data file into place - its presence is the "cache entry complete" marker.
				Files.writeString(metaFile(key), contentType, StandardCharsets.UTF_8);
				Files.move(tmp, target, StandardCopyOption.ATOMIC_MOVE);
				log.info("cached transcoded stream ({} bytes, {}) for {}", Files.size(target), contentType, uri);
				evictIfNeeded();
			} finally {
				cacheBuildLocks.remove(key, lock);
			}
		}
	}

	/**
	 * Serves a fully cached resource with {@code Content-Length} and single-range support so the browser
	 * can determine the duration and seek. Multi-range requests fall back to the full body.
	 */
	private void serveFromCache(String key, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Path file = cacheFile(key);
		long length = Files.size(file);
		touch(file);

		response.setHeader("Accept-Ranges", "bytes");
		response.setContentType(readContentType(key));

		long start = 0;
		long end = length - 1;
		boolean partial = false;

		String range = request.getHeader("Range");
		if (StringUtils.isNotBlank(range) && range.startsWith("bytes=") && !range.contains(",")) {
			String spec = range.substring("bytes=".length()).trim();
			int dash = spec.indexOf('-');
			if (dash >= 0) {
				String startText = spec.substring(0, dash).trim();
				String endText = spec.substring(dash + 1).trim();
				try {
					if (startText.isEmpty()) {
						// suffix range: bytes=-N -> the last N bytes
						long suffix = endText.isEmpty() ? 0 : Long.parseLong(endText);
						start = Math.max(0, length - suffix);
						end = length - 1;
					} else {
						start = Long.parseLong(startText);
						end = endText.isEmpty() ? length - 1 : Math.min(Long.parseLong(endText), length - 1);
					}
					if (start > end || start >= length) {
						response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						response.setHeader("Content-Range", "bytes */" + length);
						return;
					}
					partial = true;
				} catch (NumberFormatException e) {
					// Malformed range: serve the whole body.
					start = 0;
					end = length - 1;
					partial = false;
				}
			}
		}

		long contentLength = end - start + 1;
		if (partial) {
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
		} else {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		response.setHeader("Content-Length", Long.toString(contentLength));

		try (InputStream in = Files.newInputStream(file); OutputStream out = response.getOutputStream()) {
			if (start > 0) {
				in.skipNBytes(start);
			}
			copyExactly(in, out, contentLength);
			out.flush();
		} catch (IOException e) {
			// Expected when the browser aborts on seek / track change.
			log.debug("cached stream closed by client: {}", e.getMessage());
		}
	}

	private static void copyExactly(InputStream in, OutputStream out, long count) throws IOException {
		byte[] buffer = new byte[COPY_BUFFER_SIZE];
		long remaining = count;
		while (remaining > 0) {
			int read = in.read(buffer, 0, (int) Math.min(buffer.length, remaining));
			if (read == -1) {
				break;
			}
			out.write(buffer, 0, read);
			remaining -= read;
		}
	}

	private void relayHeader(HttpResponse<InputStream> upstream, HttpServletResponse response, String name) {
		Optional<String> value = upstream.headers().firstValue(name);
		value.ifPresent(v -> response.setHeader(name, v));
	}

	private Path cacheFile(String key) {
		return cacheDir.resolve(key + ".cache");
	}

	private Path metaFile(String key) {
		return cacheDir.resolve(key + ".meta");
	}

	private String readContentType(String key) {
		try {
			Path meta = metaFile(key);
			if (Files.isRegularFile(meta)) {
				String type = Files.readString(meta, StandardCharsets.UTF_8).trim();
				if (StringUtils.isNotBlank(type)) {
					return type;
				}
			}
		} catch (IOException e) {
			log.debug("could not read cached content type for {}: {}", key, e.getMessage());
		}
		return "application/octet-stream";
	}

	private static String cacheKey(URI uri) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(uri.toString().getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		} catch (NoSuchAlgorithmException e) {
			// SHA-256 is guaranteed to be present on every JVM.
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}

	private void touch(Path file) {
		try {
			Files.setLastModifiedTime(file, FileTime.from(Instant.now()));
		} catch (IOException e) {
			log.debug("could not touch cache file {}: {}", file, e.getMessage());
		}
	}

	/** Evicts least-recently-used cache entries until the total cache size is within the configured limit. */
	private synchronized void evictIfNeeded() {
		long maxCacheBytes = getMaxCacheBytes();
		if (maxCacheBytes < 0) {
			// Unlimited cache configured (0 / empty): never evict.
			return;
		}
		try {
			List<Path> dataFiles = new ArrayList<>();
			long total = 0;
			try (var stream = Files.list(cacheDir)) {
				for (Path p : (Iterable<Path>) stream::iterator) {
					if (p.getFileName().toString().endsWith(".cache")) {
						dataFiles.add(p);
						total += Files.size(p);
					}
				}
			}
			if (total <= maxCacheBytes) {
				return;
			}
			dataFiles.sort(Comparator.comparingLong(this::lastModifiedMillis));
			for (Path data : dataFiles) {
				if (total <= maxCacheBytes) {
					break;
				}
				long size;
				try {
					size = Files.size(data);
				} catch (IOException e) {
					continue;
				}
				if (deleteQuietly(data)) {
					String name = data.getFileName().toString();
					deleteQuietly(cacheDir.resolve(name.substring(0, name.length() - ".cache".length()) + ".meta"));
					total -= size;
					log.debug("evicted local-stream cache entry {}", name);
				}
			}
		} catch (IOException e) {
			log.debug("cache eviction failed: {}", e.getMessage());
		}
	}

	private long lastModifiedMillis(Path p) {
		try {
			return Files.getLastModifiedTime(p).toMillis();
		} catch (IOException e) {
			return 0L;
		}
	}

	private boolean deleteQuietly(Path p) {
		try {
			return Files.deleteIfExists(p);
		} catch (IOException e) {
			// A file currently being served may be undeletable on Windows; leave it for the next sweep.
			log.debug("could not delete cache file {}: {}", p, e.getMessage());
			return false;
		}
	}

	/**
	 * Validates the requested URL and confirms its host belongs to a currently known media server,
	 * preventing the proxy from being abused as an open relay.
	 */
	private URI validateTarget(String targetUrl) {
		if (StringUtils.isBlank(targetUrl)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "missing stream url");
		}
		URI uri;
		try {
			uri = new URI(targetUrl);
		} catch (URISyntaxException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid stream url");
		}
		String scheme = uri.getScheme();
		if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported scheme: " + scheme);
		}
		if (StringUtils.isBlank(uri.getHost())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "missing host in stream url");
		}
		if (!isKnownMediaServerHost(uri.getHost())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "host is not a known media server: " + uri.getHost());
		}
		return uri;
	}

	private boolean isKnownMediaServerHost(String host) {
		for (MediaServerDevice server : deviceRegistry.getAvailableMediaServer()) {
			try {
				URL descriptor = server.getDevice().getIdentity().getDescriptorURL();
				if (descriptor != null && host.equalsIgnoreCase(descriptor.getHost())) {
					return true;
				}
			} catch (RuntimeException e) {
				log.debug("could not read descriptor url of media server: {}", e.getMessage());
			}
		}
		return false;
	}
}
