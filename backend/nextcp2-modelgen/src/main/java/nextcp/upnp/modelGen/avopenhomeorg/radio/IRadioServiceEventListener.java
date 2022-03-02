package nextcp.upnp.modelGen.avopenhomeorg.radio;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IRadioServiceEventListener extends ISubscriptionEventListener 
{
    public void relativeChange(Integer value);
    
    public void idArrayChangedChange(Boolean value);
    
    public void metadataChange(String value);
    
    public void idArrayChange(byte[] value);
    
    public void transportStateChange(String value);
    
    public void absoluteChange(Long value);
    
    public void uriChange(String value);
    
    public void idListChange(String value);
    
    public void idArrayTokenChange(Long value);
    
    public void protocolInfoChange(String value);
    
    public void channelsMaxChange(Long value);
    
    public void idChange(Long value);
    
    public void channelListChange(String value);
    
}
