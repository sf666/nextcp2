package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
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
    public List<RendererDeviceConfiguration> rendererDevices;
    public LocalIndexSupport localIndexerSupport;
    public MusicbrainzSupport musicbrainzSupport;
    public RatingStrategy ratingStrategy;

    public Config()
    {
    }

    public Config(Boolean generateUpnpCode, String generateUpnpCodePath, String libraryPath, Integer embeddedServerPort, Long sseEmitterTimeout, String log4jConfigFile, String loggingDateTimeFormat, List<UiClientConfig> clientConfig, List<RadioStation> radioStation, List<RendererDeviceConfiguration> rendererDevices, LocalIndexSupport localIndexerSupport, MusicbrainzSupport musicbrainzSupport, RatingStrategy ratingStrategy)
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
        this.rendererDevices = rendererDevices;
        this.localIndexerSupport = localIndexerSupport;
        this.musicbrainzSupport = musicbrainzSupport;
        this.ratingStrategy = ratingStrategy;
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
        sb.append("rendererDevices=").append(this.rendererDevices).append(", ");
        sb.append("localIndexerSupport=").append(this.localIndexerSupport).append(", ");
        sb.append("musicbrainzSupport=").append(this.musicbrainzSupport).append(", ");
        sb.append("ratingStrategy=").append(this.ratingStrategy).append(", ");
        sb.append("]");
        return sb.toString();
    }

}