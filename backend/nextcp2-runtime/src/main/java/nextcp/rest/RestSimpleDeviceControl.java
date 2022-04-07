package nextcp.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.dto.MusicItemDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/SimpleDeviceControl")
public class RestSimpleDeviceControl extends BaseRestService
{
    private static final Logger log = LoggerFactory.getLogger(RestSimpleDeviceControl.class.getName());

    /**
     * set standby state.
     * 
     * @param udn
     *            udn of controlled device
     * @param newState
     *            ON / OFF
     */
    @GetMapping("/standby/{mediaRendererDevice}/{state}")
    public boolean standby(@PathVariable("mediaRendererDevice") String udn, @PathVariable("state") boolean standby)
    {
        log.info(String.format("Setting standby state on device %s to : %s", udn, Boolean.toString(standby)));
        MediaRendererDevice device = getMediaRendererByUdn(udn);
        if (standby)
        {
            device.getPlaylistServiceBridge().pause();
        }
        device.setStandby(standby);
        return standby;
    }

    /**
     * retrieve standby state.
     * 
     * @param udn
     * @return Standby state
     */
    @GetMapping("/standby/{mediaRendererDevice}")
    public Boolean standby(@PathVariable("mediaRendererDevice") String udn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(udn);
        return device.getStandby();
    }

    @GetMapping("/playDefaultRadio/{mediaRendererDevice}/{station}")
    public void playDefaultRadio(@PathVariable("mediaRendererDevice") String udn, @PathVariable("station") String station)
    {
        MediaRendererDevice device = getMediaRendererByUdn(udn);
        Optional<MusicItemDto> radio = device.getRadioServiceBridge().getRadioStations().stream().filter(mi -> mi.title.toLowerCase().startsWith(station)).findFirst();
        if (radio.isPresent())
        {
            device.getRadioServiceBridge().play(radio.get());
        }
    }

}
