package nextcp.domainmodel.device.mediarenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import nextcp.devicedriver.DeviceDriverDiscoveryService;

/**
 * Some factories ...
 */
@Configuration
public class MediaRendererFactories
{
    private static final Logger log = LoggerFactory.getLogger(MediaRendererFactories.class.getName());

    @Autowired
    private DeviceDriverDiscoveryService deviceDriverService = null;

    /**
     * Create external device drive. 
     * 
     * @param rendererUDN
     * @param driverName
     * @param connectionString
     * @return
     */
    @Bean
    @Scope(value = "prototype")
    public DeviceDriver createDeviceDriver(String rendererUDN, String driverName, String connectionString)
    {
        if (deviceDriverService.deviceDriverExists(driverName))
        {
            return new DeviceDriver(rendererUDN, driverName, connectionString);
        }
        else
        {
            log.warn("there is no known device driver registered for name : " + driverName);
        }
        return null;
    }
}
