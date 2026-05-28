package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *
 */
public class SelectedDevicesDto
{

    public MediaRendererDto mediaRenderer;
    public MediaServerDto mediaServer;

    public SelectedDevicesDto()
    {
    }

    public SelectedDevicesDto(MediaRendererDto mediaRenderer, MediaServerDto mediaServer)
    {
        this.mediaRenderer = mediaRenderer;
        this.mediaServer = mediaServer;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SelectedDevicesDto [");
        sb.append("mediaRenderer=").append(this.mediaRenderer).append(", ");
        sb.append("mediaServer=").append(this.mediaServer).append(", ");
        sb.append("]");
        return sb.toString();
    }

}