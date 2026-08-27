package nextcp.rest;

import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import nextcp.dto.BrowseRequestDto;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.util.BackendException;

@Component
public class BaseRestService
{
    @Autowired
    private DeviceRegistry deviceRegistry = null;

    public BaseRestService()
    {
        super();
    }

    protected void checkDeviceAvailability(BrowseRequestDto browseRequest, MediaServerDevice device)
    {
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + browseRequest.mediaServerUDN);
        }
    }

    protected void checkUdn(BrowseRequestDto browseRequest)
    {
        if (StringUtils.isBlank(browseRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
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
        throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "extended features not availbale : " + udn);
    }

    /**
     * Non-throwing variant of {@link #getExtendedMediaServerByUdn(String)}: returns {@code null}
     * when the UDN is blank, the server is not (currently) registered, or it does not support the
     * extended API. Use this for read-only status lookups (e.g. isAlbumLiked) that should degrade
     * gracefully instead of surfacing an HTTP error to the UI.
     */
    protected ExtendedApiMediaDevice findExtendedMediaServerByUdn(String udn)
    {
        if (StringUtils.isBlank(udn))
        {
            return null;
        }
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(udn));
        if (device instanceof ExtendedApiMediaDevice extended)
        {
            return extended;
        }
        return null;
    }

    /**
     * Turns a failed action into something the client can show. Endpoints used to swallow the
     * exception and push a toast instead, which left the request answering 200 - so the client
     * reported success and contradicted its own error toast. BackendExceptionAdvice maps this to 417
     * with the message, and HttpService shows it exactly once.
     *
     * Status reads are the exception : they degrade to an empty answer instead, because an absent
     * device is a normal state there and must not report on every poll.
     */
    protected BackendException failed(String message, Exception cause)
    {
        return cause == null ?
            new BackendException(BackendException.GENERIC_ERROR, message) :
            new BackendException(BackendException.GENERIC_ERROR, message, cause);
    }

    protected DeviceRegistry getDeviceRegistry()
    {
        return deviceRegistry;
    }

}