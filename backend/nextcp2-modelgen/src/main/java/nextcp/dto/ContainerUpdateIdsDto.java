package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ContainerUpdateIdsDto
{

    public String mediaServerUdn;
    public List<String> containerIds;

    public ContainerUpdateIdsDto()
    {
    }

    public ContainerUpdateIdsDto(String mediaServerUdn, List<String> containerIds)
    {
        this.mediaServerUdn = mediaServerUdn;
        this.containerIds = containerIds;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ContainerUpdateIdsDto [");
        sb.append("mediaServerUdn=").append(this.mediaServerUdn).append(", ");
        sb.append("containerIds=").append(this.containerIds).append(", ");
        sb.append("]");
        return sb.toString();
    }

}