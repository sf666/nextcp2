package nextcp.upnp.device.mediarenderer.renderingControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.UpnpDeviceDriver;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.RenderingControlServiceEventListenerImpl;

public class RenderingControlEventListener extends RenderingControlServiceEventListenerImpl
{
    public RenderingControlEventListener(MediaRendererDevice device) {
		super(device.getDevice());
		// TODO Auto-generated constructor stub
	}

	private static final Logger log = LoggerFactory.getLogger(RenderingControlEventListener.class.getName());

    private IDeviceDriverCallback callback = null;

    public void addDeviceDriverCallback(IDeviceDriverCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void volumeChange(Long value)
    {
        super.volumeChange(value);
        if (callback != null)
        {
            callback.volumeChanged(Math.toIntExact(value));
        }
        log.debug("UPnP device driver volume changed to " + Long.toString(value));
    }

    public void addDeviceDriverCallback(UpnpDeviceDriver upnpDeviceDriver)
    {
        callback = upnpDeviceDriver;
    }
}
