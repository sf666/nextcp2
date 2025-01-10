package nextcp.upnp.device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

	private HashMap<UDN, MediaRendererDevice> mediaRendererList = new HashMap<>();
	private HashMap<UDN, MediaServerDevice> mediaServerList = new HashMap<>();
	private HashMap<UDN, IMediaServerExtendedSupport> mediaServerExtList = new HashMap<>();
	private HashMap<UDN, MediaServerDevice> inactiveMediaServerList = new HashMap<>();

	public HashMap<UDN, MediaServerDevice> getInactiveMediaServerList() {
		return inactiveMediaServerList;
	}

	@Autowired
	public DeviceFactory deviceFactory = null;

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
	public synchronized void addMediaRendererDevice(RemoteDevice remoteDevice) {
		MediaRendererDevice device = deviceFactory.mediaRendererDeviceFactory(remoteDevice,
			rendererConfigService.isMediaRendererActive(remoteFacade.getUdnAsString(remoteDevice)));
		rendererConfigService.addMediaRendererDeviceConfig(device);
		MediaRendererDevice oldDevice = mediaRendererList.put(remoteFacade.getUDN(remoteDevice), device);
		if (oldDevice != null) {
			log.info("removed old media renderer device : {} ", oldDevice.getAsDto());
		}
		eventPublisher.publishEvent(new MediaRendererListChanged(getAvailableMediaRenderer()));
	}

	public synchronized void removeMediaRendererDevice(RemoteDevice remoteDevice) {
		log.info("device removed : {}", remoteFacade.getFriendlyName(remoteDevice));
		MediaRendererDevice device = mediaRendererList.get(remoteFacade.getUDN(remoteDevice));
		if (device != null) {
			device.setServicesOffline(true);
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
	public synchronized void addMediaServerDevice(RemoteDevice remoteDevice) {
		MediaServerDevice device = deviceFactory.mediaServerDeviceFactory(remoteDevice);
		serverConfigService.addMediaServerDeviceConfig(remoteDevice, device);
		MediaServerDevice oldDevice = mediaServerList.put(remoteFacade.getUDN(remoteDevice), device);
		inactiveMediaServerList.remove(remoteDevice.getIdentity().getUdn());
		if (oldDevice != null) {
			log.info("removed old media server device : {} ", oldDevice.getAsDto());
		}
		eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
	}

	public synchronized void removeMediaServerDevice(RemoteDevice remoteDevice) {
		MediaServerDevice device = mediaServerList.remove(remoteFacade.getUDN(remoteDevice));
		if (device != null) {
			inactiveMediaServerList.put(device.getUDN(), device);
			eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
		} else {
			log.debug("inknown device ... ");
		}
	}

	public synchronized void updatedMediaRendererDevice(RemoteDevice remoteDevice) {
		MediaRendererDevice device = mediaRendererList.get(remoteFacade.getUDN(remoteDevice));
		if (device != null) {
			device.setServicesEnded(true);
		} else {
			log.info("Updated renderer device unknown yet. Adding ... ");
			addMediaRendererDevice(remoteDevice);
		}
	}

	public synchronized void updatedMediaServerDevice(RemoteDevice remoteDevice) {
		MediaServerDevice device = mediaServerList.get(remoteFacade.getUDN(remoteDevice));
		if (device == null) {
			log.info("Updated server device unknown yet. Adding ... ");
			addMediaServerDevice(remoteDevice);
		}
	}

	public Collection<MediaServerDevice> getAvailableMediaServer() {
		return Collections.unmodifiableCollection(mediaServerList.values());
	}

	//
	// Media Server Extended Support Devices
	//
	public synchronized void addMediaServerExtDevice(IMediaServerExtendedSupport device) {
		mediaServerExtList.put(device.getUdn(), device);
	}

	public void removeMediaServerExtDevice(RemoteDevice device) {
		mediaServerExtList.remove(device.getIdentity().getUdn());
	}

	public Collection<IMediaServerExtendedSupport> getAvailableMediaServerExt() {
		return Collections.unmodifiableCollection(mediaServerExtList.values());
	}
}
