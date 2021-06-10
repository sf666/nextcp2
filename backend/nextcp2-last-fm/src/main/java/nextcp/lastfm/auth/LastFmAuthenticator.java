package nextcp.lastfm.auth;

import java.io.IOException;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextcp.lastfm.ILastFmConfig;
import nextcp.lastfm.dto.AuthSessionStatus;
import nextcp.lastfm.dto.AuthToken;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
public class LastFmAuthenticator
{

    private static final Logger log = LoggerFactory.getLogger(LastFmAuthenticator.class.getName());
    private OkHttpClient okClient = new OkHttpClient.Builder().build();
    private ObjectMapper om = new ObjectMapper();

    private AuthToken userGrantingToken = null;

    @Autowired
    private ILastFmConfig lastFmConfig = null;

    @Autowired
    private Signer signer = null;
    
    

    public LastFmAuthenticator()
    {
    }

    @PostConstruct
    private void init()
    {
        if (lastFmConfig == null)
        {
            throw new RuntimeException("LastFM config is not configured. LastFM feature will be disabled.");
        }
        getUserGrantingAuthToken();
    }

    public AuthToken getUserGrantingAuthToken()
    {
        String requestUrl = String.format("http://ws.audioscrobbler.com/2.0/?method=auth.gettoken&api_key=%s&format=json", lastFmConfig.getApiKey());
        Response response = getResponseFromGetRequest(requestUrl);
        try
        {
            String content = response.body().string();
            AuthToken authToken = om.readValue(content, AuthToken.class);
            log.info(authToken.toString());
            this.userGrantingToken = authToken;
            return authToken;
        }
        catch (IOException e)
        {
            throw new RuntimeException("LastFM error reading auth token", e);
        }
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

        String sig = signer.signCall(params);

        String requestUrl = String.format("http://ws.audioscrobbler.com/2.0/?method=auth.getsession&api_key=%s&token=%s&api_sig=%s&format=json", lastFmConfig.getApiKey(),
                userGrantingToken.getToken(), sig);
        Response response = getResponseFromGetRequest(requestUrl);
        try
        {
            String content = response.body().string();
            log.info(content);
            AuthSessionStatus authSessionStatus = om.readValue(content, AuthSessionStatus.class);
            return authSessionStatus;
        }
        catch (IOException e)
        {
            throw new RuntimeException("LastFM error reading auth token", e);
        }
    }
    /**
     * User URL for authenticating a token. This URL needs to be called and approved by the user.
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

    public Response getResponseFromGetRequest(String requestUrl)
    {
        Request request = new Request.Builder().url(requestUrl).get().build();
        Call call = okClient.newCall(request);
        try
        {
            Response response = call.execute();
            // read status status code
            return response;
        }
        catch (IOException e)
        {
            throw new RuntimeException("LastFM request error", e);
        }
    }
}
