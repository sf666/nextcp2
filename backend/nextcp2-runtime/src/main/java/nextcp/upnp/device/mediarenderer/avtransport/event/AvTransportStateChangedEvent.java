package nextcp.upnp.device.mediarenderer.avtransport.event;

import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.avtransport.AvTransportState;

public class AvTransportStateChangedEvent
{
    public AvTransportState state = null;
    public MediaRendererDevice device = null;
}
