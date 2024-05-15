package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class UpdateStarRatingRequest
{

    public Integer previousRating;
    public Integer newRating;
    public String mediaServerDevice;
    public MusicItemIdDto musicItemIdDto;

    public UpdateStarRatingRequest()
    {
    }

    public UpdateStarRatingRequest(Integer previousRating, Integer newRating, String mediaServerDevice, MusicItemIdDto musicItemIdDto)
    {
        this.previousRating = previousRating;
        this.newRating = newRating;
        this.mediaServerDevice = mediaServerDevice;
        this.musicItemIdDto = musicItemIdDto;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateStarRatingRequest [");
        sb.append("previousRating=").append(this.previousRating).append(", ");
        sb.append("newRating=").append(this.newRating).append(", ");
        sb.append("mediaServerDevice=").append(this.mediaServerDevice).append(", ");
        sb.append("musicItemIdDto=").append(this.musicItemIdDto).append(", ");
        sb.append("]");
        return sb.toString();
    }

}