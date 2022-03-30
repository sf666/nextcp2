package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class Config
{

    public Boolean generateUpnpCode;
    public String generateUpnpCodePath;
    public String libraryPath;
    public Integer embeddedServerPort;
    public Long sseEmitterTimeout;
    public String log4jConfigFile;
    public String loggingDateTimeFormat;
    public List<UiClientConfig> clientConfig;
    public List<RadioStation> radioStation;
    public MusicbrainzSupport musicbrainzSupport;
    public Long globalSearchDelay;
    public String lastFmSessionKey;
    public SpotifyConfigDto spotifyConfig;
    public List<UmsServerApiKey> umsApiKeys;
    public String databaseFilename;

    public Config()
    {
    }

    public Config(Boolean generateUpnpCode, String generateUpnpCodePath, String libraryPath, Integer embeddedServerPort, Long sseEmitterTimeout, String log4jConfigFile, String loggingDateTimeFormat, List<UiClientConfig> clientConfig, List<RadioStation> radioStation, MusicbrainzSupport musicbrainzSupport, Long globalSearchDelay, String lastFmSessionKey, SpotifyConfigDto spotifyConfig, List<UmsServerApiKey> umsApiKeys, String databaseFilename)
    {
        this.generateUpnpCode = generateUpnpCode;
        this.generateUpnpCodePath = generateUpnpCodePath;
        this.libraryPath = libraryPath;
        this.embeddedServerPort = embeddedServerPort;
        this.sseEmitterTimeout = sseEmitterTimeout;
        this.log4jConfigFile = log4jConfigFile;
        this.loggingDateTimeFormat = loggingDateTimeFormat;
        this.clientConfig = clientConfig;
        this.radioStation = radioStation;
        this.musicbrainzSupport = musicbrainzSupport;
        this.globalSearchDelay = globalSearchDelay;
        this.lastFmSessionKey = lastFmSessionKey;
        this.spotifyConfig = spotifyConfig;
        this.umsApiKeys = umsApiKeys;
        this.databaseFilename = databaseFilename;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Config [");
        sb.append("generateUpnpCode=").append(this.generateUpnpCode).append(", ");
        sb.append("generateUpnpCodePath=").append(this.generateUpnpCodePath).append(", ");
        sb.append("libraryPath=").append(this.libraryPath).append(", ");
        sb.append("embeddedServerPort=").append(this.embeddedServerPort).append(", ");
        sb.append("sseEmitterTimeout=").append(this.sseEmitterTimeout).append(", ");
        sb.append("log4jConfigFile=").append(this.log4jConfigFile).append(", ");
        sb.append("loggingDateTimeFormat=").append(this.loggingDateTimeFormat).append(", ");
        sb.append("clientConfig=").append(this.clientConfig).append(", ");
        sb.append("radioStation=").append(this.radioStation).append(", ");
        sb.append("musicbrainzSupport=").append(this.musicbrainzSupport).append(", ");
        sb.append("globalSearchDelay=").append(this.globalSearchDelay).append(", ");
        sb.append("lastFmSessionKey=").append(this.lastFmSessionKey).append(", ");
        sb.append("spotifyConfig=").append(this.spotifyConfig).append(", ");
        sb.append("umsApiKeys=").append(this.umsApiKeys).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("]");
        return sb.toString();
    }

}