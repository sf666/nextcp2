package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.devicedriver.DeviceDriverDiscoveryService;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/SimpleDeviceControl")
public class RestSimpleDeviceControl extends BaseRestService
{
    private static final Logger log = LoggerFactory.getLogger(RestSimpleDeviceControl.class.getName());

    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @Autowired
    private DeviceDriverDiscoveryService deviceDriverDiscoveryService = null;

    /**
     * 
     * @param udn
     * @param newState
     *            ON / OFF
     */
    @GetMapping("/standby/{mediaServerDevice}/{state}")
    public String standby(@PathVariable("mediaServerDevice") String udn, @PathVariable("state") String newState)
    {
        boolean standby = false;
        if ("on".equals(newState.toLowerCase()))
        {
            standby = false;
        }
        else if ("on".equals(newState.toLowerCase()))
        {
            standby = false;
        }
        else
        {
            log.warn("Illegal request state : " + newState);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Illegal request state : " + newState);
        }

        log.info(String.format("Setting standby state on device %s to : %s", udn, Boolean.toString(standby)));
        MediaRendererDevice device = getMediaRendererByUdn(udn);

        if (standby)
        {
            device.getPlaylistServiceBridge().pause();
        }
        device.setStandby(standby);
        return "OK";
    }

}
