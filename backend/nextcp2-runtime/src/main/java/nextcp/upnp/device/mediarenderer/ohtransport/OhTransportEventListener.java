package nextcp.upnp.device.mediarenderer.ohtransport;

import nextcp.dto.TransportServiceStateDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.TransportServiceEventListenerImpl;

public class OhTransportEventListener extends TransportServiceEventListenerImpl
{
    private MediaRendererDevice device = null;

    public OhTransportEventListener(MediaRendererDevice device)
    {
        this.device = device;
    }

    @Override
    public void eventProcessed()
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
