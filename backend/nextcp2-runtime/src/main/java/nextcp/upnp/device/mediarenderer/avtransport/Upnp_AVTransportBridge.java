package nextcp.upnp.device.mediarenderer.avtransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.services.IInfoService;
import nextcp.domainmodel.device.services.IUpnpAvTransport;
import nextcp.dto.TrackInfoDto;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.AVTransportService;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.NextInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.PauseInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.PlayInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.SetNextAVTransportURIInput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions.StopInput;

public class Upnp_AVTransportBridge implements IInfoService, IUpnpAvTransport
{
    private static final Logger log = LoggerFactory.getLogger(Upnp_AVTransportBridge.class.getName());

    private AVTransportService avTransportService = null;

    public Upnp_AVTransportBridge(AVTransportService upnp_avTransportService)
    {
        this.avTransportService = upnp_avTransportService;
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

    @Override
    public TrackInfoDto getTrackinfo()
    {
        log.warn("NOT IMPLEMENTED YET ...");
        
        TrackInfoDto dto = new TrackInfoDto();

        // TODO: Implement
//        
//        DetailsOutput details = details();
//        dto.lossless = details.Lossless;
//        dto.bitDepth = details.BitDepth;
//        dto.bitRate = details.BitRate;
//        dto.sampleRate = details.SampleRate;
//        dto.codecName = details.CodecName;
//        dto.duration = details.Duration;
//        dto.durationDisp = DisplayUtils.convertToDigitString(details.Duration);
//
//        CountersOutput counter = counters();
//        dto.detailsCount = counter.DetailsCount;
//        dto.metatextCount = counter.MetatextCount;
//        dto.trackCount = counter.TrackCount;
//
//        MetatextOutput meta = metatext();
//        dto.metatext = meta.Value;
//
//        TrackOutput track = track();
//        dto.metadata = track.Metadata;
//        dto.uri = track.Uri;

        return dto;
    }
}
