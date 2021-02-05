package nextcp.upnp.modelGen.schemasupnporg.renderingControl;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface IRenderingControlServiceEventListener extends ISubscriptionEventListener 
{
    public void x_SubtitleChange(String value);
    
    public void volumeChange(Long value);
    
    public void x_Current3DFormatterChange(String value);
    
    public void lastChangeChange(String value);
    
    public void x_Possible3DFormatterChange(String value);
    
    public void presetNameListChange(String value);
    
    public void muteChange(Boolean value);
    
    public void volumeDBChange(Integer value);
    
}
