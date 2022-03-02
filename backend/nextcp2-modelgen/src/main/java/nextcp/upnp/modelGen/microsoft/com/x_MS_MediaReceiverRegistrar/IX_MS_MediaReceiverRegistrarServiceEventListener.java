package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IX_MS_MediaReceiverRegistrarServiceEventListener extends ISubscriptionEventListener 
{
    public void validationSucceededUpdateIDChange(Long value);
    
    public void validationRevokedUpdateIDChange(Long value);
    
    public void authorizationGrantedUpdateIDChange(Long value);
    
    public void authorizationDeniedUpdateIDChange(Long value);
    
}
