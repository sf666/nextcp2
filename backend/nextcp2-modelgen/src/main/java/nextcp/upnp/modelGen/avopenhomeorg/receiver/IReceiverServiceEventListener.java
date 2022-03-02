package nextcp.upnp.modelGen.avopenhomeorg.receiver;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IReceiverServiceEventListener extends ISubscriptionEventListener 
{
    public void metadataChange(String value);
    
    public void protocolInfoChange(String value);
    
    public void transportStateChange(String value);
    
    public void uriChange(String value);
    
}
