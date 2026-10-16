package nextcp.upnp.device.mediarenderer.ohinfo;

import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.TrackInfoDto;
import nextcp.dto.ItemDto;
import nextcp.upnp.SubscriptionHealth;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoServiceStateVariable;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.CountersOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.DetailsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.MetatextOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.TrackOutput;
import nextcp.util.DisplayUtils;

public class OhInfoServiceEventListener extends InfoServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(OhInfoServiceEventListener.class.getName());

    private MediaRendererDevice device = null;
    private final SubscriptionHealth health = new SubscriptionHealth();

    public OhInfoServiceEventListener(MediaRendererDevice device)
    {
    	super(device.getDevice());
        this.device = device;
    }

    @Override
    public synchronized void eventProcessed()
    {
        super.eventProcessed();
        health.eventReceived();
        TrackInfoDto dto = createTrackInfoDto(getStateVariable());
        device.getEventPublisher().publishEvent(dto);
    }

    public boolean isReceivingEvents()
    {
        return health.isReceivingEvents();
    }

    /** Use the same mapping for action reads without overwriting concurrent GENA updates. */
    public TrackInfoDto fromSnapshot(DetailsOutput details, CountersOutput counters,
        TrackOutput track, MetatextOutput metatext)
    {
        InfoServiceStateVariable state = new InfoServiceStateVariable();
        state.CodecName = details.CodecName;
        state.Duration = details.Duration;
        state.BitDepth = details.BitDepth;
        state.BitRate = details.BitRate;
        state.Lossless = details.Lossless;
        state.SampleRate = details.SampleRate;
        state.DetailsCount = counters.DetailsCount;
        state.TrackCount = counters.TrackCount;
        state.MetatextCount = counters.MetatextCount;
        state.Uri = track.Uri;
        state.Metadata = track.Metadata;
        state.Metatext = metatext.Value;
        return createTrackInfoDto(state);
    }

    private TrackInfoDto createTrackInfoDto(InfoServiceStateVariable state)
    {
        TrackInfoDto dto = new TrackInfoDto();

        dto.mediaRendererUdn = device.getUDN().getIdentifierString();

        dto.codecName = state.CodecName;
        dto.detailsCount = state.DetailsCount;
        dto.metadata = state.Metadata;
        dto.trackCount = state.TrackCount;
        dto.uri = state.Uri;
        dto.duration = DisplayUtils.convertToDigitString(state.Duration);
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(state.Metadata);
        if (dto.currentTrack == null)
        {
            dto.currentTrack = new ItemDto();
        }
        dto.bitDepth = state.BitDepth;
        dto.bitrate = state.BitRate;
        dto.lossless = state.Lossless;
        dto.sampleRate = state.SampleRate;
        dto.metatext = state.Metatext;
        dto.metatextCount = state.MetatextCount;

        // Experimental: If we receive Metatext, this is usually send by a radio station in the format "artist - songtitle" (or vice versa)
        // In this case the radio station name is mapped to the album field, artist and songtitle extracted from Metatext.
        if (dto.metatextCount != null && dto.metatextCount > 0 && state.Metatext != null && !state.Metatext.contains("<DIDL-Lite"))
        {
            log.debug(String.format("[%s] Metatext present. Count: %d. Text: %s",device.getFriendlyName(), dto.metatextCount, dto.metatext));
            String[] splitMeta = state.Metatext.split(" - ");
            if (splitMeta.length == 2)
            {
                dto.currentTrack.album = dto.currentTrack.title;
                dto.currentTrack.artistName = splitMeta[0];
                dto.currentTrack.title = splitMeta[1];
            }
            else
            {
                log.warn(String.format("[%s] No split chat found in Metatext. Text: %s" , device.getFriendlyName(), dto.metatext));
                dto.currentTrack.album = dto.metatext;
            }
        } else {
            log.debug(String.format("[%s] metatext not processed." , device.getFriendlyName()));
        }
        
        if (log.isInfoEnabled()) {
            log.info(String.format("[%s] %s" , device.getFriendlyName(), dto.toString()));
        }
        
        return dto;
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus) {
        health.failed();
    	super.ended(reason, responseStatus);
    }
    
    @Override
    public void failed(UpnpResponse responseStatus) {
        health.failed();
    	super.failed(responseStatus);
    }
}
