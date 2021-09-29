package nextcp.upnp.device.mediarenderer.avtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.services.IInfoService;
import nextcp.domainmodel.device.services.ITimeService;
import nextcp.domainmodel.device.services.IUpnpAvTransport;
import nextcp.dto.TrackInfoDto;
import nextcp.dto.TrackTimeDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.AVTransportService;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetPositionInfoInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.GetPositionInfoOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.NextInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.PauseInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.PlayInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetNextAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.StopInput;
import nextcp.util.DisplayUtils;

public class Upnp_AVTransportBridge implements IInfoService, IUpnpAvTransport, ITimeService
{
    private static final Logger log = LoggerFactory.getLogger(Upnp_AVTransportBridge.class.getName());

    private AVTransportService avTransportService = null;
    private MediaRendererDevice device = null;

    public Upnp_AVTransportBridge(AVTransportService upnp_avTransportService, MediaRendererDevice device)
    {
        this.avTransportService = upnp_avTransportService;
        this.device = device;
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
    }

    @Override
    public void play(String uri, String metaData)
    {
        setUrl(uri, metaData);
        play();
    }

    @Override
    public void playNext(String uri, String metaData)
    {
        setNextUrl(uri, metaData);
    }

    @Override
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

    @Override
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
            asSeconds = asSeconds + Integer.valueOf(values[idx]) * 60;
            idx++;
            asSeconds = asSeconds + Integer.valueOf(values[idx]);
            return asSeconds;
        }
        catch (Exception e)
        {
            log.warn("error reading duration time", e);
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
    public TrackInfoDto getTrackinfo()
    {
        log.warn("NOT IMPLEMENTED YET ...");

        TrackInfoDto dto = new TrackInfoDto();

        // TODO: Implement
        //
        // DetailsOutput details = details();
        // dto.lossless = details.Lossless;
        // dto.bitDepth = details.BitDepth;
        // dto.bitRate = details.BitRate;
        // dto.sampleRate = details.SampleRate;
        // dto.codecName = details.CodecName;
        // dto.duration = details.Duration;
        // dto.durationDisp = DisplayUtils.convertToDigitString(details.Duration);
        //
        // CountersOutput counter = counters();
        // dto.detailsCount = counter.DetailsCount;
        // dto.metatextCount = counter.MetatextCount;
        // dto.trackCount = counter.TrackCount;
        //
        // MetatextOutput meta = metatext();
        // dto.metatext = meta.Value;
        //
        // TrackOutput track = track();
        // dto.metadata = track.Metadata;
        // dto.uri = track.Uri;

        return dto;
    }
}
