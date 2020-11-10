package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class QuickSearchRequestDto
{

    public Long requestCount;
    public String mediaServerUDN;
    public String searchRequest;
    public String sortCriteria;

    public QuickSearchRequestDto()
    {
    }

    public QuickSearchRequestDto(Long requestCount, String mediaServerUDN, String searchRequest, String sortCriteria)
    {
        this.requestCount = requestCount;
        this.mediaServerUDN = mediaServerUDN;
        this.searchRequest = searchRequest;
        this.sortCriteria = sortCriteria;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("QuickSearchRequestDto [");
        sb.append("requestCount=").append(this.requestCount).append(", ");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("searchRequest=").append(this.searchRequest).append(", ");
        sb.append("sortCriteria=").append(this.sortCriteria).append(", ");
        sb.append("]");
        return sb.toString();
    }

}