package nextcp.domainmodel.device.services;

public interface IUpnpAvTransport
{

    /**
     * Starts playing
     */
    void play();

    void setUrl(String currentUri, String metadata);

    void pause();

    void play(String uri, String metaData);
    
    void stop();

    void setNextUrl(String streamingURL, String trackMetadata);

    void next();

    void playNext(String streamUrl, String streamMetadata);
    
}