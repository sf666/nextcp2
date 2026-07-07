package nextcp.config;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.dto.AiConfig;
import nextcp.dto.AudioAddictConfig;
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
    	
        if (config.aiConfig == null)
        {
            config.aiConfig = new AiConfig();
            config.aiConfig.aiEnabled = true;
            // Send nextCP's own MCP tools by default (previous behavior).
            config.aiConfig.aiSendTools = true;
            // Conversation memory off by default (each message handled statelessly).
            config.aiConfig.aiConversationMemory = false;
            config.aiConfig.aiProvider = "google";
            config.aiConfig.aiApiKey = "your-api-key";
            config.aiConfig.aiModel = "gemini-3.5-flash";
            // Only used by OpenAI-compatible providers (e.g. openwebui); empty for google.
            // Must include the full path prefix, e.g. http://host:3000/v1 for OpenWebUI.
            config.aiConfig.aiBaseUrl = "";
        }
        else if (StringUtils.isBlank(config.spotifyConfig.clientId))
        {
            config.spotifyConfig.clientId = "07c3ea9a85b045b09f0dea60b83fb949";
        }
        if (config.aiConfig != null && config.aiConfig.aiBaseUrl == null)
        {
            // Backfill for configs written before the OpenAI-compatible provider support was added.
            config.aiConfig.aiBaseUrl = "";
        }
        if (config.aiConfig != null && config.aiConfig.aiSendTools == null)
        {
            // Backfill for configs written before the send-tools switch was added.
            config.aiConfig.aiSendTools = true;
        }
        if (config.aiConfig != null && config.aiConfig.aiConversationMemory == null)
        {
            // Backfill for configs written before the conversation-memory switch was added.
            config.aiConfig.aiConversationMemory = false;
        }
        if (config.aiConfig != null && config.aiConfig.aiProviderProfiles == null)
        {
            // Backfill for configs written before per-provider profiles were added.
            config.aiConfig.aiProviderProfiles = new ArrayList<>();
        }
        if (config.aiConfig != null && "openwebui".equalsIgnoreCase(config.aiConfig.aiProvider))
        {
            // 'openwebui' is reached via the canonical 'openai' provider plus its base URL.
            // Migrate the stored value so the UI shows a single, non-redundant provider option.
            log.info("Migrating legacy aiProvider 'openwebui' to canonical 'openai'.");
            config.aiConfig.aiProvider = "openai";
        }

        if (config.radioStation == null)
        {
            log.info("adding new configuration value 'radioStation'. List is empty.");
            config.radioStation = new ArrayList<>();
        }

        if (config.applicationConfig.generateUpnpCode == null)
        {
            log.info("adding new configuration value 'generateUpnpCode' as disabled.");
            config.applicationConfig.generateUpnpCode = false;
        }

        if (StringUtils.isBlank(config.applicationConfig.loggingConfigFile))
        {
            // Backfill for configs written before the log4j2 -> Logback migration
            // (the former 'log4jConfigFile' key is now 'loggingConfigFile'). Default to the
            // user's home directory (unified, always writable) rather than the working directory.
            String defaultLoggingConfig = new java.io.File(System.getProperty("user.home"), "logback.xml").getPath();
            log.info("adding new configuration value 'loggingConfigFile = {}'", defaultLoggingConfig);
            config.applicationConfig.loggingConfigFile = defaultLoggingConfig;
        }

        if (config.musicbrainzSupport == null)
        {
            log.info("adding new configuration value 'musicbrainzSupport' as disabled. To activate this feature, provide username and password.");
            config.musicbrainzSupport = new MusicbrainzSupport("", "");
        }

        if (config.applicationConfig.globalSearchDelay == null)
        {
            log.info("adding new configuration value 'globalSearchDelay = 500'");
            config.applicationConfig.globalSearchDelay = Long.valueOf(500);
        }

        if (config.applicationConfig.itemsPerPage == null)
        {
            log.info("adding new configuration value 'itemsPerPage = 100'");
            config.applicationConfig.itemsPerPage = Long.valueOf(100);
        }
        if (config.applicationConfig.nextPageAfter == null)
        {
            log.info("adding new configuration value 'nextPageAfter = 60'");
            config.applicationConfig.nextPageAfter = Long.valueOf(60);
        }
        if (config.audioAddictConfig == null)
        {
        	config.audioAddictConfig = new AudioAddictConfig();
            log.info("adding new configuration value 'audioAddictPreferEuropeanServer = true'");
            config.audioAddictConfig.preferEuropeanServer = true;
        }

        if (config.applicationConfig.localPlayerPreTranscodeEnabled == null)
        {
            log.info("adding new configuration value 'localPlayerPreTranscodeEnabled = true'");
            config.applicationConfig.localPlayerPreTranscodeEnabled = true;
        }
        if (config.applicationConfig.localPlayerCacheDir == null)
        {
            log.info("adding new configuration value 'localPlayerCacheDir' (empty = system temp dir)");
            config.applicationConfig.localPlayerCacheDir = "";
        }
        // localPlayerCacheMaxMb is intentionally NOT defaulted: null / empty / <= 0 means "unlimited"
        // (no cache eviction), see LocalStreamProxyService.getMaxCacheBytes().
        if (config.applicationConfig.localPlayerCacheTtlHours == null)
        {
            log.info("adding new configuration value 'localPlayerCacheTtlHours = 24'");
            config.applicationConfig.localPlayerCacheTtlHours = Long.valueOf(24);
        }
    }

}
