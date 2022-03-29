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
    public Integer umsAudiotrackId;

    public MusicItemIdDto()
    {
    }

    public MusicItemIdDto(String acoustID, String musicBrainzIdTrackId, Integer umsAudiotrackId)
    {
        this.acoustID = acoustID;
        this.musicBrainzIdTrackId = musicBrainzIdTrackId;
        this.umsAudiotrackId = umsAudiotrackId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MusicItemIdDto [");
        sb.append("acoustID=").append(this.acoustID).append(", ");
        sb.append("musicBrainzIdTrackId=").append(this.musicBrainzIdTrackId).append(", ");
        sb.append("umsAudiotrackId=").append(this.umsAudiotrackId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}