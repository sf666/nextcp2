package main;


import org.jupnp.DefaultUpnpServiceConfiguration;
import org.jupnp.UpnpService;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.model.message.UpnpHeaders;
import org.jupnp.model.meta.RemoteDeviceIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class UpnpServiceFactory
{
    private UpnpService upnpService = null;
    private UpnpHeaders upnpHeaders = new UpnpHeaders();
    
    public UpnpServiceFactory()
    {
        DefaultUpnpServiceConfiguration sc = new DefaultUpnpServiceConfiguration() {
            @Override
            public UpnpHeaders getDescriptorRetrievalHeaders(RemoteDeviceIdentity identity) {
                return upnpHeaders;
            }

            @Override
            public int getAliveIntervalMillis() {
                return 10000;
            }
        };        
        upnpService = new UpnpServiceImpl();
    }
    
    @Bean
    @Scope(value = "prototype")
    public UpnpService upnpService()
    {
        return upnpService;
    }

}
