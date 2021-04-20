package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class SystemInformationDto
{

    public String buildNumber;

    public SystemInformationDto()
    {
    }

    public SystemInformationDto(String buildNumber)
    {
        this.buildNumber = buildNumber;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SystemInformationDto [");
        sb.append("buildNumber=").append(this.buildNumber).append(", ");
        sb.append("]");
        return sb.toString();
    }

}