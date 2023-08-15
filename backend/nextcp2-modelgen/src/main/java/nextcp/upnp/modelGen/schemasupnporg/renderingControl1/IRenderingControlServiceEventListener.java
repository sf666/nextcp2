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
    public void loudnessChange(Boolean value);
    
    public void brightnessChange(Long value);
    
    public void greenVideoBlackLevelChange(Long value);
    
    public void blueVideoBlackLevelChange(Long value);
    
    public void presetNameListChange(String value);
    
    public void contrastChange(Long value);
    
    public void sharpnessChange(Long value);
    
    public void greenVideoGainChange(Long value);
    
    public void colorTemperatureChange(Long value);
    
    public void horizontalKeystoneChange(Integer value);
    
    public void volumeChange(Long value);
    
    public void lastChangeChange(String value);
    
    public void blueVideoGainChange(Long value);
    
    public void redVideoGainChange(Long value);
    
    public void verticalKeystoneChange(Integer value);
    
    public void redVideoBlackLevelChange(Long value);
    
    public void muteChange(Boolean value);
    
    public void volumeDBChange(Integer value);
    
}
