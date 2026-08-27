package nextcp.rest;

import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw failed("unknown media server : " + browseRequest.mediaServerUDN, null);
        }
    }

    protected void checkUdn(BrowseRequestDto browseRequest)
    {
        if (StringUtils.isBlank(browseRequest.mediaServerUDN))
        {
            throw failed("media server ID shall not be empty", null);
        }
    }

    protected MediaRendererDevice getMediaRendererByUdn(String udn)
    {
        if (udn == null || StringUtils.isBlank(udn))
        {
            throw failed("please provide output device (media-renderer).", null);
        }

        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(udn));
        if (device == null)
        {
            throw failed("Media-Renderer not found : " + udn, null);
        }
        return device;
    }

    protected MediaServerDevice getMediaServerByUdn(String udn)
    {
        if (udn == null || StringUtils.isBlank(udn))
        {
            throw failed("please provide output device (media-renderer).", null);
        }

        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(udn));
        if (device == null)
        {
            throw failed("Media-Server not found : " + udn, null);
        }
        return device;
    }

    protected ExtendedApiMediaDevice getExtendedMediaServerByUdn(String udn)
    {
        if (udn == null || StringUtils.isBlank(udn))
        {
            throw failed("please provide output device (media-renderer).", null);
        }

        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(udn));
        if (device == null)
        {
            throw failed("Media-Server not found : " + udn, null);
        }

        if (device instanceof ExtendedApiMediaDevice)
        {
            return ((ExtendedApiMediaDevice) device);
        }
        throw failed("extended features not availbale : " + udn, null);
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
    protected BackendException failed(String whatFailed, Exception cause)
    {
        if (cause == null)
        {
            return new BackendException(BackendException.GENERIC_ERROR, whatFailed);
        }
        return new BackendException(BackendException.GENERIC_ERROR, whatFailed + " : " + reasonOf(cause), cause);
    }

    /**
     * The reason out of an exception, without the wrapping a status carries. Plain getMessage() on a
     * ResponseStatusException reads 417 EXPECTATION_FAILED "Media-Renderer not found : ..." and that
     * whole string used to end up in the toast the user sees.
     */
    private String reasonOf(Exception cause)
    {
        if (cause instanceof ResponseStatusException statusException && statusException.getReason() != null)
        {
            return statusException.getReason();
        }
        return cause.getMessage();
    }

    protected DeviceRegistry getDeviceRegistry()
    {
        return deviceRegistry;
    }

}