package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class RendererDeviceConfiguration
{

    public MediaRendererDto mediaRenderer;
    public String ip;
    public String displayString;
    public Boolean active;
    public Boolean hasOpenHomeDeviceDriver;
    public String deviceDriverType;
    public String connectionString;

    public RendererDeviceConfiguration()
    {
    }

    public RendererDeviceConfiguration(MediaRendererDto mediaRenderer, String ip, String displayString, Boolean active, Boolean hasOpenHomeDeviceDriver, String deviceDriverType, String connectionString)
    {
        this.mediaRenderer = mediaRenderer;
        this.ip = ip;
        this.displayString = displayString;
        this.active = active;
        this.hasOpenHomeDeviceDriver = hasOpenHomeDeviceDriver;
        this.deviceDriverType = deviceDriverType;
        this.connectionString = connectionString;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RendererDeviceConfiguration [");
        sb.append("mediaRenderer=").append(this.mediaRenderer).append(", ");
        sb.append("ip=").append(this.ip).append(", ");
        sb.append("displayString=").append(this.displayString).append(", ");
        sb.append("active=").append(this.active).append(", ");
        sb.append("hasOpenHomeDeviceDriver=").append(this.hasOpenHomeDeviceDriver).append(", ");
        sb.append("deviceDriverType=").append(this.deviceDriverType).append(", ");
        sb.append("connectionString=").append(this.connectionString).append(", ");
        sb.append("]");
        return sb.toString();
    }

}