package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class QuickSearchRequestDto
{

    public String mediaServerUDN;
    public String searchRequest;
    public String sortCriteria;

    public QuickSearchRequestDto()
    {
    }

    public QuickSearchRequestDto(String mediaServerUDN, String searchRequest, String sortCriteria)
    {
        this.mediaServerUDN = mediaServerUDN;
        this.searchRequest = searchRequest;
        this.sortCriteria = sortCriteria;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("QuickSearchRequestDto [");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("searchRequest=").append(this.searchRequest).append(", ");
        sb.append("sortCriteria=").append(this.sortCriteria).append(", ");
        sb.append("]");
        return sb.toString();
    }

}