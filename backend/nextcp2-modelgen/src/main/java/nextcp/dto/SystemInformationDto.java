package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class SystemInformationDto
{

    public String buildNumber;
    public String name;
    public String time;

    public SystemInformationDto()
    {
    }

    public SystemInformationDto(String buildNumber, String name, String time)
    {
        this.buildNumber = buildNumber;
        this.name = name;
        this.time = time;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SystemInformationDto [");
        sb.append("buildNumber=").append(this.buildNumber).append(", ");
        sb.append("name=").append(this.name).append(", ");
        sb.append("time=").append(this.time).append(", ");
        sb.append("]");
        return sb.toString();
    }

}