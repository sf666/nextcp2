package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class DeviceDriverState
{

    public Boolean hasDeviceDriver;
    public String rendererUDN;
    public Integer volume;
    public Boolean standby;

    public DeviceDriverState()
    {
    }

    public DeviceDriverState(Boolean hasDeviceDriver, String rendererUDN, Integer volume, Boolean standby)
    {
        this.hasDeviceDriver = hasDeviceDriver;
        this.rendererUDN = rendererUDN;
        this.volume = volume;
        this.standby = standby;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceDriverState [");
        sb.append("hasDeviceDriver=").append(this.hasDeviceDriver).append(", ");
        sb.append("rendererUDN=").append(this.rendererUDN).append(", ");
        sb.append("volume=").append(this.volume).append(", ");
        sb.append("standby=").append(this.standby).append(", ");
        sb.append("]");
        return sb.toString();
    }

}