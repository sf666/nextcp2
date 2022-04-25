package nextcp.upnp.device.mediarenderer.volume;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.VolumeServiceEventListenerImpl;

public class OhVolumeServiceEventListener extends VolumeServiceEventListenerImpl
{
    private IDeviceDriverCallback callback = null;

    public OhVolumeServiceEventListener()
    {
    }

    public void addDeviceDriverCallback(IDeviceDriverCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void volumeChange(Long value)
    {
        super.volumeChange(value);
        callback.volumeChanged(Math.toIntExact(value));
    }
}
