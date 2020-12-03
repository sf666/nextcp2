package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class RendererConfigDto
{

    public CopyOnWriteArrayList<RendererDeviceConfiguration> rendererDevices;

    public RendererConfigDto()
    {
    }

    public RendererConfigDto(CopyOnWriteArrayList<RendererDeviceConfiguration> rendererDevices)
    {
        this.rendererDevices = rendererDevices;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RendererConfigDto [");
        sb.append("rendererDevices=").append(this.rendererDevices).append(", ");
        sb.append("]");
        return sb.toString();
    }

}