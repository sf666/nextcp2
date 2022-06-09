package nextcp.eventBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import nextcp.dto.ServerPlaylists;

@Controller
public class MediaServerSseEvents
{
    public static final String DEVICE_MEDIASERVER_PLAYLIST_STATE = "DEVICE_MEDIASERVER_PLAYLIST_STATE";

    
    @Autowired
    private SsePublisher ssePublisher = null;
    
    @EventListener
    public void mediaServerChanged(ServerPlaylists serverPlaylists)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIASERVER_PLAYLIST_STATE, serverPlaylists);
    }

}
