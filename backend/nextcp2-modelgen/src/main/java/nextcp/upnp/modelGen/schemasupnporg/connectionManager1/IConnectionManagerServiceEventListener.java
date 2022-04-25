package nextcp.upnp.modelGen.schemasupnporg.connectionManager1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IConnectionManagerServiceEventListener extends ISubscriptionEventListener 
{
    public void sinkProtocolInfoChange(String value);
    
    public void currentConnectionIDsChange(String value);
    
    public void sourceProtocolInfoChange(String value);
    
}
