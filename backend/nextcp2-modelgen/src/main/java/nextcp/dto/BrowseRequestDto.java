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
    public String filter;
    public Long start;
    public Long count;
    public String searchInOID;

    public BrowseRequestDto()
    {
    }

    public BrowseRequestDto(String mediaServerUDN, String objectID, String sortCriteria, String filter, Long start, Long count, String searchInOID)
    {
        this.mediaServerUDN = mediaServerUDN;
        this.objectID = objectID;
        this.sortCriteria = sortCriteria;
        this.filter = filter;
        this.start = start;
        this.count = count;
        this.searchInOID = searchInOID;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("BrowseRequestDto [");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("objectID=").append(this.objectID).append(", ");
        sb.append("sortCriteria=").append(this.sortCriteria).append(", ");
        sb.append("filter=").append(this.filter).append(", ");
        sb.append("start=").append(this.start).append(", ");
        sb.append("count=").append(this.count).append(", ");
        sb.append("searchInOID=").append(this.searchInOID).append(", ");
        sb.append("]");
        return sb.toString();
    }

}