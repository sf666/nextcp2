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
    public void transferIDsChange(String value);
    
    public void systemUpdateIDChange(Long value);
    
    public void sortCapabilitiesChange(String value);
    
    public void x_FeatureListChange(String value);
    
    public void sortExtensionCapabilitiesChange(String value);
    
    public void containerUpdateIDsChange(String value);
    
    public void searchCapabilitiesChange(String value);
    
    public void featureListChange(String value);
    
}
