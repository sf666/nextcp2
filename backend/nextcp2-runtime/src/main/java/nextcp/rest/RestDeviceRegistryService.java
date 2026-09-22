package nextcp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.jupnp.model.types.UDN;
import org.springframework.web.bind.annotation.PathVariable;

import nextcp.dto.DeviceDetailsDto;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaServerDto;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.util.BackendException;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/DeviceRegistry")
public class RestDeviceRegistryService
{
    
    /**
     * Transform Domain-Objects to DTOs.
     */
    @Autowired
    private DtoBuilder dtoBuilder;

    /**
     * Bridge to Domain Model 
     */
    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @GetMapping("/mediaServer")
    public List<MediaServerDto> getMediaServer()
    {
        List<MediaServerDto> mediaServer = dtoBuilder.getMediaServerAsDto(deviceRegistry.getAvailableMediaServer());
        return mediaServer;
    }

    @GetMapping("/mediaRenderer")
    public List<MediaRendererDto> getMediaRenderer()
    {
        List<MediaRendererDto> mediaRenderer = dtoBuilder.getMediaRendererAsDto(deviceRegistry.getAvailableMediaRenderer());
        return mediaRenderer;
    }

    @GetMapping("/activeMediaRenderer")
    public List<MediaRendererDto> getActiveMediaRenderer()
    {
        List<MediaRendererDto> mediaRenderer = dtoBuilder.getMediaRendererAsDto(deviceRegistry.getActiveMediaRenderer());
        return mediaRenderer;
    }

    /**
     * What one device says about itself, for the device info dialog.
     *
     * Read on demand rather than sent with the device lists: those go to every browser on every
     * change, and the action lists in here are only interesting while the dialog is open.
     */
    @GetMapping("/deviceDetails/{udn}")
    public DeviceDetailsDto getDeviceDetails(@PathVariable("udn") String udn)
    {
        UDN deviceUdn = new UDN(udn);
        MediaServerDevice server = deviceRegistry.getMediaServerByUDN(deviceUdn);
        if (server != null)
        {
            return dtoBuilder.buildDeviceDetails(server, true, server.getFeatures(), server.getSearchCaps());
        }
        MediaRendererDevice renderer = deviceRegistry.getMediaRendererByUDN(deviceUdn);
        if (renderer != null)
        {
            return dtoBuilder.buildDeviceDetails(renderer, false, null, null);
        }
        throw new BackendException(BackendException.DEVICE_NOT_FOUND, "no device known with udn " + udn);
    }
}
