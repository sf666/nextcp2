package nextcp.upnp.modelGen.schemasupnporg.renderingControl1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IRenderingControlServiceEventListener extends ISubscriptionEventListener 
{
    public void minVolumeDBChange(Integer value);
    
    public void volumeChange(Long value);
    
    public void maxVolumeDBChange(Integer value);
    
    public void lastChangeChange(String value);
    
    public void presetNameListChange(String value);
    
    public void muteChange(Boolean value);
    
    public void volumeDBChange(Integer value);
    
}
