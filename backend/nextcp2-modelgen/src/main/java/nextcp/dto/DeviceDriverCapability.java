package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class DeviceDriverCapability
{

    public String deviceType;
    public String deviceDescription;

    public DeviceDriverCapability()
    {
    }

    public DeviceDriverCapability(String deviceType, String deviceDescription)
    {
        this.deviceType = deviceType;
        this.deviceDescription = deviceDescription;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceDriverCapability [");
        sb.append("deviceType=").append(this.deviceType).append(", ");
        sb.append("deviceDescription=").append(this.deviceDescription).append(", ");
        sb.append("]");
        return sb.toString();
    }

}