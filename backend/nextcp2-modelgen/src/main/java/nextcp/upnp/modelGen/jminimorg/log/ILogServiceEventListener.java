package nextcp.upnp.modelGen.jminimorg.log;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface ILogServiceEventListener extends ISubscriptionEventListener 
{
    public void logStartChange(Long value);
    
    public void logDataLengthChange(Long value);
    
    public void logLevelChange(String value);
    
}
