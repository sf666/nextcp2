package nextcp.config;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import com.google.genai.Client;
import nextcp.ai.mcp.McpDevices;
import nextcp.ai.mcp.McpLocale;
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

	@Bean
	@DependsOn("rendererConfigProducer")
	public GoogleGenAiChatModel googleGenAiChatModel(Config config) {
		try {
			if (config != null && config.aiConfig != null && "google".equalsIgnoreCase(config.aiConfig.aiProvider)) {

				log.info("Initializing GoogleGenAiChatModel with model: {}", config.aiConfig.aiModel);

				Client genAiClient = Client.builder().apiKey(config.aiConfig.aiApiKey).build();

				GoogleGenAiChatOptions defaultOptions = GoogleGenAiChatOptions.builder().model(config.aiConfig.aiModel).build();

				return GoogleGenAiChatModel.builder().genAiClient(genAiClient).defaultOptions(defaultOptions).build();
			}
		} catch (Exception e) {
			log.error("Error initializing GoogleGenAiChatModel.", e);
		}
		return null;
	}
	
    /**
     * It's important to register the MCP Tools used by this application.
     * @param googleGenAiChatModel
     * @param upnpControlPointTools
     * @return
     */
    @Bean
    public ChatClient chatClient(
            @Lazy GoogleGenAiChatModel googleGenAiChatModel,
            ToolCallbackProvider upnpControlPointTools) {

        if (googleGenAiChatModel == null) {
            log.info("No AI ChatModel available - ChatClient will not be initialized.");
            return null;
        }

        return ChatClient.builder(googleGenAiChatModel)
        	.defaultToolCallbacks(upnpControlPointTools.getToolCallbacks())
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