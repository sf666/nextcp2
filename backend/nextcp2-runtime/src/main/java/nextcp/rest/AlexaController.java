package nextcp.rest;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/voice")
public class AlexaController {

	private static final Logger log = LoggerFactory.getLogger(AlexaController.class);
	private final ChatClient chatClient;

	public AlexaController(@Autowired(required = false) ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping("/alexa")
	public Map<String, Object> handleAlexaRequest(@RequestBody Map<String, Object> requestJson) {
		if (this.chatClient == null) {
			log.error("ChatClient ist nicht initialisiert! Bitte stellen Sie sicher, dass er korrekt konfiguriert ist.");
			return buildAlexaResponse("An internal server error occurred in your Spring Boot backend.", true);
		}
		
		try {
			var request = (Map<String, Object>) requestJson.get("request");
			String requestType = (String) request.get("type");

			log.info("Eingehender Alexa Request-Typ: {}", requestType);

			if ("LaunchRequest".equals(requestType)) {
				return buildAlexaResponse("Your music assistant is ready. What would you like to listen to?", false);
			} else if ("IntentRequest".equals(requestType)) {
				var intent = (Map<String, Object>) request.get("intent");
				String intentName = (String) intent.get("name");
				String userText = null;

				log.info("Eingehender Alexa Intent-Name: {}", intentName);

				if ("Befehl".equals(intentName)) {
					var slots = (Map<String, Object>) intent.get("slots");
					if (slots != null && slots.containsKey("meineFrage")) {
						var meineFrage = (Map<String, Object>) slots.get("meineFrage");
						userText = (String) meineFrage.get("value");
					}
				}
				else if ("AMAZON.FallbackIntent".equals(intentName)) {
					log.warn("Alexa ist in den Fallback gerutscht! Verwende Fallback-Text.");
					// We don't get the spoken text in a fallback
					userText = null;
				}

				if (userText != null && !userText.isBlank()) {
					log.info("Sende Text an Gemini: {}", userText);

					// send text and integrated @McpTools to GEMENI
					String aiResponse = chatClient.prompt().user(userText).call().content();

					return buildAlexaResponse(aiResponse, true);
				}
			}

			return buildAlexaResponse("I didn't quite catch that. Please repeat your command.", false);

		} catch (Exception e) {
			log.error("Kritischer Fehler im Alexa-Controller", e);
			return buildAlexaResponse("An internal server error occurred in your Spring Boot backend.", true);
		}
	}

	private Map<String, Object> buildAlexaResponse(String text, boolean shouldEndSession) {
		return Map.of("version", "1.0", "response",
			Map.of("outputSpeech", Map.of("type", "PlainText", "text", text), "shouldEndSession", shouldEndSession));
	}
}
