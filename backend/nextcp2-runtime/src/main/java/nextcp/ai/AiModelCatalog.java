package nextcp.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextcp.dto.AiConfig;
import nextcp.dto.AiToolDto;

/**
 * Catalog of the AI providers implemented by this backend and the models
 * available behind each of them.
 * <ul>
 * <li>The list of providers reflects the implemented chat-model builders (see
 * {@code ChatClientProvider}): currently {@code google} and {@code openai}
 * (OpenAI-compatible, which also covers an OpenWebUI endpoint via its base URL).</li>
 * <li>For an OpenAI-compatible provider the models are fetched live from
 * {@code {aiBaseUrl}/models}. For Google a curated static list is returned.</li>
 * </ul>
 */
@Service
public class AiModelCatalog {

	private static final Logger log = LoggerFactory.getLogger(AiModelCatalog.class);

	public static final String PROVIDER_GOOGLE = "google";
	public static final String PROVIDER_OPENAI = "openai";

	/** Providers offered in the UI, derived from the implemented model builders. */
	private static final List<String> SUPPORTED_PROVIDERS = List.of(PROVIDER_GOOGLE, PROVIDER_OPENAI);

	/**
	 * Fallback list of common Google Gemini chat models, used when the live model
	 * listing is not possible (e.g. no API key entered yet) or fails.
	 */
	private static final List<String> GOOGLE_MODELS = List.of("gemini-2.5-pro", "gemini-2.5-flash", "gemini-2.0-flash",
		"gemini-1.5-pro", "gemini-1.5-flash");

	/** Gemini API endpoint listing the available models. */
	private static final String GOOGLE_MODELS_URL = "https://generativelanguage.googleapis.com/v1beta/models?pageSize=1000";

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

	/** How long a successful model/tool listing is reused before being re-fetched. */
	private static final Duration CACHE_TTL = Duration.ofMinutes(60 * 24); // 24h

	private final Map<String, CacheEntry<List<String>>> modelsCache = new ConcurrentHashMap<>();
	private final Map<String, CacheEntry<List<AiToolDto>>> toolsCache = new ConcurrentHashMap<>();

	private static long expiry() {
		return System.currentTimeMillis() + CACHE_TTL.toMillis();
	}

	private static String cacheKey(AiConfig c) {
		return StringUtils.lowerCase(StringUtils.trimToEmpty(c.aiProvider)) + '|'
			+ StringUtils.trimToEmpty(c.aiBaseUrl) + '|'
			+ StringUtils.trimToEmpty(c.aiApiKey);
	}

	/** Cached value with an absolute expiry timestamp (epoch millis). */
	private record CacheEntry<T>(T value, long expiresAtMillis) {
		boolean isExpired() {
			return System.currentTimeMillis() > expiresAtMillis;
		}
	}

	/**
	 * @return the AI providers supported by this backend
	 */
	public List<String> getSupportedProviders() {
		return SUPPORTED_PROVIDERS;
	}

	/**
	 * Returns the models available for the given configuration's provider.
	 *
	 * @param aiConfig the AI configuration to inspect (may be {@code null})
	 * @return a (possibly empty) list of model identifiers, never {@code null}
	 */
	public List<String> getAvailableModels(AiConfig aiConfig) {
		if (aiConfig == null || !Boolean.TRUE.equals(aiConfig.aiEnabled) || StringUtils.isBlank(aiConfig.aiProvider)) {
			return List.of();
		}
		String key = cacheKey(aiConfig);
		CacheEntry<List<String>> cached = modelsCache.get(key);
		if (cached != null && !cached.isExpired()) {
			return cached.value();
		}
		List<String> models = fetchAvailableModels(aiConfig);
		// Only cache non-empty results
		if (!models.isEmpty()) {
			modelsCache.put(key, new CacheEntry<>(models, expiry()));
		}
		return models;
	}

	private List<String> fetchAvailableModels(AiConfig aiConfig) {
		String provider = aiConfig.aiProvider;
		if (PROVIDER_GOOGLE.equalsIgnoreCase(provider)) {
			return listGoogleModels(aiConfig);
		}
		if (isOpenAiCompatible(provider)) {
			return listOpenAiCompatibleModels(aiConfig);
		}
		log.warn("Cannot list models for unknown aiProvider '{}'.", provider);
		return List.of();
	}

	/**
	 * Picks a sensible default model for the given configuration, used when the
	 * user left the model empty. Returns the first available model, or {@code null}
	 * when none could be determined.
	 */
	public String pickDefaultModel(AiConfig aiConfig) {
		List<String> models = getAvailableModels(aiConfig);
		return models.isEmpty() ? null : models.get(0);
	}

	/**
	 * Queries the Gemini API for the available models and extracts the chat-capable
	 * ones (supporting {@code generateContent}) from the {@code models[].name}
	 * response, stripping the {@code models/} prefix. Falls back to the curated
	 * {@link #GOOGLE_MODELS} list when no API key is configured or the request fails.
	 */
	private List<String> listGoogleModels(AiConfig aiConfig) {
		if (StringUtils.isBlank(aiConfig.aiApiKey)) {
			log.info("No Google API key configured - falling back to the curated model list.");
			return GOOGLE_MODELS;
		}
		try {
			HttpRequest request = HttpRequest.newBuilder(URI.create(GOOGLE_MODELS_URL))
				.header("x-goog-api-key", aiConfig.aiApiKey)
				.GET().timeout(Duration.ofSeconds(8)).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() / 100 != 2) {
				log.warn("Listing Google models failed with HTTP {} - falling back to the curated model list.",
					response.statusCode());
				return GOOGLE_MODELS;
			}

			JsonNode models = objectMapper.readTree(response.body()).path("models");
			List<String> result = new ArrayList<>();
			if (models.isArray()) {
				for (JsonNode model : models) {
					if (!supportsGenerateContent(model)) {
						continue;
					}
					String name = model.path("name").asText(null);
					if (StringUtils.isNotBlank(name)) {
						result.add(StringUtils.removeStart(name, "models/"));
					}
				}
			}
			return result.isEmpty() ? GOOGLE_MODELS : result;
		} catch (Exception e) {
			log.warn("Could not list Google models ({}) - falling back to the curated model list.", e.getMessage());
			return GOOGLE_MODELS;
		}
	}

	/** Whether the Gemini model supports chat ({@code generateContent}). */
	private boolean supportsGenerateContent(JsonNode model) {
		JsonNode methods = model.path("supportedGenerationMethods");
		if (!methods.isArray()) {
			return false;
		}
		for (JsonNode method : methods) {
			if ("generateContent".equals(method.asText())) {
				return true;
			}
		}
		return false;
	}

	private boolean isOpenAiCompatible(String provider) {
		return "openai".equalsIgnoreCase(provider) || "openwebui".equalsIgnoreCase(provider);
	}

	/**
	 * Lists the ids of all tools registered on an OpenWebUI server by querying
	 * {@code {aiBaseUrl}/v1/tools/}. These ids can be passed as {@code tool_ids} in a
	 * chat completion request so OpenWebUI executes the tools server-side. Network or
	 * parse errors are logged and result in an empty list. Non-OpenWebUI endpoints
	 * (e.g. api.openai.com) simply return an error status, which also yields an
	 * empty list.
	 *
	 * @param aiConfig the AI configuration providing base URL and API key
	 * @return a (possibly empty) list of tool ids, never {@code null}
	 */
	public List<String> listServerToolIds(AiConfig aiConfig) {
		return listServerTools(aiConfig).stream().map(t -> t.id).toList();
	}

	/**
	 * Lists all tools registered on an OpenWebUI server by querying
	 * {@code {aiBaseUrl}/v1/tools/}, extracting id and display name per tool.
	 * Network or parse errors are logged and result in an empty list. Non-OpenWebUI
	 * endpoints (e.g. api.openai.com) simply return an error status, which also
	 * yields an empty list.
	 *
	 * @param aiConfig the AI configuration providing base URL and API key
	 * @return a (possibly empty) list of tools, never {@code null}
	 */
	public List<AiToolDto> listServerTools(AiConfig aiConfig) {
		if (aiConfig == null || !Boolean.TRUE.equals(aiConfig.aiEnabled) || StringUtils.isBlank(aiConfig.aiBaseUrl)
			|| !isOpenAiCompatible(aiConfig.aiProvider)) {
			return List.of();
		}
		String key = cacheKey(aiConfig);
		CacheEntry<List<AiToolDto>> cached = toolsCache.get(key);
		if (cached != null && !cached.isExpired()) {
			return cached.value();
		}
		List<AiToolDto> tools = fetchServerTools(aiConfig);
		// See getAvailableModels: don't cache empty results so errors are retried.
		if (!tools.isEmpty()) {
			toolsCache.put(key, new CacheEntry<>(tools, expiry()));
		}
		return tools;
	}

	private List<AiToolDto> fetchServerTools(AiConfig aiConfig) {
		try {
			String base = StringUtils.removeEnd(aiConfig.aiBaseUrl.trim(), "/");
			URI uri = URI.create(base + "/v1/tools/");

			HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(8));
			if (StringUtils.isNotBlank(aiConfig.aiApiKey)) {
				requestBuilder.header("Authorization", "Bearer " + aiConfig.aiApiKey);
			}

			HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() / 100 != 2) {
				log.warn("Listing tools from {} failed with HTTP {}.", uri, response.statusCode());
				return List.of();
			}

			// OpenWebUI returns a top-level JSON array of tool objects; tolerate a
			// wrapped {"data": [...]} structure as well.
			JsonNode root = objectMapper.readTree(response.body());
			JsonNode tools = root.isArray() ? root : root.path("data");
			List<AiToolDto> result = new ArrayList<>();
			if (tools.isArray()) {
				for (JsonNode tool : tools) {
					String id = tool.path("id").asText(null);
					if (StringUtils.isNotBlank(id)) {
						AiToolDto dto = new AiToolDto();
						dto.id = id;
						dto.name = tool.path("name").asText(id);
						result.add(dto);
					}
				}
			}
			return result;
		} catch (Exception e) {
			log.warn("Could not list tools from OpenAI-compatible endpoint: {}", e.getMessage());
			return List.of();
		}
	}

	/**
	 * Queries {@code {aiBaseUrl}/models} (OpenAI-compatible) and extracts the model
	 * ids from the {@code data[].id} response. Network/parse errors are logged and
	 * result in an empty list so the UI can fall back gracefully.
	 */
	private List<String> listOpenAiCompatibleModels(AiConfig aiConfig) {
		if (StringUtils.isBlank(aiConfig.aiBaseUrl)) {
			log.info("No aiBaseUrl configured - cannot list OpenAI-compatible models.");
			return List.of();
		}
		try {
			String base = StringUtils.removeEnd(aiConfig.aiBaseUrl.trim(), "/");
			URI uri = URI.create(base + "/models");

			HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(8));
			if (StringUtils.isNotBlank(aiConfig.aiApiKey)) {
				requestBuilder.header("Authorization", "Bearer " + aiConfig.aiApiKey);
			}

			HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() / 100 != 2) {
				log.warn("Listing models from {} failed with HTTP {}.", uri, response.statusCode());
				return List.of();
			}

			JsonNode data = objectMapper.readTree(response.body()).path("data");
			List<String> models = new ArrayList<>();
			if (data.isArray()) {
				for (JsonNode model : data) {
					String id = model.path("id").asText(null);
					if (StringUtils.isNotBlank(id)) {
						models.add(id);
					}
				}
			}
			return models;
		} catch (Exception e) {
			log.warn("Could not list models from OpenAI-compatible endpoint: {}", e.getMessage());
			return List.of();
		}
	}
}
