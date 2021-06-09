package nextcp.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.dto.Config;
import nextcp.dto.SystemInformationDto;
import nextcp.lastfm.ILastFmConfig;
import nextcp.lastfm.auth.LastFmAuthenticator;
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
    private LastFmAuthenticator lastFmAuth = null;

    @Autowired
    private ILastFmConfig config = null;

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
        if (config == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "config is null ");
        }
        if (config.getApiKey() == null || StringUtils.isEmpty(config.getApiKey()))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Unknown LastFm API key.");
        }
        if (lastFmAuth.getAuthToken() == null || StringUtils.isEmpty(lastFmAuth.getAuthToken().getToken()))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Unknown LastFm Token is.");
        }

        return String.format("http://www.last.fm/api/auth/?api_key=%s&token=%s", config.getApiKey(), lastFmAuth.getAuthToken().getToken());
    }
}
