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

    public NextcpFileConfigDto nextcpFileConfigDto;
    public List<ServerDeviceConfiguration> serverDevices;

    public ServerConfigDto()
    {
    }

    public ServerConfigDto(NextcpFileConfigDto nextcpFileConfigDto, List<ServerDeviceConfiguration> serverDevices)
    {
        this.nextcpFileConfigDto = nextcpFileConfigDto;
        this.serverDevices = serverDevices;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerConfigDto [");
        sb.append("nextcpFileConfigDto=").append(this.nextcpFileConfigDto).append(", ");
        sb.append("serverDevices=").append(this.serverDevices).append(", ");
        sb.append("]");
        return sb.toString();
    }

}