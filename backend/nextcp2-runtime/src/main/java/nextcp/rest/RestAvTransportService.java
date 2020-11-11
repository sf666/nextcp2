package nextcp.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.fourthline.cling.support.model.DIDLObject.Property.UPNP.ALBUM_ART_URI;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.PlayRadioDto;
import nextcp.dto.PlayRequestDto;
import nextcp.dto.UpnpAvTransportState;

/**
 * Here are AvTransportServices encapsulted, inclusing service events.
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/AvTransportService")
public class RestAvTransportService extends BaseRestService
{
    private static final Logger log = LoggerFactory.getLogger(RestAvTransportService.class.getName());

    @Autowired
    private DtoBuilder dtoBuilder = null;

    @PostMapping("/playResource")
    public void playResource(@RequestBody PlayRequestDto playRequest)
    {
        if (playRequest.mediaRendererDto == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Playing failed. Media renderer is not set.");
        }

        MediaRendererDevice device = getMediaRendererByUdn(playRequest.mediaRendererDto.udn);
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,
                    "playing failed. Select an available media renderer. Unavailable : " + playRequest.mediaRendererDto.udn);
        }
        if (device.getAvTransportServiceBridge() == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,
                    "playing failed. No AvTransport service available. UDN : " + playRequest.mediaRendererDto.udn);
        }
        device.getAvTransportServiceBridge().play(playRequest.streamUrl, playRequest.streamMetadata);
    }

    @PostMapping("/pause")
    public void pause(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        device.getAvTransportServiceBridge().pause();
    }

    @PostMapping("/play")
    public void play(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "play failed. Select an available media renderer.");
        }
        device.getAvTransportServiceBridge().play();
    }

    @PostMapping("/playOnlineResource")
    public void playOnlineResource(@RequestBody PlayRadioDto playRequest)
    {
        MediaRendererDevice device = getMediaRendererByUdn(playRequest.mediaRendererDto.udn);
        MusicTrack music = new MusicTrack();
        try
        {
            music.addProperty(new ALBUM_ART_URI(new URI(playRequest.radioStation.artworkUrl)));
            device.getAvTransportServiceBridge().play(playRequest.radioStation.resourceUrl, dtoBuilder.generateMetadataFromItem(music));
        }
        catch (URISyntaxException e)
        {
            log.error("playOnlineResource", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "playing radios failed: " + e.getMessage());
        }
    }

    /**
     * Aquire current AvTransportState
     * 
     * @param renderer
     * @return
     */
    @PostMapping("/MediaRendererAvTransportState")
    public UpnpAvTransportState getMediaRendererAvTransportState(@RequestBody MediaRendererDto renderer)
    {
        MediaRendererDevice device = getMediaRendererByUdn(renderer.udn);
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Tranport state cannot be retrieved: Select an available media renderer.");
        }
        return dtoBuilder.buildAvTransportStateDto(device.getAvTransportEventListener().getCurrentAvTransportState(), device);
    }

}
