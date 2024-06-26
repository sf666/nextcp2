package nextcp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import nextcp.dto.Config;
import nextcp.dto.ToastrMessage;
import nextcp.spotify.SpotifyService;

@Service
public class SpotifyAuthServiceBridge
{

    @Autowired
    private Config config = null;

    @Autowired
    private ConfigService confService = null;

    @Autowired
    private SpotifyService spotifyService = null;
    
    @Autowired
    private ApplicationEventPublisher publisher = null;

    public SpotifyAuthServiceBridge()
    {
    }

    public String getSpotifyRegistrationUrl(boolean protocolHandlerAvailable)
    {
        String redirect = "";
        if (protocolHandlerAvailable)
        {
            redirect = "web+nextcp://localhost/";
        }
        else
        {
            redirect = "http://localhost:65525";
        }
        config.spotifyConfig.redirectUrl = redirect;
        return spotifyService.getSpotifyRegistrationUrl(protocolHandlerAvailable);
    }

    public void registerSpotifyCode(String token)
    {
        config.spotifyConfig.refreshToken = spotifyService.setAuthCode(token);
        confService.writeAndSendConfig();
        this.publisher.publishEvent(new ToastrMessage("", "info", "Spotify", "user account successfully connected to Spotify"));
    }
}
