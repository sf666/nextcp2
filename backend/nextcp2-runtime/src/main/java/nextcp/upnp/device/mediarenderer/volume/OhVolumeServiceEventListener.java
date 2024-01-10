package nextcp.upnp.device.mediarenderer.volume;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.VolumeServiceEventListenerImpl;

public class OhVolumeServiceEventListener extends VolumeServiceEventListenerImpl
{
    private IDeviceDriverCallback callback = null;

    public OhVolumeServiceEventListener(MediaRendererDevice device) {
    	super(device.getDevice());
    }

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
    }
}
