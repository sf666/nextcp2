package nextcp.upnp.device.mediarenderer.volume;

import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.VolumeServiceEventListenerImpl;

public class OhVolumeServiceEventListener extends VolumeServiceEventListenerImpl
{
    private IDeviceDriverCallback callback = null;
    private MediaRendererDevice device = null;

    public OhVolumeServiceEventListener(MediaRendererDevice device) {
    	super(device.getDevice());
    	this.device = device;
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
    
    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus) {
    	super.ended(reason, responseStatus);
    	device.setServicesEnded(true);    	
    }
    
    @Override
    public void failed(UpnpResponse responseStatus) {
    	super.failed(responseStatus);
    	device.setServicesEnded(true);    	
    }
}
