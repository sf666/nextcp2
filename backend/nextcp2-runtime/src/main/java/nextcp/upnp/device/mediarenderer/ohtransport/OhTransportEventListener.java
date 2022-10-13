package nextcp.upnp.device.mediarenderer.ohtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.TransportServiceStateDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.TransportServiceEventListenerImpl;

public class OhTransportEventListener extends TransportServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(OhTransportEventListener.class.getName());
    private MediaRendererDevice device = null;

    public OhTransportEventListener(MediaRendererDevice device)
    {
        this.device = device;
    }

    @Override
    public void eventProcessed()
    {
        publishState();        
    }

    private void publishState()
    {
        TransportServiceStateDto dto = new TransportServiceStateDto();
        
        dto.udn = device.getUdnAsString();
        dto.canPause = getStateVariable().CanPause;
        dto.canRepeat = getStateVariable().CanRepeat;
        dto.canSeek = getStateVariable().CanSeek;
        dto.canShuffle = getStateVariable().CanShuffle;
        dto.canSkipNext = getStateVariable().CanSkipNext;
        dto.canSkipPrevious = getStateVariable().CanSkipPrevious;
        dto.repeat = getStateVariable().Repeat;
        dto.shuffle = getStateVariable().Shuffle;
        dto.transportState = getStateVariable().TransportState;
        
        device.getEventPublisher().publishEvent(dto);
    }
}
