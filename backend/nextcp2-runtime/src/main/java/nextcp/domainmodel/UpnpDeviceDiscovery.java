package nextcp.domainmodel;

import javax.annotation.PostConstruct;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import nextcp.domainmodel.device.DeviceFactory;
import nextcp.domainmodel.device.DeviceRegistry;

/**
 * Control Point: Device Discovery.
 */
@Component
public class UpnpDeviceDiscovery implements RegistryListener
{
    public final static String MEDIA_SERVER_TYPE = "MediaServer";
    public final static String MEDIA_RENDERE_TYPE = "MediaRenderer";
    public final static String JMINIM_MONITOR_TYPE = "Monitor";

    private static final Logger log = LoggerFactory.getLogger(UpnpDeviceDiscovery.class.getName());

    @Autowired
    private UpnpService upnpService = null;

    @Autowired
    private DeviceRegistry deviceRegistry = null;
    
    @Autowired
    private DeviceFactory deviceFactory = null;

    @PostConstruct
    private void init()
    {
        upnpService.getRegistry().addListener(this);

        // Broadcast a search message for all devices
        upnpService.getControlPoint().search(new STAllHeader());
    }

    @EventListener
    public void onApplicationStartedEvent(ContextRefreshedEvent event)
    {
        log.info("Starting device discovery service ... ");
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device)
    {
        log.info(String.format("remoteDeviceAdded of type '%s'. Device [%s] ", device.getType(), device.toString()));
        if (device.getType().getType().equals(MEDIA_SERVER_TYPE) && device.getType().getNamespace().equalsIgnoreCase("schemas-upnp-org"))
        {
            deviceRegistry.addMediaServerDevice(device);
        }
        else if (device.getType().getType().equals(MEDIA_RENDERE_TYPE) && device.getType().getNamespace().equalsIgnoreCase("schemas-upnp-org"))
        {
            deviceRegistry.addMediaRendererDevice(device);
        }
        else if (device.getType().getType().equals(JMINIM_MONITOR_TYPE) && device.getType().getNamespace().equalsIgnoreCase("jminim-org"))
        {
            deviceRegistry.addMediaServerExtDevice(deviceFactory.mediaServerJMinim(device));
        }
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device)
    {
        log.info(String.format("remoteDeviceDiscoveryStarted"));
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex)
    {
        log.info(String.format("remoteDeviceDiscoveryFailed"));
    }

    @Override
    public void remoteDeviceUpdated(Registry registry, RemoteDevice device)
    {
        log.debug(String.format("remoteDeviceUpdated : %s ", device.toString()));
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device)
    {
        log.info(String.format(String.format("remoteDeviceRemoved : %s", device.toString())));

        if (device.getType().getType().equals(MEDIA_SERVER_TYPE) && device.getType().getNamespace().equalsIgnoreCase("schemas-upnp-org"))
        {
            deviceRegistry.removeMediaServerDevice(device);
        }
        else if (device.getType().getType().equals(MEDIA_RENDERE_TYPE) && device.getType().getNamespace().equalsIgnoreCase("schemas-upnp-org"))
        {
            deviceRegistry.removeMediaRendererDevice(device);
        }
        else if (device.getType().getType().equals(JMINIM_MONITOR_TYPE) && device.getType().getNamespace().equalsIgnoreCase("jminim-org"))
        {
            // TODO
        }
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device)
    {
        log.info(String.format("localDeviceAdded"));
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device)
    {
        log.info(String.format("localDeviceRemoved"));
    }

    @Override
    public void beforeShutdown(Registry registry)
    {
        log.info(String.format("beforeShutdown"));
    }

    @Override
    public void afterShutdown()
    {
        log.info(String.format("afterShutdown"));
    }
}
