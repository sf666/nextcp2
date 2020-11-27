package nextcp.config;

import java.util.List;
import java.util.Optional;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import nextcp.dto.Config;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.dto.UiClientConfig;
import nextcp.eventBridge.SsePublisher;
import nextcp.upnp.device.DeviceRegistry;

@Controller
public class ConfigService
{
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class.getName());

    public static String CLIENT_CONFIG_QUEUENAME = "CONFIG_CHANGED";

    @Autowired
    private Config config = null;

    @Autowired
    private ConfigPersistence persistence = null;

    @Autowired
    private SsePublisher ssePublisher = null;

    @Autowired
    private DeviceRegistry deviceRegistry = null;

    public void addClientProfile(UiClientConfig clientConfig)
    {
        try
        {
            if (config.clientConfig != null && config.clientConfig.size() > 0)
            {
                config.clientConfig.removeIf(e -> e.uuid.contentEquals(clientConfig.uuid));
            }
        }
        catch (Exception e)
        {
            log.warn("could not remove element. Client conig:  " + clientConfig, e);
        }
        config.clientConfig.add(clientConfig);
        writeAndSendConfig();
    }

    public void updateRendererDevices(List<RendererDeviceConfiguration> rendererDevices)
    {
        config.rendererDevices = rendererDevices;
        writeAndSendConfig();
    }

    public RendererDeviceConfiguration getMediaRendererConfig(String udn)
    {
        Optional<RendererDeviceConfiguration> configEntry = config.rendererDevices.stream().filter(d -> d.mediaRenderer.udn.contentEquals(udn)).findFirst();
        if (configEntry.isPresent())
        {
            return configEntry.get();
        }
        return null;
    }

    public boolean isMediaRendererActive(String udn)
    {
        if (udn == null)
        {
            return true;
        }
        
        Optional<RendererDeviceConfiguration> configEntry = config.rendererDevices.stream().filter(d -> d.mediaRenderer.udn.contentEquals(udn)).findFirst();
        if (configEntry.isPresent())
        {
            return configEntry.get().active;
        }
        
        return true;
    }

    public void addMediaRendererDeviceConfig(RemoteDevice remoteDevice)
    {
        Optional<RendererDeviceConfiguration> configEntry = config.rendererDevices.stream()
                .filter(d -> d.mediaRenderer.udn.contentEquals(remoteDevice.getIdentity().getUdn().getIdentifierString())).findFirst();
        if (configEntry.isPresent())
        {
            log.debug(remoteDevice.getDetails().getFriendlyName() + " is already known.");
            if (!remoteDevice.getIdentity().getDescriptorURL().getHost().contentEquals(configEntry.get().ip))
            {
                configEntry.get().ip = remoteDevice.getIdentity().getDescriptorURL().getHost();
                writeAndSendConfig();
            }
        }
        else
        {
            RendererDeviceConfiguration c = new RendererDeviceConfiguration();
            c.active = true;
            c.ip = remoteDevice.getIdentity().getDescriptorURL().getHost();
            c.connectionString = "";
            c.deviceDriverType = "";
            c.displayString = remoteDevice.getDisplayString();
            c.hasOpenHomeDeviceDriver = remoteDevice.findService(new ServiceType("av-openhome-org", "Product")) != null;
            c.mediaRenderer = new MediaRendererDto(remoteDevice.getIdentity().getUdn().getIdentifierString(), remoteDevice.getDetails().getFriendlyName());
            config.rendererDevices.add(c);
            writeAndSendConfig();
            log.info(remoteDevice.getDetails().getFriendlyName() + " added RendererDevice config : " + c);
        }
    }

    public void updateRendererDevice(RendererDeviceConfiguration rendererDevice)
    {
        config.rendererDevices.removeIf(d -> d.mediaRenderer.udn.contentEquals(rendererDevice.mediaRenderer.udn));
        config.rendererDevices.add(rendererDevice);
        writeAndSendConfig();
    }

    public void deleteRendererDevice(RendererDeviceConfiguration rendererDevice)
    {
        config.rendererDevices.removeIf(d -> d.mediaRenderer.udn.contentEquals(rendererDevice.mediaRenderer.udn));
        writeAndSendConfig();
    }

    private void writeAndSendConfig()
    {
        persistence.writeConfig();
        ssePublisher.sendObjectAsJson(CLIENT_CONFIG_QUEUENAME, config);
    }
}
