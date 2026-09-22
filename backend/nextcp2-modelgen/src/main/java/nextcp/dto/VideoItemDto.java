package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class VideoItemDto
{

    public String hlsUrl;

    public VideoItemDto()
    {
    }

    public VideoItemDto(String hlsUrl)
    {
        this.hlsUrl = hlsUrl;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("VideoItemDto [");
        sb.append("hlsUrl=").append(this.hlsUrl).append(", ");
        sb.append("]");
        return sb.toString();
    }

}