package nextcp.upnp.device.mediarenderer.playlist;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlaylistState;
import nextcp.dto.RendererPlaylist;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.PlaylistServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.PlaylistServiceStateVariable;

public class OhPlaylistServiceEventListener extends PlaylistServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(OhPlaylistServiceEventListener.class.getName());

    private OhPlaylistBridge playlist;
    private MediaRendererDevice device;
    private boolean shouldPublishTransportServiceState = false;
    
    public OhPlaylistServiceEventListener(OhPlaylistBridge playlist, MediaRendererDevice mediaRendererDevice)
    {
        this.playlist = playlist;
        this.device = mediaRendererDevice;
    }
    
    @Override
    public void eventProcessed()
    {
        if (!shouldPublishTransportServiceState)
        {
        	log.info(String.format("[%s] OhPlaylistServiceEventListener ignoring event.", device.getFriendlyName()));
            return;
        }

        PlaylistServiceStateVariable state = getStateVariable();
        
        PlaylistState dto = new PlaylistState();
        dto.udn = device.getUdnAsString();
        dto.Id = state.Id;
        dto.ProtocolInfo = state.ProtocolInfo;
        dto.Repeat = state.Repeat;
        dto.Shuffle = state.Shuffle;
        dto.TracksMax = state.TracksMax;
        dto.TransportState = state.TransportState;
        
        device.getEventPublisher().publishEvent(dto);
    }
    
    @Override
    public void idArrayChange(byte[] value)
    {
        super.idArrayChange(value);
        PlaylistChangedEvent event = new PlaylistChangedEvent();
        List<MusicItemDto> playlistItems = playlist.convertIdArrayToMusicItemList(value);
        event.rendererPlaylist = new RendererPlaylist(device.getUDN().getIdentifierString(), playlistItems);
        device.getEventPublisher().publishEvent(event);
    }
    
    @Override
    public void idChange(Long value)
    {
        super.idChange(value);
    }
    
    public boolean isShouldPublishTransportServiceState()
    {
        return shouldPublishTransportServiceState;
    }

    public void setShouldPublishTransportServiceState(boolean shouldPublishTransportServiceState)
    {
        this.shouldPublishTransportServiceState = shouldPublishTransportServiceState;
    }

}
