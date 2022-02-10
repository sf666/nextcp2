package nextcp.rest;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.model.types.UDN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

@Component
public class BaseRestService
{
    @Autowired
    private DeviceRegistry deviceRegistry = null;

    public BaseRestService()
    {
        super();
    }

    protected MediaRendererDevice getMediaRendererByUdn(String udn)
    {
        if (udn == null || StringUtils.isBlank(udn))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "please provide output device (media-renderer).");
        }

        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(udn));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Media-Renderer not found : " + udn);
        }
        return device;
    }

    protected MediaServerDevice getMediaServerByUdn(String udn)
    {
        if (udn == null || StringUtils.isBlank(udn))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "please provide output device (media-renderer).");
        }

        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(udn));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Media-Server not found : " + udn);
        }
        return device;
    }

    protected ExtendedApiMediaDevice getExtendedMediaServerByUdn(String udn)
    {
        if (udn == null || StringUtils.isBlank(udn))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "please provide output device (media-renderer).");
        }

        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(udn));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Media-Server not found : " + udn);
        }
        
        if (device instanceof ExtendedApiMediaDevice)
        {
            return ((ExtendedApiMediaDevice) device);
        }
        return null;
    }

    protected DeviceRegistry getDeviceRegistry()
    {
        return deviceRegistry;
    }

}