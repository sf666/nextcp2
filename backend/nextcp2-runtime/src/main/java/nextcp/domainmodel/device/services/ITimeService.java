package nextcp.domainmodel.device.services;

import nextcp.dto.TrackTimeDto;
import nextcp.upnp.modelGen.avopenhomeorg.time.TimeServiceStateVariable;

public interface ITimeService
{
    public TrackTimeDto generateTractTimeDto();
}
