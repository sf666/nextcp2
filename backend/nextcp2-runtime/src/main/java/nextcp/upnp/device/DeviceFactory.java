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
        log.info("created new media renderer device : {} - {}", name.getDetails().getFriendlyName(), name.getIdentity().getUdn().toString());
        return new MediaRendererDevice(name, enabledByUser);
    }

	private MediaServerType getServerType(RemoteDevice device) {
		String manufacturer = device.getDetails().getManufacturerDetails().getManufacturer();
		if ("Universal Media Server".equalsIgnoreCase(manufacturer)) {
			return MediaServerType.UMS;
		}
		return MediaServerType.DEFAULT;
	}

    @Bean
    @Scope(value = "prototype")
    public MediaServerDevice mediaServerDeviceFactory(RemoteDevice name)
    {
    	
        switch (getServerType(name))
        {
            case UMS:
                log.info("Created new UmsServerDevice : {}", name.getDetails().getFriendlyName());
                return new UmsServerDevice(name);
            case DEFAULT:
                log.info("Created new ServerDevice : {}", name.getDetails().getFriendlyName());
                return new MediaServerDevice(name);
            default:
                log.warn("Unkown media server type : {}", name.getDetails().getFriendlyName());
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
