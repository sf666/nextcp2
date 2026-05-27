package nextcp.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service 
 */
@Service
public class AiServices {
	private static final Logger log = LoggerFactory.getLogger(AiServices.class.getName());
	
	private final ChatClient chatClient;
    
	public AiServices(@Autowired(required = false) ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	
	public String sendTextToGemini(String userText) {
		if (this.chatClient == null) {
			throw new RuntimeException("ChatClient  not initialized! Please ensure it is properly configured.");
		}
		log.info("Sending text to Gemini: {}", userText);
		
		// send text and integrated @McpTools to GEMENI		
		String retVal = chatClient.prompt().user(userText).call().content();
		
		log.info("Received response from Gemini: {}", retVal);
		return retVal;
	}
}
