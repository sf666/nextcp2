package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class SeekSecondsDto
{

    public String rendererUDN;
    public Long seconds;

    public SeekSecondsDto()
    {
    }

    public SeekSecondsDto(String rendererUDN, Long seconds)
    {
        this.rendererUDN = rendererUDN;
        this.seconds = seconds;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SeekSecondsDto [");
        sb.append("rendererUDN=").append(this.rendererUDN).append(", ");
        sb.append("seconds=").append(this.seconds).append(", ");
        sb.append("]");
        return sb.toString();
    }

}