package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MediaServerDto
{

    public String udn;
    public String friendlyName;
    public Boolean extendedApi;

    public MediaServerDto()
    {
    }

    public MediaServerDto(String udn, String friendlyName, Boolean extendedApi)
    {
        this.udn = udn;
        this.friendlyName = friendlyName;
        this.extendedApi = extendedApi;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaServerDto [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("friendlyName=").append(this.friendlyName).append(", ");
        sb.append("extendedApi=").append(this.extendedApi).append(", ");
        sb.append("]");
        return sb.toString();
    }

}