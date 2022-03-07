package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class BrowseRequestDto
{

    public String mediaServerUDN;
    public String objectID;
    public String sortCriteria;

    public BrowseRequestDto()
    {
    }

    public BrowseRequestDto(String mediaServerUDN, String objectID, String sortCriteria)
    {
        this.mediaServerUDN = mediaServerUDN;
        this.objectID = objectID;
        this.sortCriteria = sortCriteria;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("BrowseRequestDto [");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("objectID=").append(this.objectID).append(", ");
        sb.append("sortCriteria=").append(this.sortCriteria).append(", ");
        sb.append("]");
        return sb.toString();
    }

}