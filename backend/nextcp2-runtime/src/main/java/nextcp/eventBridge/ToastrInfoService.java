package nextcp.eventBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import nextcp.dto.ToastrMessage;

/**
 * Forwards Toastr Events to the client
 */
public class ToastrInfoService
{

    public static final String TOASTR_INFO = "TOASTR_INFO";

    @Autowired
    private SsePublisher ssePublisher = null;

    @EventListener
    public void mediaRendererChanged(ToastrMessage event)
    {
        ssePublisher.sendObjectAsJson(TOASTR_INFO, event);
    }   
}
