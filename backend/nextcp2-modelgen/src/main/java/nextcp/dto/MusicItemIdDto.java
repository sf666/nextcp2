package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MusicItemIdDto
{

    public String acoustID;
    public String musicBrainzIdTrackId;
    public String objectID;

    public MusicItemIdDto()
    {
    }

    public MusicItemIdDto(String acoustID, String musicBrainzIdTrackId, String objectID)
    {
        this.acoustID = acoustID;
        this.musicBrainzIdTrackId = musicBrainzIdTrackId;
        this.objectID = objectID;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MusicItemIdDto [");
        sb.append("acoustID=").append(this.acoustID).append(", ");
        sb.append("musicBrainzIdTrackId=").append(this.musicBrainzIdTrackId).append(", ");
        sb.append("objectID=").append(this.objectID).append(", ");
        sb.append("]");
        return sb.toString();
    }

}