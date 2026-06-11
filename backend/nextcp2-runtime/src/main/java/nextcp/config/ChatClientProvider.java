package nextcp.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

import com.google.genai.Client;

import nextcp.ai.AiModelCatalog;
import nextcp.dto.AiConfig;
import nextcp.dto.Config;

/**
 * Builds and caches the {@link ChatClient} for the currently configured AI
 * provider. The client is created lazily on first use and rebuilt automatically
 * whenever the relevant {@link AiConfig} fields change (provider, model, base URL
 * or API key), so a configuration change saved in the UI takes effect on the next
 * chat request without restarting the application.
 * <p>
 * The {@link Config} bean is the same instance that
 * {@code ServerConfig.updateAiConfig(...)} mutates in place, therefore reading
 * {@code config.aiConfig} here always reflects the latest saved values.
 */
@Component
public class ChatClientProvider {

	private static final Logger log = LoggerFactory.getLogger(ChatClientProvider.class.getName());

	private static final String SYSTEM_PROMPT = """
			You are the core logic interface of a UPnP control point audio system.

			OPERATIONAL RULES:
			1. Radio Control: Use your available MCP tools to start, change, or stop radio stations.
			2. Media Library: Use your available MCP tools to search for albums and playlists. Use the search results to play, queue, or stop specific albums and tracks.
			3. Device Management: Use your available MCP tools to switch between different Media Renderers and Media Servers.

			LANGUAGE HANDLING:
			Before performing any other action, detect the language of the user's input.
			- If the user writes in English, call the 'select_language' tool with the parameter "EN".
			- If the user writes in German, call the 'select_language' tool with the parameter "DE".
			- Only the language codes EN and DE are supported. For any other language, default to EN.
			- You may skip calling 'select_language' if the input language clearly matches the language already returned by 'selected_language' in this conversation; do not call it repeatedly for every message.
			- Always produce your final user-facing response in the detected input language, regardless of the language used in tool responses.

			SPEECH OUTPUT CONSTRAINT:
			Acknowledge the successful execution of any action in a single, extremely brief sentence suitable for text-to-speech output.
			""";

	/** Wildcard value for {@code aiToolIds}: use all tools offered by the server. */
	private static final String TOOL_IDS_WILDCARD = "*";

	private final Config config;
	private final ToolCallbackProvider upnpControlPointTools;
	private final AiModelCatalog aiModelCatalog;

	private ChatClient cachedClient;
	private String cachedSignature;

	public ChatClientProvider(Config config, ToolCallbackProvider upnpControlPointTools, AiModelCatalog aiModelCatalog) {
		this.config = config;
		this.upnpControlPointTools = upnpControlPointTools;
		this.aiModelCatalog = aiModelCatalog;
	}

	/**
	 * Returns the {@link ChatClient} for the current AI configuration, (re)building
	 * it when the configuration has changed since the last call.
	 *
	 * @return the chat client, or {@code null} when no usable provider is configured
	 */
	public synchronized ChatClient getChatClient() {
		AiConfig aiConfig = (config != null) ? config.aiConfig : null;
		// Resolve the server-side tool ids first: with the wildcard configured they are
		// fetched live from the server, so a changed toolset alters the signature and
		// triggers a client rebuild without any config change or restart.
		List<String> toolIds = resolveToolIds(aiConfig);
		String signature = signatureOf(aiConfig) + "|" + toolIds;

		if (cachedClient != null && Objects.equals(signature, cachedSignature)) {
			return cachedClient;
		}

		ChatModel chatModel = createChatModel(aiConfig, toolIds);
		cachedClient = (chatModel != null) ? buildChatClient(chatModel, aiConfig) : null;
		cachedSignature = signature;

		if (cachedClient != null) {
			log.info("ChatClient (re)built for provider '{}' with model '{}' ({}), sendTools={}.", aiConfig.aiProvider,
				aiConfig.aiModel, chatModel.getClass().getSimpleName(), shouldSendTools(aiConfig));
		}
		return cachedClient;
	}

	private ChatClient buildChatClient(ChatModel chatModel, AiConfig aiConfig) {
		ChatClient.Builder builder = ChatClient.builder(chatModel).defaultSystem(SYSTEM_PROMPT);
		if (shouldSendTools(aiConfig)) {
			// Register nextCP's own MCP tools (device / language control) with the request.
			builder.defaultTools(upnpControlPointTools);
		} else {
			// Do not send our tools, so an OpenAI-compatible server (e.g. OpenWebUI) can
			// resolve its own account/model-bound tools instead.
			log.info("aiSendTools=false: not registering nextCP MCP tools; provider-side tools (if any) will be used.");
		}
		return builder.build();
	}

	/**
	 * Whether nextCP should send its own tools. Defaults to {@code true} (previous
	 * behavior) when the flag is not set.
	 */
	private boolean shouldSendTools(AiConfig aiConfig) {
		return aiConfig == null || aiConfig.aiSendTools == null || Boolean.TRUE.equals(aiConfig.aiSendTools);
	}

	/**
	 * Cache key over the provider-relevant fields. {@code selectedRendererUdn} /
	 * {@code selectedServerUdn} are intentionally excluded because they are managed
	 * at runtime and must not trigger a client rebuild.
	 */
	private String signatureOf(AiConfig c) {
		if (c == null) {
			return "none";
		}
		return String.join("|", String.valueOf(c.aiEnabled), String.valueOf(c.aiSendTools), String.valueOf(c.aiProvider),
			String.valueOf(c.aiModel), String.valueOf(c.aiBaseUrl), String.valueOf(c.aiApiKey), String.valueOf(c.aiToolIds));
	}

	/**
	 * Selects and creates the {@link ChatModel} based on {@code aiConfig.aiProvider}.
	 * Provider-independent attributes such as the API key and the model name are
	 * read from the same {@link AiConfig} for every provider.
	 */
	private ChatModel createChatModel(AiConfig aiConfig, List<String> toolIds) {
		if (aiConfig == null || StringUtils.isBlank(aiConfig.aiProvider)) {
			log.info("No aiProvider configured - ChatModel will not be initialized.");
			return null;
		}

		String provider = aiConfig.aiProvider;
		if ("google".equalsIgnoreCase(provider)) {
			return buildGoogleChatModel(aiConfig);
		}
		if (isOpenAiCompatible(provider)) {
			return buildOpenAiChatModel(aiConfig, toolIds);
		}

		log.error("Unknown aiProvider '{}'. Supported values: google, openai, openwebui.", provider);
		return null;
	}

	private boolean isOpenAiCompatible(String provider) {
		return "openai".equalsIgnoreCase(provider) || "openwebui".equalsIgnoreCase(provider);
	}

	private ChatModel buildGoogleChatModel(AiConfig aiConfig) {
		try {
			log.info("Initializing GoogleGenAiChatModel with model: {}", aiConfig.aiModel);

			Client genAiClient = Client.builder().apiKey(aiConfig.aiApiKey).build();

			GoogleGenAiChatOptions defaultOptions = GoogleGenAiChatOptions.builder().model(aiConfig.aiModel).build();

			return GoogleGenAiChatModel.builder().genAiClient(genAiClient).options(defaultOptions).build();
		} catch (Exception e) {
			log.error("Error initializing GoogleGenAiChatModel.", e);
			return null;
		}
	}

	/**
	 * Builds a chat model for any OpenAI-compatible endpoint (e.g. a running
	 * OpenWebUI instance, vLLM, LiteLLM, Ollama's OpenAI layer).
	 * <p>
	 * Spring AI 2.0 builds the OpenAI model on top of the official openai-java SDK.
	 * The SDK appends the fixed resource path ({@code /chat/completions}) to the
	 * configured base URL, so {@code aiBaseUrl} must include the full prefix, e.g.
	 * {@code http://my-host:3000/api} for OpenWebUI.
	 */
	private ChatModel buildOpenAiChatModel(AiConfig aiConfig, List<String> toolIds) {
		try {
			log.info("Initializing OpenAiChatModel (provider={}) with model: {} at baseUrl: {}", aiConfig.aiProvider,
				aiConfig.aiModel, aiConfig.aiBaseUrl);

			if (StringUtils.isBlank(aiConfig.aiBaseUrl)) {
				log.error("No aiBaseUrl configured for OpenAI-compatible provider '{}'. ChatModel will not be initialized.",
					aiConfig.aiProvider);
				return null;
			}

			// In Spring AI 2.0 the OpenAI integration is backed by the official
			// openai-java SDK. baseUrl, apiKey and model are configured directly on
			// the options; OpenAiChatModel builds the underlying client from them.
			OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder()
				.baseUrl(aiConfig.aiBaseUrl)
				.apiKey(StringUtils.defaultString(aiConfig.aiApiKey))
				.model(aiConfig.aiModel);

			// OpenWebUI executes its own (server-side) tools for API requests only when
			// the request body contains a "tool_ids" array referencing registered tools
			// (e.g. "server:mcp:nextcp2"). The OpenAI API itself has no such field, so it
			// is injected as an extra body property. Requires the model's function calling
			// mode to be "default" in OpenWebUI, otherwise the tool calls are returned to
			// the client instead of being executed server-side.
			if (!toolIds.isEmpty()) {
				log.info("Injecting tool_ids {} into chat completion requests (server-side tool execution).", toolIds);
				optionsBuilder.extraBody(Map.of("tool_ids", toolIds));
			}

			return OpenAiChatModel.builder().options(optionsBuilder.build()).build();
		} catch (Exception e) {
			log.error("Error initializing OpenAiChatModel.", e);
			return null;
		}
	}

	/**
	 * Resolves the server-side tool ids for the given configuration. A blank value
	 * yields no tool ids, the wildcard {@code *} fetches all tools currently
	 * registered on the server (OpenAI-compatible providers only, e.g. OpenWebUI),
	 * otherwise the value is treated as a comma-separated list of tool ids.
	 */
	private List<String> resolveToolIds(AiConfig aiConfig) {
		if (aiConfig == null || StringUtils.isBlank(aiConfig.aiToolIds) || !isOpenAiCompatible(aiConfig.aiProvider)) {
			return List.of();
		}
		String value = aiConfig.aiToolIds.trim();
		if (TOOL_IDS_WILDCARD.equals(value)) {
			return aiModelCatalog.listServerToolIds(aiConfig);
		}
		return Arrays.stream(value.split(",")).map(String::trim).filter(StringUtils::isNotBlank).toList();
	}
}
