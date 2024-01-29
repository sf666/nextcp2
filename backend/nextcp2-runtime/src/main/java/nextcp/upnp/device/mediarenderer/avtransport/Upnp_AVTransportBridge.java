package nextcp.upnp.device.mediarenderer.avtransport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.services.IInfoService;
import nextcp.domainmodel.device.services.ITimeService;
import nextcp.domainmodel.device.services.ITransport;
import nextcp.dto.TrackInfoDto;
import nextcp.dto.TrackTimeDto;
import nextcp.dto.TransportServiceStateDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.AVTransportService;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetPositionInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.GetPositionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.NextInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.PauseInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.PlayInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SeekInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.SetNextAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions.StopInput;
import nextcp.util.DisplayUtils;

/**
 * This class connects the local transport service to UPnP AV Transport
 */
public class Upnp_AVTransportBridge extends BaseAvTransportChangeEventImpl implements IInfoService, ITransport, ITimeService
{
    private static final Logger log = LoggerFactory.getLogger(Upnp_AVTransportBridge.class.getName());

    private AVTransportService avTransportService = null;
    private MediaRendererDevice device = null;
    private AvTransportState currentAvTransportState = new AvTransportState(); // Init with empty state object 


    public Upnp_AVTransportBridge(AVTransportService upnp_avTransportService, MediaRendererDevice device)
    {
        this.avTransportService = upnp_avTransportService;
        this.device = device;
    }

    public void seek(long secondsAbsolute)
    {
        long hours = secondsAbsolute / 3600;
        long minutes = (secondsAbsolute % 3600) / 60;
        long seconds = secondsAbsolute % 60;

        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        
        log.info("seeking to position {} seconds : {}", secondsAbsolute, time);
        SeekInput inp = new SeekInput();
        inp.InstanceID = 0L;
        inp.Unit = "ABS_TIME";
        inp.Target = time; 
    }
    
    /**
     * Starts playing
     */
    @Override
    public void play()
    {
        PlayInput inp = new PlayInput();
        inp.InstanceID = 0L;
        inp.Speed = "1";
        avTransportService.play(inp);
        device.setAvServicesOffline(false);
    }

    public void play(String uri, String metaData)
    {
        setUrl(uri, metaData);
        play();
    }

    public void playNext(String uri, String metaData)
    {
        setNextUrl(uri, metaData);
    }

    public void setUrl(String currentUri, String metadata)
    {
        SetAVTransportURIInput uri = new SetAVTransportURIInput();
        uri.InstanceID = 0L;
        uri.CurrentURI = currentUri;
        uri.CurrentURIMetaData = metadata;

        if (log.isDebugEnabled())
        {
            log.debug(String.format("Playing '%s' with Metadata: >%s<", currentUri, metadata));
        }
        avTransportService.setAVTransportURI(uri);
    }

    @Override
    public void pause()
    {
        PauseInput pi = new PauseInput();
        pi.InstanceID = 0L;
        avTransportService.pause(pi);
    }

    @Override
    public void stop()
    {
        StopInput inp = new StopInput();
        inp.InstanceID = 0L;
        avTransportService.stop(inp);
    }

    public void setNextUrl(String streamingURL, String trackMetadata)
    {
        SetNextAVTransportURIInput inp = new SetNextAVTransportURIInput();
        inp.InstanceID = 0L;
        inp.NextURI = streamingURL;
        inp.NextURIMetaData = trackMetadata;
        avTransportService.setNextAVTransportURI(inp);
    }

    @Override
    public void next()
    {
        NextInput inp = new NextInput();
        inp.InstanceID = 0L;
        avTransportService.next(inp);
    }

    public TrackTimeDto generateTractTimeDto()
    {
        TrackTimeDto dto = new TrackTimeDto();
    	try {
            dto.mediaRendererUdn = device.getUDN().getIdentifierString();

            GetPositionInfoInput inp = new GetPositionInfoInput();
            inp.InstanceID = 0L;
            GetPositionInfoOutput out = avTransportService.getPositionInfo(inp);

            dto.duration = getAsSeconds(out.TrackDuration);
            dto.seconds = getAsSeconds(out.RelTime);
            dto.trackCount = out.Track;

            dto.durationDisp = DisplayUtils.convertToDigitString(dto.duration);
            if ("00:00".equals(dto.durationDisp))
            {
                dto.streaming = true;
                if (dto.seconds != null && dto.seconds > 0)
                {
                    dto.durationDisp = "streaming";
                }
            }
            else
            {
                dto.streaming = false;
            }
            dto.secondsDisp = DisplayUtils.convertToDigitString(dto.seconds);
            dto.percent = calcPercent(dto.seconds, dto.duration);
    	} catch (Exception e) {
    		log.warn("could not generate TrackTimeDto", e);
    		device.renewServices();
    	}
        return dto;
    }

    private Long getAsSeconds(String trackDuration)
    {
        if ("NOT_IMPLEMENTED".equalsIgnoreCase(trackDuration))
        {
            return 0L;
        }

        try
        {
            int idx = 0;
            long asSeconds = 0;
            String[] values = trackDuration.split(":");
            if (values.length == 3)
            {
                asSeconds = Integer.valueOf(values[0]) * 60 * 60;
                idx++;
            }
            if (!StringUtils.isAllBlank(values[idx])) {
                asSeconds = asSeconds + Integer.valueOf(values[idx]) * 60;
                idx++;
                if (!StringUtils.isAllBlank(values[idx])) {
                	asSeconds = asSeconds + Integer.valueOf(values[idx]);
                }
            }
            return asSeconds;
        }
        catch (Exception e)
        {
            log.warn("{}:error reading duration time", device.getFriendlyName(), e);
            return 0L;
        }
    }

    private int calcPercent(Long seconds, Long duration)
    {
        if (seconds == null || duration == null)
        {
            return 0;
        }

        if (duration == 0)
        {
            return 0;
        }

        return (int) ((seconds * 100) / duration);
    }

    @Override
    public void processingFinished(AvTransportState currentAvTransportState)
    {
        this.currentAvTransportState = currentAvTransportState;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("%s: %s", device.getFriendlyName(), currentAvTransportState.toString()));
            Thread.dumpStack();
        }
    }
    
    @Override
    public TransportServiceStateDto getCurrentTransportServiceState()
    {
        TransportServiceStateDto dto = new TransportServiceStateDto();
        
        dto.udn = device.getUdnAsString();
        dto.canPause = true;
        dto.canRepeat = true;
        dto.canSeek = true;
        dto.canShuffle = true;
        dto.canSkipNext = true;
        dto.canSkipPrevious = false;
        dto.repeat = false;
        dto.shuffle = false;

        dto.transportState = "PLAYING";//currentAvTransportState.TransportState;
        
        dto.udn = device.getUdnAsString();
        
        log.trace(String.format("%s: %s", device.getFriendlyName(), dto.toString()));
        return dto;
    }

    @Override
    public TrackInfoDto getTrackinfo()
    {
        TrackInfoDto dto = new TrackInfoDto();

        dto.mediaRendererUdn = device.getUdnAsString();
        dto.metatext = currentAvTransportState.CurrentTrackMetaData;
        dto.uri = currentAvTransportState.CurrentTrackURI;
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(currentAvTransportState.CurrentTrackMetaData);

        return dto;
    }
    
    @Override
    public void transportStateChange(String value) {
    	currentAvTransportState.TransportState = value;
    	log.debug("{}: received transport state {} ", device.getFriendlyName(), value);
    }
}
