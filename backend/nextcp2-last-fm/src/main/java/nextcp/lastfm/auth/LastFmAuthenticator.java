package nextcp.lastfm.auth;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextcp.lastfm.ILastFmConfig;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class LastFmAuthenticator
{

    private static final Logger log = LoggerFactory.getLogger(LastFmAuthenticator.class.getName());
    private OkHttpClient okClient = new OkHttpClient.Builder().build();
    private ObjectMapper om = new ObjectMapper();
    
    private AuthToken authToken = null;

    @Autowired
    private ILastFmConfig config = null;

    public LastFmAuthenticator()
    {
    }

    @PostConstruct
    private void init()
    {
        if (config == null)
        {
            throw new RuntimeException("LastFM config is not configured. LastFM feature will be disabled.");
        }
        String requestUrl = String.format("http://ws.audioscrobbler.com/2.0/?method=auth.gettoken&api_key=%s&format=json", config.getApiKey());
        Response response = getResponseFromGetRequest(requestUrl);
        try
        {
            String content = response.body().string();
            authToken = om.readValue(content, AuthToken.class);
            log.info(authToken.toString());
        }
        catch (IOException e)
        {
            throw new RuntimeException("LastFM error reading auth token", e);
        }

    }
    

    public AuthToken getAuthToken()
    {
        return authToken;
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
