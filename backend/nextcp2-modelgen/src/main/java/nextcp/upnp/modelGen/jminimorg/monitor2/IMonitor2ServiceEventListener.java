package nextcp.upnp.modelGen.jminimorg.monitor2;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface IMonitor2ServiceEventListener extends ISubscriptionEventListener 
{
    public void allContextStatusChange(String value);
    
    public void allComponentStatusChange(String value);
    
    public void propertyUpdatesChange(String value);
    
    public void logDataLengthChange(Long value);
    
}
