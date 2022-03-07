package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class SearchRequestDto
{

    public String parentObjectID;
    public Long startElement;
    public Long requestCount;
    public String mediaServerUDN;
    public String searchRequest;
    public String sortCriteria;

    public SearchRequestDto()
    {
    }

    public SearchRequestDto(String parentObjectID, Long startElement, Long requestCount, String mediaServerUDN, String searchRequest, String sortCriteria)
    {
        this.parentObjectID = parentObjectID;
        this.startElement = startElement;
        this.requestCount = requestCount;
        this.mediaServerUDN = mediaServerUDN;
        this.searchRequest = searchRequest;
        this.sortCriteria = sortCriteria;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SearchRequestDto [");
        sb.append("parentObjectID=").append(this.parentObjectID).append(", ");
        sb.append("startElement=").append(this.startElement).append(", ");
        sb.append("requestCount=").append(this.requestCount).append(", ");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("searchRequest=").append(this.searchRequest).append(", ");
        sb.append("sortCriteria=").append(this.sortCriteria).append(", ");
        sb.append("]");
        return sb.toString();
    }

}