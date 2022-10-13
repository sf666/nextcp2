package nextcp.upnp.device.mediarenderer.ohtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.services.ITransport;
import nextcp.dto.TransportServiceStateDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.OpenHomeUtils;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.TransportService;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModeInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamInfoOutput;

public class OhTransportBridge implements ITransport
{
    private static final Logger log = LoggerFactory.getLogger(OhTransportBridge.class.getName());

    private TransportService transportService = null;
    private OpenHomeUtils ohUtil = null;
    private MediaRendererDevice device = null;

    public OhTransportBridge(MediaRendererDevice device,  TransportService transportService, DtoBuilder dtoBuilder)
    {
        this.transportService = transportService;
        this.ohUtil = new OpenHomeUtils(dtoBuilder);
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
        transportService.stop();
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
