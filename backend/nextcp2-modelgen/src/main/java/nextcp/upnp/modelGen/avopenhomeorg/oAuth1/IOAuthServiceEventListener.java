package nextcp.upnp.modelGen.avopenhomeorg.oAuth1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IOAuthServiceEventListener extends ISubscriptionEventListener 
{
    public void publicKeyChange(String value);
    
    public void supportedServicesChange(String value);
    
    public void updateIdChange(Long value);
    
}
