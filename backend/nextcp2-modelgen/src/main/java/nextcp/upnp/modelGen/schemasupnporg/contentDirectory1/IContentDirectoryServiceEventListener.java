package nextcp.upnp.modelGen.schemasupnporg.contentDirectory1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IContentDirectoryServiceEventListener extends ISubscriptionEventListener 
{
    public void transferIDsChange(String value);
    
    public void systemUpdateIDChange(Long value);
    
    public void sortCapabilitiesChange(String value);
    
    public void containerUpdateIDsChange(String value);
    
    public void searchCapabilitiesChange(String value);
    
}
