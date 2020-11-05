package nextcp.domainmodel.device.mediarenderer.ohinfo;

import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.dto.TrackInfoDto;
import nextcp.dto.TrackTimeDto;
import nextcp.upnp.modelGen.avopenhomeorg.info.InfoServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.info.InfoServiceStateVariable;
import nextcp.upnp.modelGen.avopenhomeorg.time.TimeServiceStateVariable;
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
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(state.Metatext);
        return dto;
    }

}
