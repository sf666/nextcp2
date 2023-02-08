package nextcp.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    public void playDefaultRadio(@PathVariable("mediaRendererDevice") String udn, @PathVariable("station") String encodedStation) throws UnsupportedEncodingException
    {
        String station = URLDecoder.decode(encodedStation, "UTF-8");
        MediaRendererDevice device = getMediaRendererByUdn(udn);
        Optional<MusicItemDto> radio = device.getRadioServiceBridge().getRadioStations().stream().filter(mi -> mi.title.toLowerCase().startsWith(station)).findFirst();
        if (radio.isPresent())
        {
            log.info("playing radio ... " + station);
            device.getRadioServiceBridge().play(radio.get());
        }
        else
        {
            log.warn("radio station not found : " + station);
        }
    }

    @GetMapping("/pauseRadio/{mediaRendererDevice}")
    public void pauseRadio(@PathVariable("mediaRendererDevice") String udn)
    {
        log.info("pause radio ...");
        MediaRendererDevice device = getMediaRendererByUdn(udn);
        device.getRadioServiceBridge().pause();
    }

    @GetMapping("/selectInput/{mediaRendererDevice}/{input}")
    public void pauseRadio(@PathVariable("mediaRendererDevice") String udn, @PathVariable("input") String inp)
    {
        if (udn == null)
        {
            log.warn("selectInput: no device selected ... ");
        }
        if (inp == null)
        {
            log.warn("selectInput: no input set ... ");
        }
        log.info("select input to " + inp);
        MediaRendererDevice device = getMediaRendererByUdn(udn);
        device.getRadioServiceBridge().pause();
    }
}
