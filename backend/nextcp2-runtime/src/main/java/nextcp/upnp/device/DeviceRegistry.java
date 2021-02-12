package nextcp.upnp.device;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.UDN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import nextcp.config.ConfigService;
import nextcp.config.RendererConfig;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.MediaRendererListChanged;
import nextcp.upnp.device.mediaserver.IMediaServerExtendedSupport;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.device.mediaserver.MediaServerListChanged;

@Component
public class DeviceRegistry
{
    private HashMap<UDN, MediaRendererDevice> mediaRendererList = new HashMap<>();
    private HashMap<UDN, MediaServerDevice> mediaServerList = new HashMap<>();
    private HashMap<UDN, IMediaServerExtendedSupport> mediaServerExtList = new HashMap<>();

    @Autowired
    public DeviceFactory deviceFactory = null;

    @Autowired
    private RendererConfig configService = null;

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
    public void addMediaRendererDevice(RemoteDevice remoteDevice)
    {
        MediaRendererDevice device = deviceFactory.mediaRendererDeviceFactory(remoteDevice);
        mediaRendererList.put(remoteDevice.getIdentity().getUdn(), device);
        configService.addMediaRendererDeviceConfig(remoteDevice);
        eventPublisher.publishEvent(new MediaRendererListChanged(getAvailableMediaRenderer()));
    }

    public void removeMediaRendererDevice(RemoteDevice remoteDevice)
    {
        MediaRendererDevice mr_device = mediaRendererList.remove(remoteDevice.getIdentity().getUdn());
        eventPublisher.publishEvent(new MediaRendererListChanged(getAvailableMediaRenderer()));
    }

    public Collection<MediaRendererDevice> getActiveMediaRenderer()
    {
        return Collections
                .unmodifiableCollection(mediaRendererList.values().stream().filter(r -> configService.isMediaRendererActive(r.getUdnAsString())).collect(Collectors.toList()));
    }

    public Collection<MediaRendererDevice> getAvailableMediaRenderer()
    {
        return Collections.unmodifiableCollection(mediaRendererList.values());
    }

    //
    // Media Server
    //
    public void addMediaServerDevice(RemoteDevice remoteDevice, MediaServerType default1)
    {
        MediaServerDevice device = deviceFactory.mediaServerDeviceFactory(remoteDevice, default1);
        mediaServerList.put(remoteDevice.getIdentity().getUdn(), device);
        eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
    }

    public void removeMediaServerDevice(RemoteDevice remoteDevice)
    {
        MediaServerDevice device = mediaServerList.remove(remoteDevice.getIdentity().getUdn());
        eventPublisher.publishEvent(new MediaServerListChanged(getAvailableMediaServer()));
    }

    public Collection<MediaServerDevice> getAvailableMediaServer()
    {
        return Collections.unmodifiableCollection(mediaServerList.values());
    }

    //
    // Media Server Extended Support Devices
    //
    public void addMediaServerExtDevice(IMediaServerExtendedSupport device)
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
