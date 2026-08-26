package nextcp.upnp.device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import nextcp.config.RendererConfig;
import nextcp.config.ServerConfig;
import nextcp.service.upnp.RemoteDeviceFacade;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.MediaRendererListChanged;
import nextcp.upnp.device.mediaserver.IMediaServerExtendedSupport;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.device.mediaserver.MediaServerListChanged;

@Component
public class DeviceRegistry {

	private static final Logger log = LoggerFactory.getLogger(DeviceRegistry.class.getName());

	private RemoteDeviceFacade remoteFacade = new RemoteDeviceFacade();

	private Map<UDN, MediaRendererDevice> mediaRendererList = new ConcurrentHashMap<>();
	private Map<UDN, MediaServerDevice> mediaServerList = new ConcurrentHashMap<>();
	private Map<UDN, IMediaServerExtendedSupport> mediaServerExtList = new ConcurrentHashMap<>();
	private Map<UDN, MediaServerDevice> inactiveMediaServerList = new ConcurrentHashMap<>();
	private Set<UDN> initializingDevices = ConcurrentHashMap.newKeySet();

	public Map<UDN, MediaServerDevice> getInactiveMediaServerList() {
		return inactiveMediaServerList;
	}

	@Autowired
	private DeviceFactory deviceFactory = null;

	@Autowired
	private RendererConfig rendererConfigService = null;

	@Autowired
	private ServerConfig serverConfigService = null;

	@Autowired
	private ApplicationEventPublisher eventPublisher = null;

	public MediaRendererDevice getMediaRendererByUDN(UDN udn) {
		return mediaRendererList.get(udn);
	}

	public MediaServerDevice getMediaServerByUDN(UDN udn) {
		return mediaServerList.get(udn);
	}

	//
	// Media Renderer
	//
	public void addMediaRendererDevice(RemoteDevice remoteDevice) {
		UDN udn = remoteFacade.getUDN(remoteDevice);
		if (!initializingDevices.add(udn)) {
			log.debug("media renderer device is already being initialized : {}", remoteFacade.getFriendlyName(remoteDevice));
			return;
		}
		try {
			MediaRendererDevice device = deviceFactory.mediaRendererDeviceFactory(remoteDevice,
				rendererConfigService.isMediaRendererActive(remoteFacade.getUdnAsString(remoteDevice)));
			rendererConfigService.addMediaRendererDeviceConfig(device);
			MediaRendererDevice oldDevice = mediaRendererList.put(udn, device);
			if (oldDevice != null) {
				log.debug("replaced old media renderer device : {} ", oldDevice.getAsDto());
				oldDevice.removed();
			}
			eventPublisher.publishEvent(new MediaRendererListChanged(getAvailableMediaRenderer()));
		} finally {
			initializingDevices.remove(udn);
		}
	}

	public void removeMediaRendererDevice(RemoteDevice remoteDevice) {
		log.info("device removed : {}", remoteFacade.getFriendlyName(remoteDevice));
		MediaRendererDevice device = mediaRendererList.remove(remoteFacade.getUDN(remoteDevice));
		if (device != null) {
			device.removed();
			eventPublisher.publishEvent(new MediaRendererListChanged(getAvailableMediaRenderer()));
		} else {
			log.debug("device not found in registry {}", remoteFacade.getFriendlyName(remoteDevice));
		}
	}

	public Collection<MediaRendererDevice> getActiveMediaRenderer() {
		return Collections.unmodifiableCollection(mediaRendererList.values().stream()
			.filter(r -> rendererConfigService.isMediaRendererActive(r.getUdnAsString())).collect(Collectors.toList()));
	}

	public Collection<MediaRendererDevice> getAvailableMediaRenderer() {
		Collection<MediaRendererDevice> allDevices = new ArrayList<>();
		allDevices.addAll(mediaRendererList.values());
		// allDevices.addAll(inactiveMediaRendererList.values());
		return Collections.unmodifiableCollection(allDevices);
	}

	//
	// Media Server
	//
	public void addMediaServerDevice(RemoteDevice remoteDevice) {
		UDN udn = remoteFacade.getUDN(remoteDevice);
		if (!initializingDevices.add(udn)) {
			log.debug("media server device is already being initialized : {}", remoteFacade.getFriendlyName(remoteDevice));
			return;
		}
		try {
			MediaServerDevice device = deviceFactory.mediaServerDeviceFactory(remoteDevice);
			serverConfigService.addMediaServerDeviceConfig(remoteDevice, device);
			MediaServerDevice oldDevice = mediaServerList.put(udn, device);
			inactiveMediaServerList.remove(remoteDevice.getIdentity().getUdn());
			if (oldDevice != null) {
				log.debug("replaced old media server device : {} ", oldDevice.getAsDto());
			}
			eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
		} finally {
			initializingDevices.remove(udn);
		}
	}

	public void removeMediaServerDevice(RemoteDevice remoteDevice) {
		MediaServerDevice device = mediaServerList.remove(remoteFacade.getUDN(remoteDevice));
		if (device != null) {
			inactiveMediaServerList.put(device.getUDN(), device);
			eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
		} else {
			log.debug("unknown device ... ");
		}
	}

	public void updatedMediaRendererDevice(RemoteDevice remoteDevice) {
		if (!mediaRendererList.containsKey(remoteFacade.getUDN(remoteDevice))) {
			log.info("Updated renderer device unknown yet. Adding ... ");
			addMediaRendererDevice(remoteDevice);
		}
	}

	public void updatedMediaServerDevice(RemoteDevice remoteDevice) {
		MediaServerDevice device = mediaServerList.get(remoteFacade.getUDN(remoteDevice));
		if (device == null) {
			log.debug("Updated server device unknown yet. Adding ... ");
			addMediaServerDevice(remoteDevice);
		}
	}

	public Collection<MediaServerDevice> getAvailableMediaServer() {
		return Collections.unmodifiableCollection(mediaServerList.values());
	}

	/**
	 * Keeps the content directory subscriptions alive.
	 *
	 * A subscription is created once, while the device is being added, and the periodic search does not
	 * help: a device that is already known lands in updatedMediaServerDevice, which only adds when it is
	 * unknown. So a subscription that never came up stayed down for the rest of the run.
	 */
	@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
	public void ensureMediaServerSubscriptions() {
		for (MediaServerDevice device : mediaServerList.values()) {
			try {
				device.ensureContentDirectorySubscription();
			} catch (Exception e) {
				log.warn("could not subscribe to the content directory of {}", device.getFriendlyName(), e);
			}
		}
	}

	//
	// Media Server Extended Support Devices
	//
	public void addMediaServerExtDevice(IMediaServerExtendedSupport device) {
		mediaServerExtList.put(device.getUdn(), device);
	}

	public void removeMediaServerExtDevice(RemoteDevice device) {
		mediaServerExtList.remove(device.getIdentity().getUdn());
	}

	public Collection<IMediaServerExtendedSupport> getAvailableMediaServerExt() {
		return Collections.unmodifiableCollection(mediaServerExtList.values());
	}
}
