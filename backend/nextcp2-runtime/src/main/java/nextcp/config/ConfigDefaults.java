package nextcp.config;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.Config;
import nextcp.dto.MusicbrainzSupport;
import nextcp.dto.SpotifyConfigDto;

public class ConfigDefaults
{
    private static final Logger log = LoggerFactory.getLogger(ConfigDefaults.class.getName());
    
    public void applyDefaults(Config config)
    {
        if (config.spotifyConfig == null)
        {
            config.spotifyConfig = new SpotifyConfigDto();
            config.spotifyConfig.clientId = "07c3ea9a85b045b09f0dea60b83fb949";
        }
        else if (StringUtils.isBlank(config.spotifyConfig.clientId))
        {
            config.spotifyConfig.clientId = "07c3ea9a85b045b09f0dea60b83fb949";
        }
        if (config.clientConfig == null)
        {
            log.info("adding new configuration value 'clientConfig'. List is empty.");
            config.clientConfig = new ArrayList<>();
        }

        if (config.radioStation == null)
        {
            log.info("adding new configuration value 'radioStation'. List is empty.");
            config.radioStation = new ArrayList<>();
        }

        if (config.generateUpnpCode == null)
        {
            log.info("adding new configuration value 'generateUpnpCode' as disabled.");
            config.generateUpnpCode = false;
        }

        if (config.musicbrainzSupport == null)
        {
            log.info("adding new configuration value 'musicbrainzSupport' as disabled. To activate this feature, provide username and password.");
            config.musicbrainzSupport = new MusicbrainzSupport("", "");
        }

        if (config.globalSearchDelay == null)
        {
            log.info("adding new configuration value 'globalSearchDelay = 500'");
            config.globalSearchDelay = Long.valueOf(500);
        }

        if (config.umsApiKeys == null)
        {
            log.info("adding new configuration value 'umsApiKeys = []'");
            config.umsApiKeys = new ArrayList<>();
        }
    }

}
