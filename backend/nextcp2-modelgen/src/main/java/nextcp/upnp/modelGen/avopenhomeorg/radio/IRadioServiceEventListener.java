package nextcp.upnp.modelGen.avopenhomeorg.radio;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface IRadioServiceEventListener extends ISubscriptionEventListener 
{
    public void metadataChange(String value);
    
    public void idArrayChange(byte[] value);
    
    public void transportStateChange(String value);
    
    public void uriChange(String value);
    
    public void protocolInfoChange(String value);
    
    public void channelsMaxChange(Long value);
    
    public void idChange(Long value);
    
}
