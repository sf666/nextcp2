package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class RatingStrategy
{

    public Boolean updateMusicBrainzRating;
    public Boolean updateLocalFileRating;
    public Boolean updateUmsServerRating;
    public Boolean syncRatings;
    public String collisionStrategy;

    public RatingStrategy()
    {
    }

    public RatingStrategy(Boolean updateMusicBrainzRating, Boolean updateLocalFileRating, Boolean updateUmsServerRating, Boolean syncRatings, String collisionStrategy)
    {
        this.updateMusicBrainzRating = updateMusicBrainzRating;
        this.updateLocalFileRating = updateLocalFileRating;
        this.updateUmsServerRating = updateUmsServerRating;
        this.syncRatings = syncRatings;
        this.collisionStrategy = collisionStrategy;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RatingStrategy [");
        sb.append("updateMusicBrainzRating=").append(this.updateMusicBrainzRating).append(", ");
        sb.append("updateLocalFileRating=").append(this.updateLocalFileRating).append(", ");
        sb.append("updateUmsServerRating=").append(this.updateUmsServerRating).append(", ");
        sb.append("syncRatings=").append(this.syncRatings).append(", ");
        sb.append("collisionStrategy=").append(this.collisionStrategy).append(", ");
        sb.append("]");
        return sb.toString();
    }

}