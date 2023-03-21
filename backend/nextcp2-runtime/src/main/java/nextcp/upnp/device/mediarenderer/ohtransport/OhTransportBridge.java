package nextcp.upnp.device.mediarenderer.ohtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.domainmodel.device.services.ITransport;
import nextcp.dto.ToastrMessage;
import nextcp.dto.TransportServiceStateDto;
import nextcp.eventBridge.ToastrInfoService;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.OpenHomeUtils;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.TransportService;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModeInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamInfoOutput;

/**
 * This class connects to the Open Home transport service.
 */
public class OhTransportBridge implements ITransport
{
    private static final Logger log = LoggerFactory.getLogger(OhTransportBridge.class.getName());

    private TransportService transportService = null;
    private MediaRendererDevice device = null;
    private OhTransportEventListener ohTransportEventListener = null;
    
    private ApplicationEventPublisher publisher = null;

    
    public OhTransportBridge(MediaRendererDevice device,  TransportService transportService, OhTransportEventListener ohTransportEventListener, ApplicationEventPublisher publisher)
    {
        this.transportService = transportService;
        this.device = device;
        this.publisher = publisher;
        this.ohTransportEventListener = ohTransportEventListener;
    }

    @Override
    public void play()
    {
        transportService.play();
    }

    @Override
    public void pause()
    {
        transportService.pause();
    }

    @Override
    public void stop()
    {
        transportService.stop();
    }

    @Override
    public void next()
    {
        transportService.skipNext();
    }

    @Override
    public void seek(long secondsAbsolute)
    {
        try
        {
            if (ohTransportEventListener.getStateVariable().CanSeek == null || ohTransportEventListener.getStateVariable().CanSeek)
            {
                if (ohTransportEventListener.getStateVariable().CanSeek == null)
                {
                    log.warn("CanSeek state variable not set. Trying anyway ... ");
                }
                SeekSecondAbsoluteInput inp = new SeekSecondAbsoluteInput();
                inp.SecondAbsolute = secondsAbsolute;
                inp.StreamId = transportService.streamId().StreamId;
                transportService.seekSecondAbsolute(inp);
            }
            else
            {
                publisher.publishEvent(new ToastrMessage(null, "warn", "seek", "current stream cannot be seeked."));
            }
        }
        catch (Exception e)
        {
            log.error("seek", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "seek", e.getMessage()));
        }
    }

    @Override
    public TransportServiceStateDto getCurrentTransportServiceState()
    {
        TransportServiceStateDto dto = new TransportServiceStateDto();
        
        ModeInfoOutput mod =  transportService.modeInfo();
        dto.canRepeat = mod.CanRepeat;
        dto.canShuffle = mod.CanShuffle;
        dto.canSkipNext = mod.CanSkipNext;
        dto.canSkipPrevious = mod.CanSkipPrevious;
        
        dto.transportState = transportService.transportState().State;
        dto.repeat = transportService.repeat().Repeat;
        dto.shuffle = transportService.shuffle().Shuffle;
        
        StreamInfoOutput si = transportService.streamInfo();
        dto.canPause = si.CanPause;
        dto.canSeek = si.CanSeek;
        
        dto.udn = device.getUdnAsString();
        
        return dto;
    }

}
