package nextcp.domainmodel.device.mediarenderer.ohinfo;

import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.domainmodel.device.services.IInfoService;
import nextcp.dto.TrackInfoDto;
import nextcp.upnp.modelGen.avopenhomeorg.info.InfoService;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.CountersOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.DetailsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.MetatextOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.TrackOutput;
import nextcp.util.DisplayUtils;

public class Oh_InfoServiceImpl implements IInfoService
{
    private InfoService infoService = null;
    private MediaRendererDevice device = null;

    public Oh_InfoServiceImpl(MediaRendererDevice device, InfoService infoService)
    {
        this.device = device;
        this.infoService = infoService;
    }

    public DetailsOutput details()
    {
        return infoService.details();
    }

    public CountersOutput counters()
    {
        return infoService.counters();
    }

    public MetatextOutput metatext()
    {
        return infoService.metatext();
    }

    public TrackOutput track()
    {
        return infoService.track();
    }

    private TrackInfoDto createTrackInfoDto()
    {
        TrackInfoDto dto = new TrackInfoDto();

        DetailsOutput details = details();
        dto.mediaRendererUdn = device.getUdnAsString();
        dto.codecName = details.CodecName;

        CountersOutput counter = counters();
        dto.detailsCount = counter.DetailsCount;
        dto.metatextCount = counter.MetatextCount;
        dto.trackCount = counter.TrackCount;

        MetatextOutput meta = metatext();
        dto.metatext = meta.Value;

        TrackOutput track = track();
        dto.metadata = track.Metadata;
        dto.uri = track.Uri;
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(dto.metatext);

        return dto;
    }

    @Override
    public TrackInfoDto getTrackinfo()
    {
        return createTrackInfoDto();
    }
}
