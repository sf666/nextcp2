package nextcp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import nextcp.dto.ToastrMessage;

@Controller
public class ToastEventPublisher {

    @Autowired
    private ApplicationEventPublisher eventPublisher = null;

	public ToastEventPublisher() {
		// TODO Auto-generated constructor stub
	}

	
	public void publishMessage(String clientID, String type, String header, String body) {
        eventPublisher.publishEvent(new ToastrMessage(clientID, type, header, body));
	}
	
	public void publishInfoMessage(String clientID, String header, String body) {
        eventPublisher.publishEvent(new ToastrMessage(clientID, "info", header, body));
	}

	public void publishErrorMessage(String clientID, String header, String body) {
        eventPublisher.publishEvent(new ToastrMessage(clientID, "error", header, body));
	}

	public void publishSuccessMessage(String clientID, String header, String body) {
        eventPublisher.publishEvent(new ToastrMessage(clientID, "success", header, body));
	}

	public void publishWarningMessage(String clientID, String header, String body) {
        eventPublisher.publishEvent(new ToastrMessage(clientID, "warning", header, body));
	}
}
