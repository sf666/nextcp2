package nextcp.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.dto.Config;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlayOpenHomeRadioDto;
import nextcp.dto.PlayRequestDto;
import nextcp.dto.RadioStation;
import nextcp.upnp.GenActionException;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/RadioService")
public class RestRadioService extends BaseRestService
{
    private static final Logger log = LoggerFactory.getLogger(RestRadioService.class.getName());

    @Autowired
    private Config config = null;

    /**
     * The control point usually loads the whole configuration file and therefore this service method might not be called at all.
     */
    @GetMapping("/radioStations")
    public List<RadioStation> getConfiguration()
    {
        return config.radioStation;
    }

    @PostMapping("/deviceRadioStations")
    public List<MusicItemDto> getRadioStation(@RequestBody MediaRendererDto mediaRenderer)
    {
    	log.info("entering deviceRadioStations");
        MediaRendererDevice device = getMediaRendererByUdn(mediaRenderer.udn);
        if (device.hasRadioService())
        {
        	List<MusicItemDto> radioStationList = device.getRadioServiceBridge().getRadioStations();
        	log.info("radioStationList size {}", radioStationList.size());
            return radioStationList;
        }
        log.debug("device has no openhome radio service");
        return new ArrayList<>();
    }

    @PostMapping("/playRadioStation")
    public void getRadioStation(@RequestBody PlayOpenHomeRadioDto playRequest)
    {
        MediaRendererDevice device = getMediaRendererByUdn(playRequest.mediaRendererDto.udn);
        if (device.hasRadioService())
        {
            device.getRadioServiceBridge().play(playRequest.radioStation);
            return;
        }
        log.debug("device has no openhome radio service");
    }

    /**
     * Plays an arbitrary stream URL (e.g. an audioBroadcast item from a media server). On an
     * OpenHome renderer it switches the active source to "Radio" (so the renderer uses its radio
     * pipeline, which consumes ICY metadata) and loads the stream with its DIDL metadata. If the
     * renderer has no OpenHome Radio service it falls back to plain UPnP AVTransport.
     */
    @PostMapping("/playStream")
    public void playStream(@RequestBody PlayRequestDto playRequest)
    {
        MediaRendererDevice device = getMediaRendererByUdn(playRequest.mediaRendererDto.udn);
        // Diagnostic (temporary): capture which PlayAs Modes the renderer advertises on its
        // OpenHome Transport service, to decide whether to migrate from the legacy Radio service.
        device.logTransportModesForDiagnostics();
        if (device.hasRadioService() && !device.isRadioPlayUnsupported())
        {
            try
            {
                if (device.hasProductService())
                {
                	log.debug("switching to Radio source for device {}", device.getFriendlyName());
                    device.getProductService().switchToSource("Radio");
                }
                device.getRadioServiceBridge().playStream(playRequest.streamUrl, playRequest.streamMetadata);
                return;
            }
            catch (Exception e)
            {
                // Some OpenHome renderers (e.g. Linn/LUMIN) don't accept arbitrary stream URIs on
                // their Radio source (SetChannel -> UPnP error 708 "Unsupported action"). Remember
                // this per device so we don't retry on every play, and fall back to AVTransport.
                device.setRadioPlayUnsupported(true);
                // GenActionException carries the full SOAP fault (incl. the UPnP errorCode /
                // errorDescription) in its 'description' field, while getMessage() is null. Log the
                // full fault at WARN so the exact reason for the 708 is not lost.
                log.warn("OpenHome Radio play failed for {} ({}); using AVTransport for this device from now on",
                    playRequest.streamUrl, describeActionError(e));
            }
        }
        log.debug("playing stream via AVTransport: {}", playRequest.streamUrl);
        if (device.getAvTransportBridge() != null)
        {
            device.getAvTransportBridge().play(playRequest.streamUrl, playRequest.streamMetadata);
        }
        else
        {
            log.warn("device has neither a working OpenHome radio nor AVTransport - cannot play stream {}", playRequest.streamUrl);
        }
    }

    /**
     * Extracts the most informative message from a failed UPnP action. For a
     * {@link GenActionException} the full SOAP fault body (including the UPnP errorCode /
     * errorDescription) is carried in its {@code description} field, whereas {@code getMessage()}
     * is null. Falls back to the exception message / class for other exception types.
     */
    private static String describeActionError(Throwable e)
    {
        if (e instanceof GenActionException ge && ge.description != null && !ge.description.isBlank())
        {
            return ge.description;
        }
        return e.getMessage() != null ? e.getMessage() : e.toString();
    }
}
