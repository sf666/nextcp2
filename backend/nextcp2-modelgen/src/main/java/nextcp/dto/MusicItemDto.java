package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class MusicItemDto
{

    public String mediaServerUDN;
    public String streamingURL;
    public String objectID;
    public String objectClass;
    public String parentId;
    public String refId;
    public String currentTrackMetadata;
    public String creator;
    public String title;
    public String artistName;
    public String numberOfThisDisc;
    public String originalTrackNumber;
    public String album;
    public String date;
    public AudioFormat audioFormat;
    public String albumArtUrl;
    public Integer rating;

    public MusicItemDto()
    {
    }

    public MusicItemDto(String mediaServerUDN, String streamingURL, String objectID, String objectClass, String parentId, String refId, String currentTrackMetadata, String creator, String title, String artistName, String numberOfThisDisc, String originalTrackNumber, String album, String date, AudioFormat audioFormat, String albumArtUrl, Integer rating)
    {
        this.mediaServerUDN = mediaServerUDN;
        this.streamingURL = streamingURL;
        this.objectID = objectID;
        this.objectClass = objectClass;
        this.parentId = parentId;
        this.refId = refId;
        this.currentTrackMetadata = currentTrackMetadata;
        this.creator = creator;
        this.title = title;
        this.artistName = artistName;
        this.numberOfThisDisc = numberOfThisDisc;
        this.originalTrackNumber = originalTrackNumber;
        this.album = album;
        this.date = date;
        this.audioFormat = audioFormat;
        this.albumArtUrl = albumArtUrl;
        this.rating = rating;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MusicItemDto [");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("streamingURL=").append(this.streamingURL).append(", ");
        sb.append("objectID=").append(this.objectID).append(", ");
        sb.append("objectClass=").append(this.objectClass).append(", ");
        sb.append("parentId=").append(this.parentId).append(", ");
        sb.append("refId=").append(this.refId).append(", ");
        sb.append("currentTrackMetadata=").append(this.currentTrackMetadata).append(", ");
        sb.append("creator=").append(this.creator).append(", ");
        sb.append("title=").append(this.title).append(", ");
        sb.append("artistName=").append(this.artistName).append(", ");
        sb.append("numberOfThisDisc=").append(this.numberOfThisDisc).append(", ");
        sb.append("originalTrackNumber=").append(this.originalTrackNumber).append(", ");
        sb.append("album=").append(this.album).append(", ");
        sb.append("date=").append(this.date).append(", ");
        sb.append("audioFormat=").append(this.audioFormat).append(", ");
        sb.append("albumArtUrl=").append(this.albumArtUrl).append(", ");
        sb.append("rating=").append(this.rating).append(", ");
        sb.append("]");
        return sb.toString();
    }

}