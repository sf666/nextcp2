package nextcp.dto;

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
    public Integer embeddedServerSslPort;
    public String embeddedServerSslP12Keystore;
    public String embeddedServerSslP12KeystorePassword;
    public Long sseEmitterTimeout;
    public String log4jConfigFile;
    public String loggingDateTimeFormat;
    public Long globalSearchDelay;
    public String databaseFilename;
    public Long itemsPerPage;
    public Long nextPageAfter;
    public String pathToRestartScript;
    public String upnpBindInterface;

    public ApplicationConfig()
    {
    }

    public ApplicationConfig(String myPlaylistFolderName, Boolean generateUpnpCode, String generateUpnpCodePath, String libraryPath, Integer embeddedServerPort, Integer embeddedServerSslPort, String embeddedServerSslP12Keystore, String embeddedServerSslP12KeystorePassword, Long sseEmitterTimeout, String log4jConfigFile, String loggingDateTimeFormat, Long globalSearchDelay, String databaseFilename, Long itemsPerPage, Long nextPageAfter, String pathToRestartScript, String upnpBindInterface)
    {
        this.myPlaylistFolderName = myPlaylistFolderName;
        this.generateUpnpCode = generateUpnpCode;
        this.generateUpnpCodePath = generateUpnpCodePath;
        this.libraryPath = libraryPath;
        this.embeddedServerPort = embeddedServerPort;
        this.embeddedServerSslPort = embeddedServerSslPort;
        this.embeddedServerSslP12Keystore = embeddedServerSslP12Keystore;
        this.embeddedServerSslP12KeystorePassword = embeddedServerSslP12KeystorePassword;
        this.sseEmitterTimeout = sseEmitterTimeout;
        this.log4jConfigFile = log4jConfigFile;
        this.loggingDateTimeFormat = loggingDateTimeFormat;
        this.globalSearchDelay = globalSearchDelay;
        this.databaseFilename = databaseFilename;
        this.itemsPerPage = itemsPerPage;
        this.nextPageAfter = nextPageAfter;
        this.pathToRestartScript = pathToRestartScript;
        this.upnpBindInterface = upnpBindInterface;
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
        sb.append("embeddedServerSslPort=").append(this.embeddedServerSslPort).append(", ");
        sb.append("embeddedServerSslP12Keystore=").append(this.embeddedServerSslP12Keystore).append(", ");
        sb.append("embeddedServerSslP12KeystorePassword=").append(this.embeddedServerSslP12KeystorePassword).append(", ");
        sb.append("sseEmitterTimeout=").append(this.sseEmitterTimeout).append(", ");
        sb.append("log4jConfigFile=").append(this.log4jConfigFile).append(", ");
        sb.append("loggingDateTimeFormat=").append(this.loggingDateTimeFormat).append(", ");
        sb.append("globalSearchDelay=").append(this.globalSearchDelay).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("itemsPerPage=").append(this.itemsPerPage).append(", ");
        sb.append("nextPageAfter=").append(this.nextPageAfter).append(", ");
        sb.append("pathToRestartScript=").append(this.pathToRestartScript).append(", ");
        sb.append("upnpBindInterface=").append(this.upnpBindInterface).append(", ");
        sb.append("]");
        return sb.toString();
    }

}