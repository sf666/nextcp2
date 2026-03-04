package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class DiscogsId
{

    public String ArtistId;
    public Long ReleaseId;

    public DiscogsId()
    {
    }

    public DiscogsId(String ArtistId, Long ReleaseId)
    {
        this.ArtistId = ArtistId;
        this.ReleaseId = ReleaseId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DiscogsId [");
        sb.append("ArtistId=").append(this.ArtistId).append(", ");
        sb.append("ReleaseId=").append(this.ReleaseId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}