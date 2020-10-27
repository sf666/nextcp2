package nextcp.domainmodel.device.mediaserver;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.UDN;

import nextcp.domainmodel.device.BaseDevice;

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
