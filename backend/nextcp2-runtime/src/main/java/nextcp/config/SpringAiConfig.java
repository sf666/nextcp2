package nextcp.config;

import java.util.Arrays;
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
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import com.google.genai.Client;
import nextcp.ai.mcp.McpDevices;
import nextcp.ai.mcp.McpLocale;
import nextcp.dto.AiConfig;
import nextcp.dto.Config;

@Configuration
public class SpringAiConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringAiConfig.class.getName());

	@Bean
	public ToolCallbackProvider upnpControlPointTools(McpDevices mcpDevices, McpLocale mcpLocale) {
		ToolCallbackProvider provider = MethodToolCallbackProvider.builder().toolObjects(mcpDevices, mcpLocale).build();

		log.info("=== Registered MCP Tools ===");
		Arrays.asList(provider.getToolCallbacks()).forEach(tool -> log.info("  Tool registered: name={}", tool.getToolDefinition().name()));

		return provider;
	}

	private boolean isOpenAiCompatible(String provider) {
		return "openai".equalsIgnoreCase(provider) || "openwebui".equalsIgnoreCase(provider);
	}

	/**
	 * Selects and creates the {@link ChatModel} based on {@code aiConfig.aiProvider}.
	 * Provider-independent attributes such as the API key and the model name are
	 * read from the same {@link AiConfig} for every provider.
	 *
	 * @param aiConfig the AI configuration (may be {@code null})
	 * @return the configured chat model, or {@code null} when no usable provider is set
	 */
	private ChatModel createChatModel(AiConfig aiConfig) {
		if (aiConfig == null || StringUtils.isBlank(aiConfig.aiProvider)) {
			log.info("No aiProvider configured - ChatModel will not be initialized.");
			return null;
		}

		String provider = aiConfig.aiProvider;
		if ("google".equalsIgnoreCase(provider)) {
			return buildGoogleChatModel(aiConfig);
		}
		if (isOpenAiCompatible(provider)) {
			return buildOpenAiChatModel(aiConfig);
		}

		log.error("Unknown aiProvider '{}'. Supported values: google, openai, openwebui.", provider);
		return null;
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
	 * configured base URL, so {@code aiBaseUrl} must include the full prefix:
	 * <ul>
	 * <li>OpenWebUI OpenAI-compatible layer: {@code http://my-host:3000/v1}</li>
	 * <li>OpenWebUI native endpoint: {@code http://my-host:3000/api}</li>
	 * </ul>
	 */
	private ChatModel buildOpenAiChatModel(AiConfig aiConfig) {
		try {
			log.info("Initializing OpenAiChatModel (provider={}) with model: {} at baseUrl: {}",
					aiConfig.aiProvider, aiConfig.aiModel, aiConfig.aiBaseUrl);

			if (StringUtils.isBlank(aiConfig.aiBaseUrl)) {
				log.error("No aiBaseUrl configured for OpenAI-compatible provider '{}'. ChatModel will not be initialized.",
						aiConfig.aiProvider);
				return null;
			}

			// In Spring AI 2.0 the OpenAI integration is backed by the official
			// openai-java SDK. baseUrl, apiKey and model are configured directly on
			// the options; OpenAiChatModel builds the underlying client from them.
			OpenAiChatOptions options = OpenAiChatOptions.builder()
					.baseUrl(aiConfig.aiBaseUrl)
					.apiKey(StringUtils.defaultString(aiConfig.aiApiKey))
					.model(aiConfig.aiModel)
					.build();

			return OpenAiChatModel.builder().options(options).build();
		} catch (Exception e) {
			log.error("Error initializing OpenAiChatModel.", e);
			return null;
		}
	}

    /**
     * Builds the {@link ChatClient} for the provider selected via
     * {@code aiConfig.aiProvider}. It's important to register the MCP Tools used by
     * this application.
     *
     * @param config global application configuration
     * @param upnpControlPointTools registered MCP tool callbacks
     * @return the configured ChatClient, or null when no provider is available
     */
    @Bean
    @DependsOn("rendererConfigProducer")
    public ChatClient chatClient(Config config, ToolCallbackProvider upnpControlPointTools) {

        ChatModel chatModel = createChatModel(config != null ? config.aiConfig : null);

        if (chatModel == null) {
            log.info("No AI ChatModel available - ChatClient will not be initialized.");
            return null;
        }

        log.info("Initializing ChatClient with model implementation: {}", chatModel.getClass().getSimpleName());

        return ChatClient.builder(chatModel)
        	.defaultTools(upnpControlPointTools)
            .defaultSystem(
                 """
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
                 """)
                .build();
    }	
}