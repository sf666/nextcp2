package nextcp.upnp.modelGen.avopenhomeorg.time1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface ITimeServiceEventListener extends ISubscriptionEventListener 
{
    public void durationChange(Long value);
    
    public void secondsChange(Long value);
    
    public void trackCountChange(Long value);
    
}
