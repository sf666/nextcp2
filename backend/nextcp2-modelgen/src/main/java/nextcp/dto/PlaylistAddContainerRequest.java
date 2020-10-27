package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class PlaylistAddContainerRequest
{

    public ContainerDto containerDto;
    public String mediaRendererUdn;

    public PlaylistAddContainerRequest()
    {
    }

    public PlaylistAddContainerRequest(ContainerDto containerDto, String mediaRendererUdn)
    {
        this.containerDto = containerDto;
        this.mediaRendererUdn = mediaRendererUdn;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PlaylistAddContainerRequest [");
        sb.append("containerDto=").append(this.containerDto).append(", ");
        sb.append("mediaRendererUdn=").append(this.mediaRendererUdn).append(", ");
        sb.append("]");
        return sb.toString();
    }

}