package nextcp.upnp.device.mediarenderer.avtransport;

public class AvTransportState
{
    public Long InstanceId = 0L;
    public String AbsoluteTimePosition = "";
    public String CurrentTrackURI = "";
    public String CurrentTrackMetaData = "";
    public Integer RelativeCounterPosition = 0;
    public String TransportStatus = "";
    public String AVTransportURIMetaData = "";
    public String TransportState = "";
    public Long CurrentTrack = 0L;
    public String PlaybackStorageMedium = "";
    public String PossibleRecordQualityModes = "";
    public String NextAVTransportURIMetaData = "";
    public Long NumberOfTracks = 0L;
    public String CurrentMediaDuration = "";
    public String NextAVTransportURI = "";
    public String RecordStorageMedium = "";
    public String AVTransportURI = "";
    public String TransportPlaySpeed = "";
    public Integer AbsoluteCounterPosition = 0;
    public String RelativeTimePosition = "";
    public String CurrentPlayMode = "";
    public String CurrentTrackDuration = "";
    public String PossiblePlaybackStorageMedia = "";
    public String CurrentRecordQualityMode = "";
    public String RecordMediumWriteStatus = "";
    public String CurrentTransportActions = "";
    public String PossibleRecordStorageMedia = "";
    public String LastChange = "";
    @Override
    public String toString()
    {
        return "AvTransportState [InstanceId=" + InstanceId + ", AbsoluteTimePosition=" + AbsoluteTimePosition + ", CurrentTrackURI=" + CurrentTrackURI + ", CurrentTrackMetaData="
                + CurrentTrackMetaData + ", RelativeCounterPosition=" + RelativeCounterPosition + ", TransportStatus=" + TransportStatus + ", AVTransportURIMetaData="
                + AVTransportURIMetaData + ", TransportState=" + TransportState + ", CurrentTrack=" + CurrentTrack + ", PlaybackStorageMedium=" + PlaybackStorageMedium
                + ", PossibleRecordQualityModes=" + PossibleRecordQualityModes + ", NextAVTransportURIMetaData=" + NextAVTransportURIMetaData + ", NumberOfTracks=" + NumberOfTracks
                + ", CurrentMediaDuration=" + CurrentMediaDuration + ", NextAVTransportURI=" + NextAVTransportURI + ", RecordStorageMedium=" + RecordStorageMedium
                + ", AVTransportURI=" + AVTransportURI + ", TransportPlaySpeed=" + TransportPlaySpeed + ", AbsoluteCounterPosition=" + AbsoluteCounterPosition
                + ", RelativeTimePosition=" + RelativeTimePosition + ", CurrentPlayMode=" + CurrentPlayMode + ", CurrentTrackDuration=" + CurrentTrackDuration
                + ", PossiblePlaybackStorageMedia=" + PossiblePlaybackStorageMedia + ", CurrentRecordQualityMode=" + CurrentRecordQualityMode + ", RecordMediumWriteStatus="
                + RecordMediumWriteStatus + ", CurrentTransportActions=" + CurrentTransportActions + ", PossibleRecordStorageMedia=" + PossibleRecordStorageMedia + ", LastChange="
                + LastChange + "]";
    }
    
    
}
