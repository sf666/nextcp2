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
import java.time.Duration;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

/**
 * Server-side pass-through proxy that streams a media server (UMS) resource to the browser for
 * local HTML5 {@code <audio>} playback (the "This Browser" virtual renderer).
 * <p>
 * The browser cannot set a custom {@code User-Agent} on an {@code <audio src>}, so it cannot make
 * UMS recognize a browser-specific renderer profile. This proxy fetches the media on the browser's
 * behalf and tags every request with {@link #PROXY_USER_AGENT}, which the UMS
 * {@code nextcp2webplayer.conf} renderer profile matches. UMS then streams browser-native formats untouched and transcodes the
 * rest to MP3. It also sidesteps CORS / mixed-content issues when the UI is served over HTTPS.
 * <p>
 * HTTP {@code Range} requests are forwarded verbatim so the browser can seek, and the upstream
 * status and relevant response headers are relayed back unchanged.
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

	private final DeviceRegistry deviceRegistry;

	private final HttpClient httpClient = HttpClient.newBuilder()
		.followRedirects(HttpClient.Redirect.NORMAL)
		.connectTimeout(Duration.ofSeconds(15))
		.build();

	public LocalStreamProxyService(DeviceRegistry deviceRegistry) {
		this.deviceRegistry = deviceRegistry;
	}

	/**
	 * Fetches {@code targetUrl} from a known media server and streams it back through
	 * {@code response}, forwarding the client's {@code Range} header and relaying the upstream
	 * status and headers.
	 *
	 * @param targetUrl the media server resource URL (must belong to a discovered media server)
	 * @param request   the incoming browser request (used for the {@code Range} header)
	 * @param response  the response the media bytes are written to
	 */
	public void proxy(String targetUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
		URI uri = validateTarget(targetUrl);

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

	private void relayHeader(HttpResponse<InputStream> upstream, HttpServletResponse response, String name) {
		Optional<String> value = upstream.headers().firstValue(name);
		value.ifPresent(v -> response.setHeader(name, v));
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
