package nextcp.eventBridge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaServerDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.MediaRendererListChanged;
import nextcp.upnp.device.mediaserver.MediaServerListChanged;

@Controller
public class DeviceRegistrySseEvents
{
    public static final String DEVICE_MEDIARENDERER_LIST_CHANGED = "DEVICE_MEDIARENDERER_LIST_CHANGED";
    public static final String DEVICE_MEDIASERVER_LIST_CHANGED = "DEVICE_MEDIASERVER_CHANGED";

    @Autowired
    private SsePublisher ssePublisher = null;

    @Autowired
    private DtoBuilder dtoBuilder = null;

    @EventListener
    public void mediaRendererChanged(MediaRendererListChanged event)
    {
        List<MediaRendererDto> mediaRenderer = dtoBuilder.getMediaRendererAsDto(event.availableMediaRenderer);
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_LIST_CHANGED, mediaRenderer);
    }

    @EventListener
    public void mediaServerChanged(MediaServerListChanged event)
    {
        List<MediaServerDto> mediaServer = dtoBuilder.getMediaServerAsDto(event.availableMediaServer);
        ssePublisher.sendObjectAsJson(DEVICE_MEDIASERVER_LIST_CHANGED, mediaServer);
    }   
}
