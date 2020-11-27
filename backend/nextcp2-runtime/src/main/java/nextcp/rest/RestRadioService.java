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
import nextcp.dto.RadioStation;
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
        MediaRendererDevice device = getMediaRendererByUdn(mediaRenderer.udn);
        if (device.hasRadioService())
        {
            return device.getRadioServiceBridge().getRadioStations();
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
}
