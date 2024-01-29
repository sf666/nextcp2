package nextcp.service.upnp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import nextcp.config.RendererConfig;
import nextcp.dto.Config;
import nextcp.upnp.device.DeviceRegistry;

@Component
public class UpnpServiceFactory
{
    private Nextcp2UpnpServiceImpl upnpService = null;
    private Nextcp2DefaultUpnpServiceConfiguration nextcp2DefaultUpnpServiceConfiguration = null;

    @Autowired
	private RendererConfig rendererConfigService;
    
    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @Autowired
    private Config config = null;

    public UpnpServiceFactory()
    {
    }

    @PostConstruct
    public void init() {
        // UpnpService upnpService = new UpnpServiceImpl(new DefaultUpnpServiceConfiguration());
        // CodegenUpnpServiceConfiguration sc = new CodegenUpnpServiceConfiguration();
    	nextcp2DefaultUpnpServiceConfiguration = new Nextcp2DefaultUpnpServiceConfiguration(config.applicationConfig.upnpStreamClient);
    	nextcp2DefaultUpnpServiceConfiguration.setDeviceRegistry(deviceRegistry);
        upnpService = new Nextcp2UpnpServiceImpl(nextcp2DefaultUpnpServiceConfiguration, rendererConfigService);
        
        upnpService.startup();
    }
    
    @Bean
    @Scope(value = "prototype")
    public Nextcp2UpnpServiceImpl upnpService()
    {
        return upnpService;
    }

}
