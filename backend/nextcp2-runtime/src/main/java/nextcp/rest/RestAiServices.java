package nextcp.rest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
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

	public RestAiServices() {
	}

	@PostMapping(value = "/doAction", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter sendTextToGemini(@RequestBody String userText) {
		SseEmitter emitter = new SseEmitter(120000L);

		CompletableFuture.runAsync(() -> {
			try {
				String extractedMessage = extractMessage(userText);
				String response = aiServices.sendTextToGemini(extractedMessage);
				emitter.send(SseEmitter.event().data(Map.of("response", response), MediaType.APPLICATION_JSON));
				emitter.send(SseEmitter.event().data("[DONE]", MediaType.TEXT_PLAIN));
				emitter.complete();
			} catch (Exception e) {
				log.error("Error processing AI request:", e);
				try {
					emitter.send(SseEmitter.event().data(Map.of("response", "Error processing AI request: " + e.getMessage()), MediaType.APPLICATION_JSON));
					emitter.send(SseEmitter.event().data("[DONE]", MediaType.TEXT_PLAIN));
					emitter.complete();
				} catch (IOException ioException) {
					log.warn("Could not send error response over SSE: {}", ioException.getMessage());
					emitter.completeWithError(e);
				}
			}
		});

		return emitter;
	}

	private String extractMessage(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "";
		}
		try {
			if (input.trim().startsWith("{")) {
				JsonNode rootNode = objectMapper.readTree(input);
				if (rootNode.has("message")) {
					return rootNode.get("message").asText();
				}
			}
		} catch (Exception e) {
			log.warn("Input looked like JSON but could not be parsed, using raw input. Error: {}", e.getMessage());
		}
		return input; 
	}
}
