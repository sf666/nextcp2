package nextcp.upnp.device.mediarenderer.ohinfo;

import nextcp.dto.TrackInfoDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoServiceStateVariable;
import nextcp.util.DisplayUtils;

public class OhInfoServiceEventListener extends InfoServiceEventListenerImpl
{
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
        dto.metatext = state.Metatext;
        dto.metatextCount = state.MetatextCount;
        dto.trackCount = state.TrackCount;
        dto.uri = state.Uri;
        dto.duration = DisplayUtils.convertToDigitString(state.Duration);
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(state.Metadata);
        return dto;
    }

}
