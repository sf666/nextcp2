package nextcp.upnp.modelGen.avopenhomeorg.sender1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface ISenderServiceEventListener extends ISubscriptionEventListener 
{
    public void statusChange(String value);
    
    public void presentationUrlChange(String value);
    
    public void attributesChange(String value);
    
    public void metadataChange(String value);
    
    public void audioChange(Boolean value);
    
}
