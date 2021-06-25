package nextcp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import nextcp.config.ConfigPersistence;
import nextcp.dto.Config;
import nextcp.dto.UiClientConfig;
import nextcp.eventBridge.SsePublisher;
import nextcp.spotify.ISpotifyConfig;
import nextcp.upnp.device.DeviceRegistry;

@Controller
public class ConfigService
{
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class.getName());

    public static String CLIENT_CONFIG_QUEUENAME = "CONFIG_CHANGED";

    @Autowired
    private Config config = null;

    @Autowired
    private ConfigPersistence persistence = null;

    @Autowired
    private SsePublisher ssePublisher = null;

    @Autowired
    private DeviceRegistry deviceRegistry = null;

    public void addClientProfile(UiClientConfig clientConfig)
    {
        try
        {
            if (config.clientConfig != null && config.clientConfig.size() > 0)
            {
                config.clientConfig.removeIf(e -> e.uuid.contentEquals(clientConfig.uuid));
            }
        }
        catch (Exception e)
        {
            log.warn("could not remove element. Client conig:  " + clientConfig, e);
        }
        config.clientConfig.add(clientConfig);
        writeAndSendConfig();
    }

    public void writeAndSendConfig()
    {
        persistence.writeConfig();
        ssePublisher.sendObjectAsJson(CLIENT_CONFIG_QUEUENAME, config);
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
        };
    }
}
