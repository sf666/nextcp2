package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nextcp.ai.AiServices;

@RestController
@RequestMapping("/api/ai")
public class RestAiServices {
	
	private static final Logger log = LoggerFactory.getLogger(RestAiServices.class.getName());
	
	@Autowired
	private AiServices aiServices;

//	@Autowired
//    private ToastEventPublisher toastEventPublisher = null;
	

	public RestAiServices() {

	}

	@PostMapping("/doAction")
	public String sendTextToGemini(@RequestBody String userText) {
		try {
			return aiServices.sendTextToGemini(userText);			
		} catch (Exception e) {
			log.error("Error processing AI request:", e);
			return "Error processing AI request: " + e.getMessage();
		}
	}
	
}
