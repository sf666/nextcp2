package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class RendererConfigDto
{

    public List<RendererDeviceConfiguration> rendererDevices;

    public RendererConfigDto()
    {
    }

    public RendererConfigDto(List<RendererDeviceConfiguration> rendererDevices)
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