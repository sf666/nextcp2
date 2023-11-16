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

    public String myPlaylistFolderName;
    public Boolean generateUpnpCode;
    public String generateUpnpCodePath;
    public String libraryPath;
    public Integer embeddedServerPort;
    public Long sseEmitterTimeout;
    public String log4jConfigFile;
    public String loggingDateTimeFormat;
    public Long globalSearchDelay;
    public String databaseFilename;
    public Long itemsPerPage;
    public Long nextPageAfter;
    public String pathToRestartScript;

    public ApplicationConfig()
    {
    }

    public ApplicationConfig(String myPlaylistFolderName, Boolean generateUpnpCode, String generateUpnpCodePath, String libraryPath, Integer embeddedServerPort, Long sseEmitterTimeout, String log4jConfigFile, String loggingDateTimeFormat, Long globalSearchDelay, String databaseFilename, Long itemsPerPage, Long nextPageAfter, String pathToRestartScript)
    {
        this.myPlaylistFolderName = myPlaylistFolderName;
        this.generateUpnpCode = generateUpnpCode;
        this.generateUpnpCodePath = generateUpnpCodePath;
        this.libraryPath = libraryPath;
        this.embeddedServerPort = embeddedServerPort;
        this.sseEmitterTimeout = sseEmitterTimeout;
        this.log4jConfigFile = log4jConfigFile;
        this.loggingDateTimeFormat = loggingDateTimeFormat;
        this.globalSearchDelay = globalSearchDelay;
        this.databaseFilename = databaseFilename;
        this.itemsPerPage = itemsPerPage;
        this.nextPageAfter = nextPageAfter;
        this.pathToRestartScript = pathToRestartScript;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplicationConfig [");
        sb.append("myPlaylistFolderName=").append(this.myPlaylistFolderName).append(", ");
        sb.append("generateUpnpCode=").append(this.generateUpnpCode).append(", ");
        sb.append("generateUpnpCodePath=").append(this.generateUpnpCodePath).append(", ");
        sb.append("libraryPath=").append(this.libraryPath).append(", ");
        sb.append("embeddedServerPort=").append(this.embeddedServerPort).append(", ");
        sb.append("sseEmitterTimeout=").append(this.sseEmitterTimeout).append(", ");
        sb.append("log4jConfigFile=").append(this.log4jConfigFile).append(", ");
        sb.append("loggingDateTimeFormat=").append(this.loggingDateTimeFormat).append(", ");
        sb.append("globalSearchDelay=").append(this.globalSearchDelay).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("itemsPerPage=").append(this.itemsPerPage).append(", ");
        sb.append("nextPageAfter=").append(this.nextPageAfter).append(", ");
        sb.append("pathToRestartScript=").append(this.pathToRestartScript).append(", ");
        sb.append("]");
        return sb.toString();
    }

}