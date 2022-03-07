package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MusicBrainzId
{

    public String AlbumId;
    public String ArtistId;
    public String ReleaseTrackId;
    public String AlbumArtistId;
    public String WorkId;
    public String TrackId;

    public MusicBrainzId()
    {
    }

    public MusicBrainzId(String AlbumId, String ArtistId, String ReleaseTrackId, String AlbumArtistId, String WorkId, String TrackId)
    {
        this.AlbumId = AlbumId;
        this.ArtistId = ArtistId;
        this.ReleaseTrackId = ReleaseTrackId;
        this.AlbumArtistId = AlbumArtistId;
        this.WorkId = WorkId;
        this.TrackId = TrackId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MusicBrainzId [");
        sb.append("AlbumId=").append(this.AlbumId).append(", ");
        sb.append("ArtistId=").append(this.ArtistId).append(", ");
        sb.append("ReleaseTrackId=").append(this.ReleaseTrackId).append(", ");
        sb.append("AlbumArtistId=").append(this.AlbumArtistId).append(", ");
        sb.append("WorkId=").append(this.WorkId).append(", ");
        sb.append("TrackId=").append(this.TrackId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}