package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar;

import nextcp.upnp.ISubscriptionEventListener;

public interface IX_MS_MediaReceiverRegistrarServiceEventListener extends ISubscriptionEventListener 
{
    public void validationRevokedUpdateIDChange(Long value);
    
    public void validationSucceededUpdateIDChange(Long value);
    
    public void authorizationDeniedUpdateIDChange(Long value);
    
    public void authorizationGrantedUpdateIDChange(Long value);
    
}
