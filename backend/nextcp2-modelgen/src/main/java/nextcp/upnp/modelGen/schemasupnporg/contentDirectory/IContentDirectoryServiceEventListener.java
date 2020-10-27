package nextcp.upnp.modelGen.schemasupnporg.contentDirectory;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface IContentDirectoryServiceEventListener extends ISubscriptionEventListener 
{
    public void systemUpdateIDChange(Long value);
    
    public void containerUpdateIDsChange(String value);
    
    public void transferIDsChange(String value);
    
}
