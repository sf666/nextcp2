package nextcp.upnp.modelGen.avopenhomeorg.volume1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IVolumeServiceEventListener extends ISubscriptionEventListener 
{
    public void volumeStepsChange(Long value);
    
    public void fadeMaxChange(Long value);
    
    public void volumeLimitChange(Long value);
    
    public void volumeChange(Long value);
    
    public void balanceMaxChange(Long value);
    
    public void volumeMilliDbPerStepChange(Long value);
    
    public void volumeMaxChange(Long value);
    
    public void balanceChange(Integer value);
    
    public void volumeUnityChange(Long value);
    
    public void muteChange(Boolean value);
    
    public void fadeChange(Integer value);
    
}
