package nextcp.upnp.device.mediarenderer.ohinfo;

import nextcp.domainmodel.device.services.IInfoService;
import nextcp.dto.TrackInfoDto;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoService;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.CountersOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.DetailsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.MetatextOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.TrackOutput;

public class Oh_InfoServiceImpl implements IInfoService
{
    private InfoService infoService = null;
    private final OhInfoServiceEventListener listener;

    public Oh_InfoServiceImpl(InfoService infoService, OhInfoServiceEventListener listener)
    {
        this.infoService = infoService;
        this.listener = listener;
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
        DetailsOutput details = details();
        CountersOutput counter = counters();
        MetatextOutput meta = metatext();
        TrackOutput track = track();
        return listener.fromSnapshot(details, counter, track, meta);
    }

    @Override
    public TrackInfoDto getTrackinfo()
    {
        return createTrackInfoDto();
    }
}
