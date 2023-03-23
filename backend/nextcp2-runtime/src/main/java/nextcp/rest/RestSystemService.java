package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nextcp.dto.SystemInformationDto;
import nextcp.service.LastFmService;
import nextcp.service.SpotifyAuthServiceBridge;
import nextcp.service.SystemService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/SystemService")
public class RestSystemService
{

    private static final Logger log = LoggerFactory.getLogger(RestSystemService.class.getName());

    @Autowired
    private SystemService systemService = null;

    @Autowired
    private LastFmService lastFmAuth = null;

    @Autowired
    private SpotifyAuthServiceBridge spotifyService = null;

    public RestSystemService()
    {
    }

    @GetMapping("/getSystemInformation")
    public SystemInformationDto getSystemInformation()
    {
        return systemService.getSystemInformation();
    }

    @GetMapping("/getLastFmAppRegistration")
    public String getLastFMAppRegistrationPath()
    {
        return lastFmAuth.getLastFmAppRegistrationPath();
    }

    @GetMapping("/createLastFmSession")
    public void createLastFmSession()
    {
        lastFmAuth.createLastFmSession();
    }

    @PostMapping("/getSpotifyAppRegistration")
    public String getSpotifyAppRegistration(@RequestBody boolean protocolHandlerAvailable)
    {
        return spotifyService.getSpotifyRegistrationUrl(protocolHandlerAvailable);
    }

    @GetMapping("/createSpotifySession")
    public void createSpotifySession()
    {
        lastFmAuth.createLastFmSession();
    }

    @GetMapping("/spotifyCallbackOAuth/{spotifyCode}")
    @ResponseBody
    public String spotifyCallbackCode(@PathVariable("spotifyCode") String spotifyCode)
    {
        int codeIdx = spotifyCode.indexOf("?code=");
        String token = spotifyCode.substring(codeIdx + 6);

        if (log.isDebugEnabled())
        {
            log.debug("received OAuth callback from spotify : " + spotifyCode);
            log.debug("token : " + token);
        }
        spotifyService.registerSpotifyCode(token);
        
        return "<a href=\"JavaScript:window.close()\">Close this window</a>";
    }

    @GetMapping("/spotifyCallback")
    public void spotifyCallback(@RequestParam String code)
    {
        spotifyService.registerSpotifyCode(code);
        log.info("Spotify account connected with code : " + code);
    }

    @GetMapping("/restartNextcp2")
    public void restart(@RequestParam String code)
    {
        spotifyService.registerSpotifyCode(code);
        log.info("Spotify account connected with code : " + code);
    }
}
