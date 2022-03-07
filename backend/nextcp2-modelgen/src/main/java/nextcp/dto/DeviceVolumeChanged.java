package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class DeviceVolumeChanged
{

    public String udn;
    public Integer vol;

    public DeviceVolumeChanged()
    {
    }

    public DeviceVolumeChanged(String udn, Integer vol)
    {
        this.udn = udn;
        this.vol = vol;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceVolumeChanged [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("vol=").append(this.vol).append(", ");
        sb.append("]");
        return sb.toString();
    }

}