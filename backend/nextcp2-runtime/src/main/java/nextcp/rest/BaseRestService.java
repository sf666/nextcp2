package nextcp.rest;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.model.types.UDN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import nextcp.domainmodel.device.DeviceRegistry;
import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;

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

    protected DeviceRegistry getDeviceRegistry()
    {
        return deviceRegistry;
    }

}