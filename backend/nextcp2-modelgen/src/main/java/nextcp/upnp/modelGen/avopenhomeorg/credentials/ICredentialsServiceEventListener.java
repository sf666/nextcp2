package nextcp.upnp.modelGen.avopenhomeorg.credentials;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface ICredentialsServiceEventListener extends ISubscriptionEventListener 
{
    public void publicKeyChange(String value);
    
    public void sequenceNumberChange(Long value);
    
    public void idsChange(String value);
    
}
