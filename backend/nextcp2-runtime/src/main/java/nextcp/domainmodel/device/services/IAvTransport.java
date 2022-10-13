package nextcp.domainmodel.device.services;

public interface IAvTransport
{
    void setUrl(String currentUri, String metadata);
    void setNextUrl(String streamingURL, String trackMetadata);
    void playNext(String streamUrl, String streamMetadata);
    void play(String uri, String metaData);    
}
