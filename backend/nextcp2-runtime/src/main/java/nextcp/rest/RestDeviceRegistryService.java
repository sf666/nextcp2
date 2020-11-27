package nextcp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaServerDto;
import nextcp.upnp.device.DeviceRegistry;

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
}
