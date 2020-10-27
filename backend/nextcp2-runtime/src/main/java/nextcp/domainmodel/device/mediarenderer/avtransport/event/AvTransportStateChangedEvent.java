package nextcp.domainmodel.device.mediarenderer.avtransport.event;

import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.domainmodel.device.mediarenderer.avtransport.AvTransportState;

public class AvTransportStateChangedEvent
{
    public AvTransportState state = null;
    public MediaRendererDevice device = null;
}
