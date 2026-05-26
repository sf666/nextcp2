package nextcp.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AiConfig {

	private static final Logger log = LoggerFactory.getLogger(AiConfig.class);

    @Bean
    public ChatClient chatClient(@Lazy GoogleGenAiChatModel googleGenAiChatModel) {
        if (googleGenAiChatModel == null) {
            log.info("No AI ChatModel available - ChatClient will not be initialized.");
            return null;
        }
        return ChatClient.builder(googleGenAiChatModel)
                .defaultSystem(
                	"""
You are the core logic interface of a UPnP control point audio system.
            
            OPERATIONAL RULES:
            1. Radio Control: Use your available MCP tools to start, change, or stop radio stations.
            2. Media Library: Use your available MCP tools to search for albums and playlists. Use the search results to play, queue, or stop specific albums and tracks.
            3. Device Management: Use your available MCP tools to switch between different Media Renderers and Media Servers.
            
            SPEECH OUTPUT CONSTRAINT:
            Acknowledge the successful execution of any action in a single, extremely brief sentence suitable for text-to-speech output.

                	""")
                .build();
    }
}