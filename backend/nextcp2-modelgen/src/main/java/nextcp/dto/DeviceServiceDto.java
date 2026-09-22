package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class DeviceServiceDto
{

    public String serviceType;
    public String serviceId;
    public Integer version;
    public List<String> actions;
    public List<String> eventedVariables;

    public DeviceServiceDto()
    {
    }

    public DeviceServiceDto(String serviceType, String serviceId, Integer version, List<String> actions, List<String> eventedVariables)
    {
        this.serviceType = serviceType;
        this.serviceId = serviceId;
        this.version = version;
        this.actions = actions;
        this.eventedVariables = eventedVariables;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceServiceDto [");
        sb.append("serviceType=").append(this.serviceType).append(", ");
        sb.append("serviceId=").append(this.serviceId).append(", ");
        sb.append("version=").append(this.version).append(", ");
        sb.append("actions=").append(this.actions).append(", ");
        sb.append("eventedVariables=").append(this.eventedVariables).append(", ");
        sb.append("]");
        return sb.toString();
    }

}