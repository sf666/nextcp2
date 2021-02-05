package nextcp.upnp.modelGen.schemasupnporg.aVTransport;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Event Listener Interface.  
 */
public interface IAVTransportServiceEventListener extends ISubscriptionEventListener 
{
    public void absoluteTimePositionChange(String value);
    
    public void currentTrackURIChange(String value);
    
    public void currentTrackMetaDataChange(String value);
    
    public void relativeCounterPositionChange(Integer value);
    
    public void transportStatusChange(String value);
    
    public void aVTransportURIMetaDataChange(String value);
    
    public void transportStateChange(String value);
    
    public void currentTrackChange(Long value);
    
    public void playbackStorageMediumChange(String value);
    
    public void possibleRecordQualityModesChange(String value);
    
    public void nextAVTransportURIMetaDataChange(String value);
    
    public void numberOfTracksChange(Long value);
    
    public void currentMediaDurationChange(String value);
    
    public void nextAVTransportURIChange(String value);
    
    public void recordStorageMediumChange(String value);
    
    public void aVTransportURIChange(String value);
    
    public void transportPlaySpeedChange(String value);
    
    public void absoluteCounterPositionChange(Integer value);
    
    public void relativeTimePositionChange(String value);
    
    public void currentPlayModeChange(String value);
    
    public void currentTrackDurationChange(String value);
    
    public void possiblePlaybackStorageMediaChange(String value);
    
    public void currentRecordQualityModeChange(String value);
    
    public void recordMediumWriteStatusChange(String value);
    
    public void currentTransportActionsChange(String value);
    
    public void possibleRecordStorageMediaChange(String value);
    
    public void lastChangeChange(String value);
    
}
