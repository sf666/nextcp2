package nextcp.spotify;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest;

import jakarta.annotation.PostConstruct;

@Service
public class SpotifyService
{
    private static final Logger log = LoggerFactory.getLogger(SpotifyService.class.getName());

    @Autowired
    private ISpotifyConfig config = null;

    private SpotifyApi spotifyApi = null;

    private long renewTokenTimeout = 0;

    private String codeVerifier = "";
    private String codeChallange = "";

    private static final long twoMinutesMilli = 2 * 60 * 1000;

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
            config.setUserAuthorizationNeeded(true);
            return;
        }
        if (StringUtils.isAllBlank(config.getRedirectUrl()))
        {
            log.warn("API not linked yet. Please connect nextcp to your Spotify account.");
            config.setUserAuthorizationNeeded(true);
            return;
        }

        try
        {
            SpotifyApi.Builder builder = new SpotifyApi.Builder();
            spotifyApi = builder.setClientId(config.getClientId()).setRedirectUri(new URI(config.getRedirectUrl())).build();

            if (!StringUtils.isAllBlank(config.getSpotifyRefreshToken()))
            {
                renewToken();
            }
        }
        catch (URISyntaxException e)
        {
            log.warn("Spotify API connect failed.", e);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void checkToken()
    {
        if (System.currentTimeMillis() > renewTokenTimeout)
        {
            renewToken();
        }
    }

    public void renewToken()
    {
        if (StringUtils.isAllBlank(config.getRedirectUrl()))
        {
            log.trace("checkToken: API not linked yet.");
            return;
        }

        log.info("renew Spotify token ...");
        spotifyApi.setRefreshToken(config.getSpotifyRefreshToken());
        AuthorizationCodePKCERefreshRequest authorizationCodePKCERefreshRequest = spotifyApi.authorizationCodePKCERefresh().build();
        AuthorizationCodeCredentials authorizationCodeCredentials;
        try
        {
            authorizationCodeCredentials = authorizationCodePKCERefreshRequest.execute();
            renewTokenTimeout = System.currentTimeMillis() + authorizationCodeCredentials.getExpiresIn() * 1000 - twoMinutesMilli;
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            config.setSpotifyRefreshToken(authorizationCodeCredentials.getRefreshToken());
        }
        catch (ParseException | IOException e)
        {
            log.warn("Parse or IO exception. Error renewing spotify access token. " + e.getMessage());
        }
        catch (SpotifyWebApiException e)
        {
            renewTokenTimeout = Long.MAX_VALUE;
            spotifyApi.setRefreshToken("");
            config.setUserAuthorizationNeeded(true);
            log.warn("Spotify Web Api exception. Error renewing spotify access token. " + e.getMessage());
        }
    }

    public String getSpotifyRegistrationUrl(boolean protocolHandlerAvailable)
    {
        try
        {
            init();
            AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodePKCEUri(codeChallange).build();
            URI uri = authorizationCodeUriRequest.execute();
            String url = uri.toURL().toString();
            log.info("spotify registration url : " + url);
            return url;
        }
        catch (MalformedURLException e)
        {
            log.warn("Spotify API connect failed.", e);
            config.setUserAuthorizationNeeded(true);
        }
        return "";
    }

    /**
     * 
     * @param authorizationCode
     * @return AccessToken
     */
    public String setAuthCode(String authorizationCode)
    {
        AuthorizationCodePKCERequest authorizationCodeRequest = spotifyApi.authorizationCodePKCE(authorizationCode, codeVerifier).build();
        AuthorizationCodeCredentials authorizationCodeCredentials;
        try
        {
            authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            config.setUserAuthorizationNeeded(false);
            log.info("spotify user authenticated ...");
            return authorizationCodeCredentials.getRefreshToken();
        }
        catch (ParseException | IOException e)
        {
            log.warn("cannot connect to spotify api.", e);
            return "";
        }
        catch (SpotifyWebApiException e)
        {
            log.warn("cannot connect to spotify api.", e);
            config.setUserAuthorizationNeeded(true);
            return "";
        }
    }

    public SpotifyApi getSpotifyApi()
    {
        return spotifyApi;
    }
}
