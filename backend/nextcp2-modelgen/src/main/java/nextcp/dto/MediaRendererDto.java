package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MediaRendererDto
{

    public String udn;
    public String friendlyName;

    public MediaRendererDto()
    {
    }

    public MediaRendererDto(String udn, String friendlyName)
    {
        this.udn = udn;
        this.friendlyName = friendlyName;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaRendererDto [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("friendlyName=").append(this.friendlyName).append(", ");
        sb.append("]");
        return sb.toString();
    }

}