package nextcp.service.upnp;

import org.jupnp.model.message.header.STAllHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import nextcp.config.RendererConfig;
import nextcp.dto.Config;

@Component
public class UpnpServiceFactory
{
    private Nextcp2UpnpServiceImpl upnpService = null;
    private Nextcp2DefaultUpnpServiceConfiguration nextcp2DefaultUpnpServiceConfiguration = null;

    @Autowired
	private RendererConfig rendererConfigService;
    
    @Autowired
    private Config config = null;

    public UpnpServiceFactory()
    {
    }

    @PostConstruct
    public void init() {
        // UpnpService upnpService = new UpnpServiceImpl(new DefaultUpnpServiceConfiguration());
        // CodegenUpnpServiceConfiguration sc = new CodegenUpnpServiceConfiguration();
    	nextcp2DefaultUpnpServiceConfiguration = new Nextcp2DefaultUpnpServiceConfiguration(
    		config.applicationConfig.upnpBindInterface);
        upnpService = new Nextcp2UpnpServiceImpl(nextcp2DefaultUpnpServiceConfiguration, rendererConfigService);
        
        upnpService.startup();
        upnpService.getControlPoint().search(new STAllHeader());
    }
    
    @Bean
    @Scope(value = "prototype")
    public Nextcp2UpnpServiceImpl upnpService()
    {
        return upnpService;
    }

}
