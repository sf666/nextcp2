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
    private long dupCount = 0;
    private boolean shouldPublishTransportServiceState = false;

    private TransportServiceStateDto last_dto = new TransportServiceStateDto();

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
        if (!shouldPublishTransportServiceState)
        {
            return;
        }

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

        if (!dtoEqual(dto))
        {
            device.getEventPublisher().publishEvent(dto);
        }
        else
        {
            log.debug("NO STATE CHANGE.");
            dupCount++;
        }
        last_dto = dto;
    }

    private boolean dtoEqual(TransportServiceStateDto newDto)
    {
        if (newDto.canPause != last_dto.canPause)
        {
            return false;
        }
        if (newDto.canRepeat != last_dto.canRepeat)
        {
            return false;
        }
        if (newDto.canSeek != last_dto.canSeek)
        {
            return false;
        }
        if (newDto.canShuffle != last_dto.canShuffle)
        {
            return false;
        }
        if (newDto.canSkipNext != last_dto.canSkipNext)
        {
            return false;
        }
        if (newDto.repeat != last_dto.repeat)
        {
            return false;
        }
        if (newDto.shuffle != last_dto.shuffle)
        {
            return false;
        }
        if (!newDto.transportState.equals(newDto.transportState))
        {
            return false;
        }

        return true;
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
