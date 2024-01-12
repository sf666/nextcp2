package nextcp.service.upnp;

import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.types.UDN;

public class RemoteDeviceFacade {

	public RemoteDeviceFacade() {
	}

    public UDN getUDN(RemoteDevice device)
    {
        return device.getIdentity().getUdn();
    }

    public String getUdnAsString(RemoteDevice device)
    {
        return device.getIdentity().getUdn().getIdentifierString();
    }

    public String getFriendlyName(RemoteDevice device)
    {
        return device.getDetails().getFriendlyName();
    }
	
}
