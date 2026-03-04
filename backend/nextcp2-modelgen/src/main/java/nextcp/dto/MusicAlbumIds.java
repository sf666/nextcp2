package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MusicAlbumIds
{

    public String musicBrainzAlbumId;
    public Long discogsReleaseId;

    public MusicAlbumIds()
    {
    }

    public MusicAlbumIds(String musicBrainzAlbumId, Long discogsReleaseId)
    {
        this.musicBrainzAlbumId = musicBrainzAlbumId;
        this.discogsReleaseId = discogsReleaseId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MusicAlbumIds [");
        sb.append("musicBrainzAlbumId=").append(this.musicBrainzAlbumId).append(", ");
        sb.append("discogsReleaseId=").append(this.discogsReleaseId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}