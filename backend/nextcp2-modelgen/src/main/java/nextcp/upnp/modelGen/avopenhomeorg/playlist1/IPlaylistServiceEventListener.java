package nextcp.upnp.modelGen.avopenhomeorg.playlist1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IPlaylistServiceEventListener extends ISubscriptionEventListener 
{
    public void relativeChange(Integer value);
    
    public void idArrayChangedChange(Boolean value);
    
    public void tracksMaxChange(Long value);
    
    public void shuffleChange(Boolean value);
    
    public void trackListChange(String value);
    
    public void metadataChange(String value);
    
    public void indexChange(Long value);
    
    public void repeatChange(Boolean value);
    
    public void idArrayChange(byte[] value);
    
    public void absoluteChange(Long value);
    
    public void transportStateChange(String value);
    
    public void uriChange(String value);
    
    public void idListChange(String value);
    
    public void idArrayTokenChange(Long value);
    
    public void protocolInfoChange(String value);
    
    public void idChange(Long value);
    
}
