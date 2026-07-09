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
    public Integer embeddedServerSslPort;
    public String embeddedServerSslP12Keystore;
    public String embeddedServerSslP12KeystorePassword;
    public Long sseEmitterTimeout;
    public String loggingConfigFile;
    public Long globalSearchDelay;
    public String databaseFilename;
    public Long itemsPerPage;
    public Long nextPageAfter;
    public String pathToRestartScript;
    public String upnpBindInterface;
    public Integer chatHistorySize;
    public Boolean behindReverseProxy;
    public Integer sseHeartbeatSeconds;
    public Boolean localPlayerPreTranscodeEnabled;
    public String localPlayerCacheDir;
    public Long localPlayerCacheMaxMb;
    public Long localPlayerCacheTtlHours;
    public Boolean showImageItems;

    public ApplicationConfig()
    {
    }

    public ApplicationConfig(Boolean generateUpnpCode, String generateUpnpCodePath, String libraryPath, Integer embeddedServerPort, Integer embeddedServerSslPort, String embeddedServerSslP12Keystore, String embeddedServerSslP12KeystorePassword, Long sseEmitterTimeout, String loggingConfigFile, Long globalSearchDelay, String databaseFilename, Long itemsPerPage, Long nextPageAfter, String pathToRestartScript, String upnpBindInterface, Integer chatHistorySize, Boolean behindReverseProxy, Integer sseHeartbeatSeconds, Boolean localPlayerPreTranscodeEnabled, String localPlayerCacheDir, Long localPlayerCacheMaxMb, Long localPlayerCacheTtlHours, Boolean showImageItems)
    {
        this.generateUpnpCode = generateUpnpCode;
        this.generateUpnpCodePath = generateUpnpCodePath;
        this.libraryPath = libraryPath;
        this.embeddedServerPort = embeddedServerPort;
        this.embeddedServerSslPort = embeddedServerSslPort;
        this.embeddedServerSslP12Keystore = embeddedServerSslP12Keystore;
        this.embeddedServerSslP12KeystorePassword = embeddedServerSslP12KeystorePassword;
        this.sseEmitterTimeout = sseEmitterTimeout;
        this.loggingConfigFile = loggingConfigFile;
        this.globalSearchDelay = globalSearchDelay;
        this.databaseFilename = databaseFilename;
        this.itemsPerPage = itemsPerPage;
        this.nextPageAfter = nextPageAfter;
        this.pathToRestartScript = pathToRestartScript;
        this.upnpBindInterface = upnpBindInterface;
        this.chatHistorySize = chatHistorySize;
        this.behindReverseProxy = behindReverseProxy;
        this.sseHeartbeatSeconds = sseHeartbeatSeconds;
        this.localPlayerPreTranscodeEnabled = localPlayerPreTranscodeEnabled;
        this.localPlayerCacheDir = localPlayerCacheDir;
        this.localPlayerCacheMaxMb = localPlayerCacheMaxMb;
        this.localPlayerCacheTtlHours = localPlayerCacheTtlHours;
        this.showImageItems = showImageItems;
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
        sb.append("embeddedServerSslPort=").append(this.embeddedServerSslPort).append(", ");
        sb.append("embeddedServerSslP12Keystore=").append(this.embeddedServerSslP12Keystore).append(", ");
        sb.append("embeddedServerSslP12KeystorePassword=").append(this.embeddedServerSslP12KeystorePassword).append(", ");
        sb.append("sseEmitterTimeout=").append(this.sseEmitterTimeout).append(", ");
        sb.append("loggingConfigFile=").append(this.loggingConfigFile).append(", ");
        sb.append("globalSearchDelay=").append(this.globalSearchDelay).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("itemsPerPage=").append(this.itemsPerPage).append(", ");
        sb.append("nextPageAfter=").append(this.nextPageAfter).append(", ");
        sb.append("pathToRestartScript=").append(this.pathToRestartScript).append(", ");
        sb.append("upnpBindInterface=").append(this.upnpBindInterface).append(", ");
        sb.append("chatHistorySize=").append(this.chatHistorySize).append(", ");
        sb.append("behindReverseProxy=").append(this.behindReverseProxy).append(", ");
        sb.append("sseHeartbeatSeconds=").append(this.sseHeartbeatSeconds).append(", ");
        sb.append("localPlayerPreTranscodeEnabled=").append(this.localPlayerPreTranscodeEnabled).append(", ");
        sb.append("localPlayerCacheDir=").append(this.localPlayerCacheDir).append(", ");
        sb.append("localPlayerCacheMaxMb=").append(this.localPlayerCacheMaxMb).append(", ");
        sb.append("localPlayerCacheTtlHours=").append(this.localPlayerCacheTtlHours).append(", ");
        sb.append("showImageItems=").append(this.showImageItems).append(", ");
        sb.append("]");
        return sb.toString();
    }

}