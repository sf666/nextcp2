package nextcp.service.upnp;

import java.util.concurrent.TimeUnit;
import org.jupnp.model.message.header.STAllHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import jakarta.annotation.PostConstruct;
import nextcp.config.RendererConfig;

@Configuration
public class UpnpServiceFactory
{
    private Nextcp2UpnpServiceImpl upnpService = null;
    
    private static final Logger log = LoggerFactory.getLogger(UpnpServiceFactory.class.getName());
    
    @Autowired
    private Nextcp2DefaultUpnpServiceConfiguration nextcp2DefaultUpnpServiceConfiguration = null;

    @Autowired
	private RendererConfig rendererConfigService;
    
    public UpnpServiceFactory()
    {
    }

    @PostConstruct
    public void init() {
    	log.info("[UpnpServiceFactory] starting UPnP service ... ");
        // UpnpService upnpService = new UpnpServiceImpl(new DefaultUpnpServiceConfiguration());
        // CodegenUpnpServiceConfiguration sc = new CodegenUpnpServiceConfiguration();
        upnpService = new Nextcp2UpnpServiceImpl(nextcp2DefaultUpnpServiceConfiguration, rendererConfigService);
        
        upnpService.startup();
    }
    
    @Bean
    @Scope(value = "prototype")
    public Nextcp2UpnpServiceImpl upnpService()
    {
        return upnpService;
    }
    
    @Scheduled(fixedRate = 10,timeUnit = TimeUnit.MINUTES)
    public void scanForDevices() {
        upnpService.getControlPoint().search(new STAllHeader());
    }
}
