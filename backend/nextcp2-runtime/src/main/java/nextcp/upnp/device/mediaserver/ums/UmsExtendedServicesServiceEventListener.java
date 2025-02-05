package nextcp.upnp.device.mediaserver.ums;

import org.jupnp.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.upnp.device.mediaserver.UmsServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.UmsExtendedServicesServiceEventListenerImpl;


public class UmsExtendedServicesServiceEventListener extends UmsExtendedServicesServiceEventListenerImpl {

	private static final Logger log = LoggerFactory.getLogger(UmsExtendedServicesServiceEventListener.class.getName());
	
	private UmsServerDevice umsServerDevice = null;
	
	public UmsExtendedServicesServiceEventListener(RemoteDevice device, UmsServerDevice umsServerDevice) {
		super(device);
		this.umsServerDevice = umsServerDevice;
	}
	
	@Override
	public void eventProcessed() {
		umsServerDevice.newUmsConfig();
	}
}
