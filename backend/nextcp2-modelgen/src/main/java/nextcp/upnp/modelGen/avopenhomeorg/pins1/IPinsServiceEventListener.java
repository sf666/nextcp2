package nextcp.upnp.modelGen.avopenhomeorg.pins1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IPinsServiceEventListener extends ISubscriptionEventListener 
{
    public void idArrayChange(String value);
    
    public void modesChange(String value);
    
    public void accountMaxChange(Long value);
    
    public void deviceMaxChange(Long value);
    
}
