package nextcp.eventBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import nextcp.dto.DeviceDriverState;
import nextcp.dto.InputSourceChangeDto;
import nextcp.dto.PlaylistState;
import nextcp.dto.TrackInfoDto;
import nextcp.dto.TrackTimeDto;
import nextcp.dto.TransportServiceStateDto;
import nextcp.dto.UpnpAvTransportState;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.avtransport.event.AvTransportStateChangedEvent;
import nextcp.upnp.device.mediarenderer.playlist.PlaylistChangedEvent;

@Controller
public class MediaRendererSseEvents
{
    public static final String DEVICE_MEDIARENDERER_DEVICE_DRIVER_STATE_CHANGED = "DEVICE_MEDIARENDERER_DEVICE_DRIVER_STATE_CHANGED";
    public static final String DEVICE_MEDIARENDERER_AVTRANSPORT = "AVTRANSPORT_STATE";
    public static final String DEVICE_MEDIARENDERER_TRANSPORT = "TRANSPORT_STATE";
    public static final String DEVICE_MEDIARENDERER_INPUT_SOURCE = "DEVICE_MEDIARENDERER_INPUT_SOURCE";
    public static final String DEVICE_MEDIARENDERER_TRACK_INFO = "DEVICE_MEDIARENDERER_TRACK_INFO";
    public static final String DEVICE_MEDIARENDERER_TRACK_TIME = "DEVICE_MEDIARENDERER_TRACK_TIME";
    public static final String DEVICE_MEDIARENDERER_PLAYLIST_ITEMS = "DEVICE_MEDIARENDERER_PLAYLIST_ITEMS";
    public static final String DEVICE_MEDIARENDERER_PLAYLIST_STATE = "DEVICE_MEDIARENDERER_PLAYLIST_STATE";

    @Autowired
    private DtoBuilder dtoBuilder = null;

    @Autowired
    private SsePublisher ssePublisher = null;

    //
    // Render Device specific event listener propagation
    // =============================================================================================================================================================================

    @EventListener
    public void listenForInputSourceChanged(InputSourceChangeDto inputSource)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_INPUT_SOURCE, inputSource);
    }
    
    @EventListener
    public void listenForDeviceDriverStateChanged(DeviceDriverState deviceDriverState)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_DEVICE_DRIVER_STATE_CHANGED, deviceDriverState);
    }
    
    @EventListener
    public void listenForUpnpStateVariable(AvTransportStateChangedEvent event)
    {
        UpnpAvTransportState dto = dtoBuilder.buildAvTransportStateDto(event.state, event.device);
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_AVTRANSPORT, dto);
    }

    @EventListener
    public void listenForTransportStateVariable(TransportServiceStateDto event)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_TRANSPORT, event);
    }
    
    
    @EventListener
    public void listenForTrackInfoStateVariable(TrackInfoDto event)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_TRACK_INFO, event);
    }
    
    @EventListener
    public void listenForPositionInfoStateVariable(TrackTimeDto event)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_TRACK_TIME, event);
    }
    
    //
    // Playlist Events
    //
    
    @EventListener
    public void listenForPlaylistItems(PlaylistChangedEvent  event)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_PLAYLIST_ITEMS, event.rendererPlaylist);
    }
    
    @EventListener
    public void listenForPlaylistState(PlaylistState event)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIARENDERER_PLAYLIST_STATE, event);
    }
    
}
