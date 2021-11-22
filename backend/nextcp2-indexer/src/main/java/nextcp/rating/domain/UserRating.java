package nextcp.rating.domain;

/**
 * Rating a user has done. This is the sink of all available backends.
 */
public class UserRating
{
    public String musicBrainzId;
    public String acoustId;
    public Integer rating;
    
    public UserRating()
    {
    }
    
    public UserRating(String musicBrainzId, String acoustId, Integer rating)
    {
        super();
        this.musicBrainzId = musicBrainzId;
        this.acoustId = acoustId;
        this.rating = rating;
    }
}
