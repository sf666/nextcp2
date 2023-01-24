package nextcp.service.upnp;

import org.jupnp.UpnpService;
import org.jupnp.UpnpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class UpnpServiceFactory
{
    private UpnpService upnpService = null;

    public UpnpServiceFactory()
    {
        
        // UpnpService upnpService = new UpnpServiceImpl(new DefaultUpnpServiceConfiguration());
        // CodegenUpnpServiceConfiguration sc = new CodegenUpnpServiceConfiguration();

        upnpService = new UpnpServiceImpl(new Nextcp2DefaultUpnpServiceConfiguration());
        upnpService.startup();
    }

    @Bean
    @Scope(value = "prototype")
    public UpnpService upnpService()
    {
        return upnpService;
    }

}
