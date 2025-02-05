package nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IUmsExtendedServicesServiceEventListener extends ISubscriptionEventListener 
{
    public void anonymousDevicesWriteChange(Boolean value);
    
    public void audioLikesVisibleRootChange(Boolean value);
    
    public void upnpCdsWriteChange(Boolean value);
    
    public void audioUpdateRatingTagChange(Boolean value);
    
}
