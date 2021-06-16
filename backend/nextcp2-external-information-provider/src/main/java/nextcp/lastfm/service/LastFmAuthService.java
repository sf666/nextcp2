package nextcp.lastfm.service;

import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import nextcp.lastfm.ILastFmConfig;
import nextcp.lastfm.dto.auth.AuthSessionStatus;
import nextcp.lastfm.dto.auth.AuthToken;
import nextcp.lastfm.request.LastFmApiRequestService;
import nextcp.lastfm.request.Parameter;

/**
 * 
 * <pre>
 * Initial authentication process:
 * 
 * 1. Aquire token
 * 2. User has to approve token
 * 3. Use approved user token for creating a LastFM session
 * 
 * </pre>
 */
@Service
public class LastFmAuthService
{

    private static final Logger log = LoggerFactory.getLogger(LastFmAuthService.class.getName());

    /**
     * token for application registration
     */
    private AuthToken userGrantingToken = null;

    @Autowired
    private ILastFmConfig lastFmConfig = null;

    @Autowired
    private LastFmApiRequestService api = null;

    public LastFmAuthService()
    {
    }

    @PostConstruct
    private void init()
    {
        if (lastFmConfig == null)
        {
            throw new RuntimeException("LastFM config is not configured. LastFM feature will be disabled.");
        }
    }

    public AuthToken getUserGrantingAuthToken()
    {
        String requestUrl = String.format("http://ws.audioscrobbler.com/2.0/?method=auth.gettoken&api_key=%s&format=json", lastFmConfig.getApiKey());
        AuthToken authToken = api.doApiRequest(requestUrl, AuthToken.class);
        log.info(authToken.toString());
        this.userGrantingToken = authToken;
        return authToken;
    }

    public AuthSessionStatus createSession()
    {
        if (this.userGrantingToken == null)
        {
            throw new RuntimeException("User has to approve a token first.");
        }
        TreeSet<Parameter> params = new TreeSet<>();
        params.add(new Parameter("api_key", lastFmConfig.getApiKey()));
        params.add(new Parameter("method", "auth.getsession"));
        params.add(new Parameter("token", userGrantingToken.getToken()));

        AuthSessionStatus authSessionStatus = api.doApiRequest(params, AuthSessionStatus.class, true);
        return authSessionStatus;
    }

    /**
     * User URL for authenticating a token. This URL needs to be called and approved by the user. In the next step this token has to be used to create a session key.
     * 
     * @return
     */
    public String getAppRegistrationUrl()
    {
        AuthToken authToken = getUserGrantingAuthToken();
        if (authToken == null || StringUtils.isEmpty(authToken.getToken()))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Unknown LastFm Token is.");
        }

        return String.format("http://www.last.fm/api/auth/?api_key=%s&token=%s", lastFmConfig.getApiKey(), authToken.getToken());
    }
}
