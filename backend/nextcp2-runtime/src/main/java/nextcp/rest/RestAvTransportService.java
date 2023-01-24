package nextcp.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.jupnp.support.model.DIDLObject.Property.UPNP.ALBUM_ART_URI;
import org.jupnp.support.model.item.MusicTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.dto.MediaRendererDto;
import nextcp.dto.PlayRadioDto;
import nextcp.dto.PlayRequestDto;
import nextcp.dto.ToastrMessage;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;

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

    @Autowired
    private ApplicationEventPublisher publisher = null;

    @PostMapping("/playResource")
    public void playResource(@RequestBody PlayRequestDto playRequest)
    {
        MediaRendererDevice device = checkPlayInput(playRequest);
        if (device.getProductService() != null)
        {
            if (device.getProductService().getCurrentInputSource().Type.equalsIgnoreCase("playlist"))
            {
                log.info("Try to find current song in playlist ... ");
                if (device.getPlaylistServiceBridge().seekId(playRequest.streamUrl))
                {
                    log.debug("playing streamURL from playlist ... ");
                    return;
                }
            }
        }
        log.debug("try playing on AVTransport ... ");
        device.getAvTransportBridge().play(playRequest.streamUrl, playRequest.streamMetadata);
    }

    @PostMapping("/playResourceNext")
    public void playResourceNext(@RequestBody PlayRequestDto playRequest)
    {
        try
        {
            MediaRendererDevice device = checkPlayInput(playRequest);
            device.getAvTransportBridge().playNext(playRequest.streamUrl, playRequest.streamMetadata);
            publisher.publishEvent(new ToastrMessage(null, "success", "play", "song will be played next."));
        }
        catch (Exception e)
        {
            publisher.publishEvent(new ToastrMessage(null, "error", "play next", e.getMessage()));
        }
    }

    private MediaRendererDevice checkPlayInput(PlayRequestDto playRequest)
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
        if (device.getTransportServiceBridge() == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "playing failed. No AvTransport service available. UDN : " + playRequest.mediaRendererDto.udn);
        }
        return device;
    }

    @PostMapping("/pause")
    public void pause(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        device.getTransportServiceBridge().pause();
    }

    @PostMapping("/play")
    public void play(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "play failed. Select an available media renderer.");
        }
        device.getTransportServiceBridge().play();
    }

    @PostMapping("/playOnlineResource")
    public void playOnlineResource(@RequestBody PlayRadioDto playRequest)
    {
        MediaRendererDevice device = getMediaRendererByUdn(playRequest.mediaRendererDto.udn);
        MusicTrack music = new MusicTrack();
        try
        {
            music.addProperty(new ALBUM_ART_URI(new URI(playRequest.radioStation.artworkUrl)));
            device.getAvTransportBridge().play(playRequest.radioStation.resourceUrl, dtoBuilder.generateMetadataFromItem(music));
        }
        catch (URISyntaxException e)
        {
            log.error("playOnlineResource", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "playing radios failed: " + e.getMessage());
        }
    }

    /**
     * Acquire current AvTransportState
     * 
     * @param renderer
     * @return
     */
    @PostMapping("/MediaRendererAvTransportState")
    public void getMediaRendererAvTransportState(@RequestBody MediaRendererDto renderer)
    {
        MediaRendererDevice device = getMediaRendererByUdn(renderer.udn);
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Tranport state cannot be retrieved: Select an available media renderer.");
        }
        device.getAvTransportEventPublisher().publishAllAvEvents();
        // return dtoBuilder.buildAvTransportStateDto(device.getAvTransportEventListener().getCurrentAvTransportState(), device);
    }

}
