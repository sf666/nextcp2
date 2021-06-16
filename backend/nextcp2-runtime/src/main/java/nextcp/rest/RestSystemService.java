package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/getSpotifyAppRegistration")
    public String getSpotifyAppRegistration()
    {
        return spotifyService.getSpotifyRegistrationUrl();
    }

    @GetMapping("/createSpotifySession")
    public void createSpotifySession()
    {
        lastFmAuth.createLastFmSession();
    }

    @GetMapping("/spotifyCallback")
    public String spotifyCallback(@RequestParam String code)
    {
        spotifyService.registerAccessToken(code);
        log.info("Spotify account connected with code : " + code);
        return "<html><head><title>Close</title></head><body onload=\"window.close();\"></body></html>";
    }
}
