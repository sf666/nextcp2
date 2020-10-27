package nextcp.domainmodel.device.mediarenderer.playlist;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlaylistState;
import nextcp.dto.RendererPlaylist;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.PlaylistServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.PlaylistServiceStateVariable;

public class OhPlaylistServiceEventListener extends PlaylistServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(OhPlaylistServiceEventListener.class.getName());

    private ApplicationEventPublisher eventPublisher = null;
    private OhPlaylist playlist;
    private MediaRendererDevice device;

    public OhPlaylistServiceEventListener(ApplicationEventPublisher eventPublisher, OhPlaylist playlist, MediaRendererDevice mediaRendererDevice)
    {
        this.eventPublisher = eventPublisher;
        this.playlist = playlist;
        this.device = mediaRendererDevice;
    }
    
    @Override
    public void eventProcessed()
    {
        super.eventProcessed();

        PlaylistServiceStateVariable state = getStateVariable();
        
        PlaylistState dto = new PlaylistState();
        dto.udn = device.getUdnAsString();
        dto.Id = state.Id;
        dto.ProtocolInfo = state.ProtocolInfo;
        dto.Repeat = state.Repeat;
        dto.Shuffle = state.Shuffle;
        dto.TracksMax = state.TracksMax;
        dto.TransportState = state.TransportState;
        
        eventPublisher.publishEvent(dto);
    }
    
    @Override
    public void idArrayChange(byte[] value)
    {
        super.idArrayChange(value);
        PlaylistChangedEvent event = new PlaylistChangedEvent();
        List<MusicItemDto> playlistItems = playlist.convertIdArrayToMusicItemList(value);
        event.rendererPlaylist = new RendererPlaylist(device.getUDN().getIdentifierString(), playlistItems);
        eventPublisher.publishEvent(event);
    }
    
    @Override
    public void idChange(Long value)
    {
        super.idChange(value);
//        ReadOutput out = playlist.read(value);
//        log.debug("idArrayChange Event Metadata : " + out.Metadata);
    }
}
