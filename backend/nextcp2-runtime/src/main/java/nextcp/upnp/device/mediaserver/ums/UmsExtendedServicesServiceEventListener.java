package nextcp.upnp.device.mediaserver.ums;

import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.upnp.device.mediaserver.UmsServerDevice;
import nextcp.upnp.SubscriptionHealth;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.UmsExtendedServicesServiceEventListenerImpl;


public class UmsExtendedServicesServiceEventListener extends UmsExtendedServicesServiceEventListenerImpl {

	private static final Logger log = LoggerFactory.getLogger(UmsExtendedServicesServiceEventListener.class.getName());
	
	private UmsServerDevice umsServerDevice = null;
	private final SubscriptionHealth health = new SubscriptionHealth();
	
	public UmsExtendedServicesServiceEventListener(RemoteDevice device, UmsServerDevice umsServerDevice) {
		super(device);
		this.umsServerDevice = umsServerDevice;
	}
	
	@Override
	public void eventProcessed() {
		health.eventReceived();
		umsServerDevice.newUmsConfig();
	}

	public boolean isReceivingEvents() {
		return health.isReceivingEvents();
	}

	@Override
	public void failed(UpnpResponse responseStatus) {
		health.failed();
		super.failed(responseStatus);
	}

	@Override
	public void ended(CancelReason reason, UpnpResponse responseStatus) {
		health.failed();
		super.ended(reason, responseStatus);
	}

	@Override
	public void webStreamNowPlayingChange(String value) {
		super.webStreamNowPlayingChange(value);
		umsServerDevice.webStreamNowPlaying(value);
	}
}
