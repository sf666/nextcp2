package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class UpnpAvTransportState
{

    public MediaRendererDto mediaRenderer;
    public String AbsoluteTimePosition;
    public String CurrentTrackURI;
    public MusicItemDto CurrentTrackMetaData;
    public Integer RelativeCounterPosition;
    public String TransportStatus;
    public MusicItemDto AVTransportURIMetaData;
    public String TransportState;
    public Long CurrentTrack;
    public String PlaybackStorageMedium;
    public String PossibleRecordQualityModes;
    public String NextAVTransportURIMetaData;
    public Long NumberOfTracks;
    public String CurrentMediaDuration;
    public String NextAVTransportURI;
    public String RecordStorageMedium;
    public String AVTransportURI;
    public String TransportPlaySpeed;
    public Integer AbsoluteCounterPosition;
    public String RelativeTimePosition;
    public String CurrentPlayMode;
    public String CurrentTrackDuration;
    public String PossiblePlaybackStorageMedia;
    public String CurrentRecordQualityMode;
    public String RecordMediumWriteStatus;
    public String CurrentTransportActions;
    public String PossibleRecordStorageMedia;

    public UpnpAvTransportState()
    {
    }

    public UpnpAvTransportState(MediaRendererDto mediaRenderer, String AbsoluteTimePosition, String CurrentTrackURI, MusicItemDto CurrentTrackMetaData, Integer RelativeCounterPosition, String TransportStatus, MusicItemDto AVTransportURIMetaData, String TransportState, Long CurrentTrack, String PlaybackStorageMedium, String PossibleRecordQualityModes, String NextAVTransportURIMetaData, Long NumberOfTracks, String CurrentMediaDuration, String NextAVTransportURI, String RecordStorageMedium, String AVTransportURI, String TransportPlaySpeed, Integer AbsoluteCounterPosition, String RelativeTimePosition, String CurrentPlayMode, String CurrentTrackDuration, String PossiblePlaybackStorageMedia, String CurrentRecordQualityMode, String RecordMediumWriteStatus, String CurrentTransportActions, String PossibleRecordStorageMedia)
    {
        this.mediaRenderer = mediaRenderer;
        this.AbsoluteTimePosition = AbsoluteTimePosition;
        this.CurrentTrackURI = CurrentTrackURI;
        this.CurrentTrackMetaData = CurrentTrackMetaData;
        this.RelativeCounterPosition = RelativeCounterPosition;
        this.TransportStatus = TransportStatus;
        this.AVTransportURIMetaData = AVTransportURIMetaData;
        this.TransportState = TransportState;
        this.CurrentTrack = CurrentTrack;
        this.PlaybackStorageMedium = PlaybackStorageMedium;
        this.PossibleRecordQualityModes = PossibleRecordQualityModes;
        this.NextAVTransportURIMetaData = NextAVTransportURIMetaData;
        this.NumberOfTracks = NumberOfTracks;
        this.CurrentMediaDuration = CurrentMediaDuration;
        this.NextAVTransportURI = NextAVTransportURI;
        this.RecordStorageMedium = RecordStorageMedium;
        this.AVTransportURI = AVTransportURI;
        this.TransportPlaySpeed = TransportPlaySpeed;
        this.AbsoluteCounterPosition = AbsoluteCounterPosition;
        this.RelativeTimePosition = RelativeTimePosition;
        this.CurrentPlayMode = CurrentPlayMode;
        this.CurrentTrackDuration = CurrentTrackDuration;
        this.PossiblePlaybackStorageMedia = PossiblePlaybackStorageMedia;
        this.CurrentRecordQualityMode = CurrentRecordQualityMode;
        this.RecordMediumWriteStatus = RecordMediumWriteStatus;
        this.CurrentTransportActions = CurrentTransportActions;
        this.PossibleRecordStorageMedia = PossibleRecordStorageMedia;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("UpnpAvTransportState [");
        sb.append("mediaRenderer=").append(this.mediaRenderer).append(", ");
        sb.append("AbsoluteTimePosition=").append(this.AbsoluteTimePosition).append(", ");
        sb.append("CurrentTrackURI=").append(this.CurrentTrackURI).append(", ");
        sb.append("CurrentTrackMetaData=").append(this.CurrentTrackMetaData).append(", ");
        sb.append("RelativeCounterPosition=").append(this.RelativeCounterPosition).append(", ");
        sb.append("TransportStatus=").append(this.TransportStatus).append(", ");
        sb.append("AVTransportURIMetaData=").append(this.AVTransportURIMetaData).append(", ");
        sb.append("TransportState=").append(this.TransportState).append(", ");
        sb.append("CurrentTrack=").append(this.CurrentTrack).append(", ");
        sb.append("PlaybackStorageMedium=").append(this.PlaybackStorageMedium).append(", ");
        sb.append("PossibleRecordQualityModes=").append(this.PossibleRecordQualityModes).append(", ");
        sb.append("NextAVTransportURIMetaData=").append(this.NextAVTransportURIMetaData).append(", ");
        sb.append("NumberOfTracks=").append(this.NumberOfTracks).append(", ");
        sb.append("CurrentMediaDuration=").append(this.CurrentMediaDuration).append(", ");
        sb.append("NextAVTransportURI=").append(this.NextAVTransportURI).append(", ");
        sb.append("RecordStorageMedium=").append(this.RecordStorageMedium).append(", ");
        sb.append("AVTransportURI=").append(this.AVTransportURI).append(", ");
        sb.append("TransportPlaySpeed=").append(this.TransportPlaySpeed).append(", ");
        sb.append("AbsoluteCounterPosition=").append(this.AbsoluteCounterPosition).append(", ");
        sb.append("RelativeTimePosition=").append(this.RelativeTimePosition).append(", ");
        sb.append("CurrentPlayMode=").append(this.CurrentPlayMode).append(", ");
        sb.append("CurrentTrackDuration=").append(this.CurrentTrackDuration).append(", ");
        sb.append("PossiblePlaybackStorageMedia=").append(this.PossiblePlaybackStorageMedia).append(", ");
        sb.append("CurrentRecordQualityMode=").append(this.CurrentRecordQualityMode).append(", ");
        sb.append("RecordMediumWriteStatus=").append(this.RecordMediumWriteStatus).append(", ");
        sb.append("CurrentTransportActions=").append(this.CurrentTransportActions).append(", ");
        sb.append("PossibleRecordStorageMedia=").append(this.PossibleRecordStorageMedia).append(", ");
        sb.append("]");
        return sb.toString();
    }

}