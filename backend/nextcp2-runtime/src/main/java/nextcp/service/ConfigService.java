package nextcp.service;

import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import nextcp.config.ConfigPersistence;
import nextcp.dto.Config;
import nextcp.dto.MusicbrainzSupport;
import nextcp.dto.ToastrMessage;
import nextcp.dto.UiClientConfig;
import nextcp.eventBridge.SsePublisher;
import nextcp.spotify.ISpotifyConfig;

@Controller
public class ConfigService
{
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class.getName());

    public static String CONFIG_QUEUENAME = "CONFIG_CHANGED";

    @Autowired
    private Config config = null;

    @Autowired
    private ConfigPersistence persistence = null;

    @Autowired
    private SsePublisher ssePublisher = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    public void saveMusicBrainzConfig(MusicbrainzSupport mbConfig)
    {
        if (!isCurrentBase64Password(mbConfig.password))
        {
            mbConfig.password = Base64.getEncoder().encodeToString(mbConfig.password.getBytes());
        }
        persistence.updateMusicBrainzInjectionBean(mbConfig);        
        config.musicbrainzSupport = mbConfig;
        writeAndSendConfig();
    }

    private boolean isCurrentBase64Password(String newPassword)
    {
        return config.musicbrainzSupport.password.equals(newPassword);
    }

    public void addClientProfile(UiClientConfig clientConfig)
    {
        deleteProfileInternal(clientConfig);
        config.clientConfig.add(clientConfig);
        writeAndSendConfig();
        log.debug("Client profile added or updated : " + clientConfig);
        publisher.publishEvent(new ToastrMessage(null, "info", "client profile", "profile added or updated : " + clientConfig.clientName));
    }

    public boolean deleteClientProfile(UiClientConfig clientConfig)
    {
        deleteProfileInternal(clientConfig);
        writeAndSendConfig();
        return true;
    }

    /**
     * @param clientConfig
     *            Profile to delete
     * @return return TRUE if profile is deleted else FALSE
     */
    private boolean deleteProfileInternal(UiClientConfig clientConfig)
    {
        if (config.clientConfig != null && config.clientConfig.size() > 0)
        {
            if (!config.clientConfig.removeIf(e -> e.uuid.contentEquals(clientConfig.uuid)))
            {
                log.debug("Client config not found : " + clientConfig);
                return false;
            }
        }
        return false;
    }

    public void writeAndSendConfig()
    {
        persistence.writeConfig();
        ssePublisher.sendObjectAsJson(CONFIG_QUEUENAME, config);
    }

    @Bean
    public ISpotifyConfig spotifyConfigProducer()
    {
        return new ISpotifyConfig()
        {
            @Override
            public String getSpotifyRefreshToken()
            {
                return config.spotifyConfig.refreshToken;
            }

            @Override
            public String getClientId()
            {
                return config.spotifyConfig.clientId;
            }

            @Override
            public void setSpotifyRefreshToken(String currentToken)
            {
                config.spotifyConfig.refreshToken = currentToken;
                writeAndSendConfig();
            }

            @Override
            public void setUserAuthorizationNeeded(boolean authNeeded)
            {
                config.spotifyConfig.accountConnected = !authNeeded;
                writeAndSendConfig();
            }

            @Override
            public boolean userIsAuthorized()
            {
                return config.spotifyConfig.accountConnected;
            }

            @Override
            public String getRedirectUrl()
            {
                return config.spotifyConfig.redirectUrl;
            }
        };
    }

}
