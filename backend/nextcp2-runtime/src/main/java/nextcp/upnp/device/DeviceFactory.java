package nextcp.upnp.device;

import org.jupnp.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediaserver.JMinimDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.device.mediaserver.UmsServerDevice;

@Configuration
public class DeviceFactory
{
    private static final Logger log = LoggerFactory.getLogger(DeviceFactory.class.getName());

    @Bean
    @Scope(value = "prototype")
    public MediaRendererDevice mediaRendererDeviceFactory(RemoteDevice name, boolean enabledByUser)
    {
        // if ("LUMIN".equalsIgnoreCase(name.getDetails().getModelDetails().getModelDescription()))
        // {
        // return new LuminDevice(name);
        // }
        log.info("created new media renderer device : {} ", name);
        return new MediaRendererDevice(name, enabledByUser);
    }

    @Bean
    @Scope(value = "prototype")
    public MediaServerDevice mediaServerDeviceFactory(RemoteDevice name, MediaServerType serverType)
    {
        switch (serverType)
        {
            case UMS:
                log.info("Created new UmsServerDevice : {}", name);
                return new UmsServerDevice(name);
            case DEFAULT:
                log.info("Created new ServerDevice : {}", name);
                return new MediaServerDevice(name);
            default:
                log.info("Unkown media server type : {}", serverType.toString());
                throw new RuntimeException("Unknown server type.");
        }
    }

    @Bean
    @Scope(value = "prototype")
    public JMinimDevice mediaServerJMinim(RemoteDevice name)
    {
        return new JMinimDevice(name);
    }
}
