package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class RadioBrowserFilterValueDto
{

    public String value;
    public String code;
    public Integer stationCount;

    public RadioBrowserFilterValueDto()
    {
    }

    public RadioBrowserFilterValueDto(String value, String code, Integer stationCount)
    {
        this.value = value;
        this.code = code;
        this.stationCount = stationCount;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioBrowserFilterValueDto [");
        sb.append("value=").append(this.value).append(", ");
        sb.append("code=").append(this.code).append(", ");
        sb.append("stationCount=").append(this.stationCount).append(", ");
        sb.append("]");
        return sb.toString();
    }

}