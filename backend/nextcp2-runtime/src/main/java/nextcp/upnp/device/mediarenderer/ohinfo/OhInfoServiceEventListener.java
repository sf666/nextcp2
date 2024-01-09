package nextcp.upnp.device.mediarenderer.ohinfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.TrackInfoDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoServiceStateVariable;
import nextcp.util.DisplayUtils;

public class OhInfoServiceEventListener extends InfoServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(OhInfoServiceEventListener.class.getName());

    private MediaRendererDevice device = null;

    public OhInfoServiceEventListener(MediaRendererDevice device)
    {
        this.device = device;
    }

    @Override
    public void eventProcessed()
    {
        super.eventProcessed();
        TrackInfoDto dto = createTrackInfoDto();
        device.getEventPublisher().publishEvent(dto);
    }

    private TrackInfoDto createTrackInfoDto()
    {
        InfoServiceStateVariable state = getStateVariable();
        TrackInfoDto dto = new TrackInfoDto();

        dto.mediaRendererUdn = device.getUDN().getIdentifierString();

        dto.codecName = state.CodecName;
        dto.detailsCount = state.DetailsCount;
        dto.metadata = state.Metadata;
        dto.trackCount = state.TrackCount;
        dto.uri = state.Uri;
        dto.duration = DisplayUtils.convertToDigitString(state.Duration);
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(state.Metadata);
        dto.bitDepth = state.BitDepth;
        dto.bitrate = state.BitRate;
        dto.lossless = state.Lossless;
        dto.sampleRate = state.SampleRate;
        dto.metatext = state.Metatext;
        dto.metatextCount = state.MetatextCount;

        // Experimental: If we receive Metatext, this is usually send by a radio station in the format "artist - songtitle" (or vice versa)
        // In this case the radio station name is mapped to the album field, artist and songtitle extracted from Metatext.
        if (dto.metatextCount > 0 && state.Metatext != null && !state.Metatext.contains("<DIDL-Lite"))
        {
            log.debug(String.format("Metatext present. Count: %d. Text: %s", dto.metatextCount, dto.metatext));
            String[] splitMeta = state.Metatext.split(" - ");
            if (splitMeta.length == 2)
            {
                dto.currentTrack.album = dto.currentTrack.title;
                dto.currentTrack.artistName = splitMeta[0];
                dto.currentTrack.title = splitMeta[1];
            }
            else
            {
                log.warn("No split chat found in Metatext. Text: " + dto.metatext);
                dto.currentTrack.album = dto.metatext;
            }
        } else {
        	log.debug("metatext not processed.");
        }
        
        if (log.isInfoEnabled()) {
        	log.info(dto.toString());
        }
        
        return dto;
    }

}
