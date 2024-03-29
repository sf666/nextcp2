package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class PlaylistAddContainerRequest
{

    public Boolean shuffle;
    public ContainerDto containerDto;
    public String mediaRendererUdn;

    public PlaylistAddContainerRequest()
    {
    }

    public PlaylistAddContainerRequest(Boolean shuffle, ContainerDto containerDto, String mediaRendererUdn)
    {
        this.shuffle = shuffle;
        this.containerDto = containerDto;
        this.mediaRendererUdn = mediaRendererUdn;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PlaylistAddContainerRequest [");
        sb.append("shuffle=").append(this.shuffle).append(", ");
        sb.append("containerDto=").append(this.containerDto).append(", ");
        sb.append("mediaRendererUdn=").append(this.mediaRendererUdn).append(", ");
        sb.append("]");
        return sb.toString();
    }

}