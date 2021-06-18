package nextcp.spotify;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest;

@Service
public class SpotifyService
{
    private static final Logger log = LoggerFactory.getLogger(SpotifyService.class.getName());

    @Autowired
    private ISpotifyConfig config = null;

    private SpotifyApi spotifyApi = null;

    // private boolean refreshTokenNeeded = false;

    private boolean authorizationNeeded = true;

    private String codeVerifier = "";
    private String codeChallange = "";

    public SpotifyService()
    {
        codeVerifier = RandomStringUtils.random(128, true, true);
        byte[] sha256codeVerifier = DigestUtils.sha256(codeVerifier);
        codeChallange = Base64.getUrlEncoder().withoutPadding().encodeToString(sha256codeVerifier); 
    }

    @PostConstruct
    private void init()
    {
        if (StringUtils.isAllBlank(config.getClientId()))
        {
            log.warn("Spotify clientId or spotify clientSecret is not set. Spotify support disabled.");
        }
        else
        {
            try
            {
                SpotifyApi.Builder builder = new SpotifyApi.Builder();
                builder.setClientId(config.getClientId()).setRedirectUri(new URI("http://localhost:65525"));
                if (!StringUtils.isAllBlank(config.getSpotifyRefreshToken()))
                {
                    builder.setRefreshToken(config.getSpotifyRefreshToken());
                }
                spotifyApi = builder.build();

                AuthorizationCodeCredentials authorizationCodeCredentials;
                try
                {
                    if (!StringUtils.isAllBlank(config.getSpotifyRefreshToken()))
                    {
                        authorizationCodeCredentials = spotifyApi.authorizationCodeRefresh().build().execute();
                        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
                        log.info("spotify access token aquired.");
                    }
                }
                catch (ParseException | SpotifyWebApiException | IOException e)
                {
                    authorizationNeeded = true;
                    log.warn("could not connect to spotify api. Please re-authorize app.", e);
                }

            }
            catch (URISyntaxException e)
            {
                log.warn("Spotify API connect failed.", e);
            }
        }
    }

    public String getSpotifyRegistrationUrl()
    {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodePKCEUri(codeChallange).build();
        URI uri = authorizationCodeUriRequest.execute();
        try
        {
            String url = uri.toURL().toString();
            log.info("spotify registration url : " + url);
            return url;
        }
        catch (MalformedURLException e)
        {
            log.warn("Spotify API connect failed.", e);
        }
        return "";
    }

    public void setAuthCode(String authorizationCode)
    {
        AuthorizationCodePKCERequest authorizationCodeRequest = spotifyApi.authorizationCodePKCE(authorizationCode, codeVerifier).build();
        AuthorizationCodeCredentials authorizationCodeCredentials;
        try
        {
            authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        }
        catch (ParseException | SpotifyWebApiException | IOException e)
        {
            log.warn("cannot connect to spotify api.", e);
        }
    }

    public SpotifyApi getSpotifyApi()
    {
        return spotifyApi;
    }

    public boolean isAuthorizationNeeded()
    {
        return authorizationNeeded;
    }
}
