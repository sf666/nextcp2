package nextcp.upnp.device.mediaserver;

import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.types.UDN;

import nextcp.upnp.device.BaseDevice;

public class JMinimDevice extends BaseDevice implements IMediaServerExtendedSupport
{
    public JMinimDevice(RemoteDevice device)
    {
        super(device);
    }

    @Override
    public UDN getUdn()
    {
        return getDevice().getIdentity().getUdn();
    }
    
}
