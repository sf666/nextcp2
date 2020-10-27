package nextcp.domainmodel.device;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.domainmodel.device.mediaserver.JMinimDevice;
import nextcp.domainmodel.device.mediaserver.MediaServerDevice;

@Configuration
public class DeviceFactory
{

    @Bean
    @Scope(value = "prototype")
    public MediaRendererDevice mediaRendererDeviceFactory(RemoteDevice name)
    {
        return new MediaRendererDevice(name);
    }

    @Bean
    @Scope(value = "prototype")
    public MediaServerDevice mediaServerDeviceFactory(RemoteDevice name)
    {
        return new MediaServerDevice(name);
    }

    @Bean
    @Scope(value = "prototype")
    public JMinimDevice mediaServerJMinim(RemoteDevice name)
    {
        return new JMinimDevice(name);
    }
}
