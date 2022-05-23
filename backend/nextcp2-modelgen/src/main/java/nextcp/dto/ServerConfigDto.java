package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ServerConfigDto
{

    public List<ServerDeviceConfiguration> serverDevices;

    public ServerConfigDto()
    {
    }

    public ServerConfigDto(List<ServerDeviceConfiguration> serverDevices)
    {
        this.serverDevices = serverDevices;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerConfigDto [");
        sb.append("serverDevices=").append(this.serverDevices).append(", ");
        sb.append("]");
        return sb.toString();
    }

}