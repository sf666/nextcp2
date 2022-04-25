package nextcp.upnp.modelGen.avopenhomeorg.transport1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface ITransportServiceEventListener extends ISubscriptionEventListener 
{
    public void modesChange(String value);
    
    public void streamIdChange(Long value);
    
    public void shuffleChange(Boolean value);
    
    public void repeatChange(Boolean value);
    
    public void canSkipNextChange(Boolean value);
    
    public void canShuffleChange(Boolean value);
    
    public void transportStateChange(String value);
    
    public void canRepeatChange(Boolean value);
    
    public void canPauseChange(Boolean value);
    
    public void canSeekChange(Boolean value);
    
    public void canSkipPreviousChange(Boolean value);
    
}
