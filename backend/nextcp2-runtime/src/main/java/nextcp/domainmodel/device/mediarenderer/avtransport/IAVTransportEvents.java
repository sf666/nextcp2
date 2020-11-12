package nextcp.domainmodel.device.mediarenderer.avtransport;

/**
 * Available AVTransport change events
 */
public interface IAVTransportEvents
{
    public void transportStatusChange(String transportStatus);
    public void transportPlaySpeedChange(String transportPlaySpeed);
    public void relativeTimePositionChange(String relativeTimePosition);
    public void relativeCounterPositionChange(Integer relativeCounterPosition);
    public void recordStorageMediumChange(String recordStorageMedium);
    public void recordMediumWriteStatusChange(String recordMediumWriteStatus);
    public void possibleRecordStorageMediaChange(String possibleRecordStorageMedia);
    public void possibleRecordQualityModesChange(String possibleRecordQualityModes);
    public void possiblePlaybackStorageMediaChange(String possiblePlaybackStorageMedia);
    public void playbackStorageMediumChange(String playbackStorageMedium);
    public void numberOfTracksChange(Long numberOfTracks);
    public void nextAVTransportURIMetaDataChange(String nextAVTransportURIMetaData);
    public void nextAVTransportURIChange(String nextAVTransportURI);
    public void currentTransportActionsChange(String currentTransportActions);
    public void currentTrackMetaDataChange(String currentTrackMetaData);
    public void currentTrackDurationChange(String currentTrackDuration);
    public void currentRecordQualityModeChange(String currentRecordQualityMode);
    public void currentTrackChange(Long currentTrack);
    public void currentPlayModeChange(String currentPlayMode);
    public void currentMediaDurationChange(String currentMediaDuration);
    public void aVTransportURIMetaDataChange(String aVTransportURIMetaData);
    public void aVTransportURIChange(String aVTransportURI);
    public void absoluteTimePositionChange(String absoluteTimePosition);
    public void absoluteCounterPositionChange(Integer absoluteCounterPosition);
    public void currentTrackURIChange(String value);
    public void transportStateChange(String value);
    public void processingFinished(AvTransportState currentAvTransportState);
}
