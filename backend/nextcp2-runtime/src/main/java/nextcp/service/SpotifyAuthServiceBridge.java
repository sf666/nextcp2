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

    public String getSpotifyRegistrationUrl()
    {
        return spotifyService.getSpotifyRegistrationUrl();
    }

    public void registerSpotifyCode(String token)
    {
        config.spotifyRefreshToken = spotifyService.setAuthCode(token);
        confService.writeAndSendConfig();
        this.publisher.publishEvent(new ToastrMessage("", "info", "Spotify", "user account successfully connected to Spotify"));
    }
}
