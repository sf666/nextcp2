package nextcp.rest;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nextcp.ai.AiServices;

@RestController
@RequestMapping("/api/voice")
public class RestAlexaController {

	private static final Logger log = LoggerFactory.getLogger(RestAlexaController.class);

	@Autowired
	private AiServices aiServices;
	

	@PostMapping("/alexa")
	public Map<String, Object> handleAlexaRequest(@RequestBody Map<String, Object> requestJson) {
		try {
			var request = (Map<String, Object>) requestJson.get("request");
			String requestType = (String) request.get("type");

			log.info("Incoming Alexa Request-Typ: {}", requestType);

			if ("LaunchRequest".equals(requestType)) {
				return buildAlexaResponse("Your music assistant is ready. What would you like to listen to?", false);
			} else if ("IntentRequest".equals(requestType)) {
				var intent = (Map<String, Object>) request.get("intent");
				String intentName = (String) intent.get("name");
				String userText = null;

				log.info("Alexa Intent-Name: {}", intentName);

				if ("Befehl".equals(intentName)) {
					var slots = (Map<String, Object>) intent.get("slots");
					if (slots != null && slots.containsKey("meineFrage")) {
						var meineFrage = (Map<String, Object>) slots.get("meineFrage");
						userText = (String) meineFrage.get("value");
					}
				}
				else if ("AMAZON.FallbackIntent".equals(intentName)) {
					log.warn("Alexa went into fallback intent. This means Alexa couldn't match the user's utterance to any defined intent. The spoken text is not available in this case.");
					userText = null;
				}

				if (userText != null && !userText.isBlank()) {
					String aiResponse =  aiServices.sendTextToGemini(userText);
					return buildAlexaResponse(aiResponse, true);
				}
			}

			return buildAlexaResponse("I didn't quite catch that. Please repeat your command.", false);

		} catch (Exception e) {
			log.error("Error within Alexa-Controller", e);
			return buildAlexaResponse("An internal server error occurred in your Spring Boot backend.", true);
		}
	}

	private Map<String, Object> buildAlexaResponse(String text, boolean shouldEndSession) {
		return Map.of("version", "1.0", "response",
			Map.of("outputSpeech", Map.of("type", "PlainText", "text", text), "shouldEndSession", shouldEndSession));
	}
}
