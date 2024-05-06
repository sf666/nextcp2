package nextcp.eventBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import nextcp.dto.ToastrMessage;

/**
 * Forwards Toastr Events to the client
 */
@Controller
public class ToastrInfoService
{

    public static final String TOASTR_INFO = "TOASTR_INFO";

    @Autowired
    private SsePublisher ssePublisher = null;

    @EventListener
    public void toastToClient(ToastrMessage event)
    {
        ssePublisher.sendObjectAsJson(TOASTR_INFO, event);
    }
}
