package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextcp.ai.AiServices;

@RestController
@RequestMapping("/api/ai")
public class RestAiServices {

	private static final Logger log = LoggerFactory.getLogger(RestAiServices.class.getName());

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private AiServices aiServices;

	// @Autowired
	// private ToastEventPublisher toastEventPublisher = null;

	public RestAiServices() {
	}

	@PostMapping("/doAction")
	public String sendTextToGemini(@RequestBody String userText) {
		try {
			String extractedMessage = extractMessage(userText);
			return aiServices.sendTextToGemini(extractedMessage);
		} catch (Exception e) {
			log.error("Error processing AI request:", e);
			return "Error processing AI request: " + e.getMessage();
		}
	}

	private String extractMessage(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "";
		}
		try {
			// Prüfen, ob der String wie ein JSON-Objekt aussieht
			if (input.trim().startsWith("{")) {
				JsonNode rootNode = objectMapper.readTree(input);
				if (rootNode.has("message")) {
					return rootNode.get("message").asText();
				}
			}
		} catch (Exception e) {
			log.warn("Input looked like JSON but could not be parsed, using raw input. Error: {}", e.getMessage());
		}
		return input; // Fallback, falls kein JSON oder Feld fehlt
	}
}
