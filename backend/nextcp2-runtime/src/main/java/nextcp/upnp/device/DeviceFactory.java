package nextcp.upnp.device;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import nextcp.upnp.device.mediarenderer.LuminDevice;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediaserver.JMinimDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.device.mediaserver.UmsServerDevice;

@Configuration
public class DeviceFactory
{

    @Bean
    @Scope(value = "prototype")
    public MediaRendererDevice mediaRendererDeviceFactory(RemoteDevice name)
    {
        if ("LUMIN".equalsIgnoreCase(name.getDetails().getModelDetails().getModelDescription()))
        {
            return new LuminDevice(name);
        }
        return new MediaRendererDevice(name);
    }

    @Bean
    @Scope(value = "prototype")
    public MediaServerDevice mediaServerDeviceFactory(RemoteDevice name, MediaServerType serverType)
    {
        switch (serverType)
        {
            case UMS:
                return new UmsServerDevice(name);
            case DEFAULT:
                return new MediaServerDevice(name);
            default:
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
