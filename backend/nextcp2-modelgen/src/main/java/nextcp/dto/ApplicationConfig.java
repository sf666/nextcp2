package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ApplicationConfig
{

    public Boolean generateUpnpCode;
    public String generateUpnpCodePath;
    public String libraryPath;
    public Integer embeddedServerPort;
    public Long sseEmitterTimeout;
    public String log4jConfigFile;
    public String loggingDateTimeFormat;
    public Long globalSearchDelay;
    public String databaseFilename;

    public ApplicationConfig()
    {
    }

    public ApplicationConfig(Boolean generateUpnpCode, String generateUpnpCodePath, String libraryPath, Integer embeddedServerPort, Long sseEmitterTimeout, String log4jConfigFile, String loggingDateTimeFormat, Long globalSearchDelay, String databaseFilename)
    {
        this.generateUpnpCode = generateUpnpCode;
        this.generateUpnpCodePath = generateUpnpCodePath;
        this.libraryPath = libraryPath;
        this.embeddedServerPort = embeddedServerPort;
        this.sseEmitterTimeout = sseEmitterTimeout;
        this.log4jConfigFile = log4jConfigFile;
        this.loggingDateTimeFormat = loggingDateTimeFormat;
        this.globalSearchDelay = globalSearchDelay;
        this.databaseFilename = databaseFilename;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplicationConfig [");
        sb.append("generateUpnpCode=").append(this.generateUpnpCode).append(", ");
        sb.append("generateUpnpCodePath=").append(this.generateUpnpCodePath).append(", ");
        sb.append("libraryPath=").append(this.libraryPath).append(", ");
        sb.append("embeddedServerPort=").append(this.embeddedServerPort).append(", ");
        sb.append("sseEmitterTimeout=").append(this.sseEmitterTimeout).append(", ");
        sb.append("log4jConfigFile=").append(this.log4jConfigFile).append(", ");
        sb.append("loggingDateTimeFormat=").append(this.loggingDateTimeFormat).append(", ");
        sb.append("globalSearchDelay=").append(this.globalSearchDelay).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("]");
        return sb.toString();
    }

}