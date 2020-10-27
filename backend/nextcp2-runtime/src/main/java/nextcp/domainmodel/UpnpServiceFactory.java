package nextcp.domainmodel;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class UpnpServiceFactory
{
    private UpnpService upnpService = new UpnpServiceImpl();

    @Bean
    @Scope(value = "prototype")
    public UpnpService upnpService()
    {
        return upnpService;
    }

}
