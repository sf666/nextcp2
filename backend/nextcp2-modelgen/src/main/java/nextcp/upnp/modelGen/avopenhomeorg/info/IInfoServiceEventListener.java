package nextcp.upnp.modelGen.avopenhomeorg.info;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface IInfoServiceEventListener extends ISubscriptionEventListener 
{
    public void detailsCountChange(Long value);
    
    public void metatextCountChange(Long value);
    
    public void sampleRateChange(Long value);
    
    public void metadataChange(String value);
    
    public void durationChange(Long value);
    
    public void trackCountChange(Long value);
    
    public void bitRateChange(Long value);
    
    public void uriChange(String value);
    
    public void codecNameChange(String value);
    
    public void losslessChange(Boolean value);
    
    public void bitDepthChange(Long value);
    
    public void metatextChange(String value);
    
}
