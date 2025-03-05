package nextcp.upnp.device;

import jakarta.annotation.PostConstruct;

import org.jupnp.UpnpService;
import org.jupnp.model.message.header.STAllHeader;
import org.jupnp.model.meta.LocalDevice;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.registry.Registry;
import org.jupnp.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Control Point: Device Discovery.
 */
@Component
public class UpnpDeviceDiscovery implements RegistryListener {

	public final static String MEDIA_SERVER_TYPE = "MediaServer";
	public final static String MEDIA_RENDERE_TYPE = "MediaRenderer";
	public final static String MEDIA_RENDERE_OPENHOME = "Source";
	public final static String JMINIM_MONITOR_TYPE = "Monitor";

	private static final Logger log = LoggerFactory.getLogger(UpnpDeviceDiscovery.class.getName());

	@Autowired
	private UpnpService upnpService = null;

	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	private DeviceFactory deviceFactory = null;

	@PostConstruct
	private void init() {
		upnpService.getRegistry().addListener(this);

		// Broadcast a search message for all devices
		upnpService.getControlPoint().search(new STAllHeader());
	}

	@EventListener
	public void onApplicationStartedEvent(ContextRefreshedEvent event) {
		log.info("Starting device discovery service ... ");
	}

	@Override
	public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
		log.debug("remoteDeviceAdded of type {}. Device {} ", device.getType().toString(), device.getDetails().getFriendlyName());
		if (device.getType().getType().equals(MEDIA_SERVER_TYPE)) {
			deviceRegistry.addMediaServerDevice(device);
		} else if (isMediaRenderer(device)) {
			deviceRegistry.addMediaRendererDevice(device);
		} else if (device.getType().getType().equals(JMINIM_MONITOR_TYPE) &&
			device.getType().getNamespace().equalsIgnoreCase("jminim-org")) {
			deviceRegistry.addMediaServerExtDevice(deviceFactory.mediaServerJMinim(device));
		}
	}

	@Override
	public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
		log.info("remoteDeviceDiscoveryStarted : {} ",
			device.getDetails() != null ? device.getDetails().getFriendlyName() : device.toString());
	}

	@Override
	public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
		log.info("remoteDeviceDiscoveryFailed : {}",
			device.getDetails() != null ? device.getDetails().getFriendlyName() : device.toString());
	}

	@Override
	public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
		log.debug("remote media renderer device updated : {} ", device.getDetails().getFriendlyName());

		if (isMediaRenderer(device)) {
			deviceRegistry.updatedMediaRendererDevice(device);
		} else if (device.getType().getType().equals(MEDIA_SERVER_TYPE)) {
			deviceRegistry.updatedMediaServerDevice(device);
		} else {
			log.info("ignoring device : " + device.getDetails() != null ? device.getDetails().getFriendlyName() : device.toString());
		}
	}

	@Override
	public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
		log.debug("remoteDeviceRemoved : {}", device.getDetails().getFriendlyName());

		if (device.getType().getType().equals(MEDIA_SERVER_TYPE)) {
			deviceRegistry.removeMediaServerDevice(device);
		} else if (isMediaRenderer(device)) {
			deviceRegistry.removeMediaRendererDevice(device);
		} else if (device.getType().getType().equals(JMINIM_MONITOR_TYPE) &&
			device.getType().getNamespace().equalsIgnoreCase("jminim-org")) {
			deviceRegistry.removeMediaServerExtDevice(device);
		}
	}

	private boolean isMediaRenderer(RemoteDevice device) {
		if (device.getType().getType().equals(MEDIA_RENDERE_TYPE)) {
			log.debug("identified upnp renderer");
			return true;
		} else {
			for (RemoteService service : device.getServices()) {
				if ("av-openhome-org".equals(service.getServiceType().getNamespace())) {
					log.debug("identified open home renderer");
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void localDeviceAdded(Registry registry, LocalDevice device) {
		log.info("localDeviceAdded : {} ", device.getDetails() != null ? device.getDetails().getFriendlyName() : device.toString());
	}

	@Override
	public void localDeviceRemoved(Registry registry, LocalDevice device) {
		log.info("localDeviceRemoved : {} ", device.getDetails() != null ? device.getDetails().getFriendlyName() : device.toString());
	}

	@Override
	public void beforeShutdown(Registry registry) {
		log.info("beforeShutdown");
	}

	@Override
	public void afterShutdown() {
		log.info("afterShutdown");
	}
}
