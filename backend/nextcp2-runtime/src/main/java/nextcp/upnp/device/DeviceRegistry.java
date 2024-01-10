package nextcp.upnp.device;

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
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.MediaRendererListChanged;
import nextcp.upnp.device.mediaserver.IMediaServerExtendedSupport;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.device.mediaserver.MediaServerListChanged;

@Component
public class DeviceRegistry
{
    private static final Logger log = LoggerFactory.getLogger(DeviceRegistry.class.getName());

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

    public MediaRendererDevice getMediaRendererByUDN(UDN udn)
    {
        return mediaRendererList.get(udn);
    }

    public MediaServerDevice getMediaServerByUDN(UDN udn)
    {
        return mediaServerList.get(udn);
    }

    //
    // Media Renderer
    //
    public synchronized void addMediaRendererDevice(RemoteDevice remoteDevice)
    {
        MediaRendererDevice device = deviceFactory.mediaRendererDeviceFactory(remoteDevice);
        rendererConfigService.addMediaRendererDeviceConfig(device);        
        MediaRendererDevice oldDevice = mediaRendererList.put(remoteDevice.getIdentity().getUdn(), device);
        if (oldDevice != null)
        {
            log.info("removed old media renderer device : {} ", oldDevice.getAsDto());
        }
        eventPublisher.publishEvent(new MediaRendererListChanged(getAvailableMediaRenderer()));
    }

    public synchronized void removeMediaRendererDevice(RemoteDevice remoteDevice)
    {
        MediaRendererDevice mr_device = mediaRendererList.remove(remoteDevice.getIdentity().getUdn());
        eventPublisher.publishEvent(new MediaRendererListChanged(getAvailableMediaRenderer()));
    }

    public Collection<MediaRendererDevice> getActiveMediaRenderer()
    {
        return Collections.unmodifiableCollection(
                mediaRendererList.values().stream().filter(r -> rendererConfigService.isMediaRendererActive(r.getUdnAsString())).collect(Collectors.toList()));
    }

    public Collection<MediaRendererDevice> getAvailableMediaRenderer()
    {
        return Collections.unmodifiableCollection(mediaRendererList.values());
    }

    //
    // Media Server
    //
    public synchronized void addMediaServerDevice(RemoteDevice remoteDevice, MediaServerType serverType)
    {
        MediaServerDevice device = deviceFactory.mediaServerDeviceFactory(remoteDevice, serverType);
        serverConfigService.addMediaServerDeviceConfig(remoteDevice, device);
        MediaServerDevice oldDevice =  mediaServerList.put(remoteDevice.getIdentity().getUdn(), device);
        inactiveMediaServerList.remove(remoteDevice.getIdentity().getUdn());
        if (oldDevice != null)
        {
            log.info("removed old media server device : {} ", oldDevice.getAsDto());
        }
        eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
    }

    public synchronized void removeMediaServerDevice(RemoteDevice remoteDevice)
    {
        MediaServerDevice device = mediaServerList.remove(remoteDevice.getIdentity().getUdn());
        inactiveMediaServerList.put(device.getUDN(), device);
        eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
    }

    public synchronized void updatedMediaRendererDevice(RemoteDevice remoteDevice)
    {
        MediaRendererDevice device = mediaRendererList.get(remoteDevice.getIdentity().getUdn());
        if (device != null) {
        	log.debug("DeviceRegistry. Renderer updated : " + device.getFriendlyName());
            device.deviceUpdated();
        }
    }
    
    public Collection<MediaServerDevice> getAvailableMediaServer()
    {
        return Collections.unmodifiableCollection(mediaServerList.values());
    }

    //
    // Media Server Extended Support Devices
    //
    public synchronized void addMediaServerExtDevice(IMediaServerExtendedSupport device)
    {
        mediaServerExtList.put(device.getUdn(), device);
    }

    public void removeMediaServerExtDevice(RemoteDevice device)
    {
        mediaServerExtList.remove(device.getIdentity().getUdn());
    }

    public Collection<IMediaServerExtendedSupport> getAvailableMediaServerExt()
    {
        return Collections.unmodifiableCollection(mediaServerExtList.values());
    }
}
