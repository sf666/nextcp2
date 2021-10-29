package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class DevicePowerChanged
{

    public String udn;
    public Boolean isPowerOn;

    public DevicePowerChanged()
    {
    }

    public DevicePowerChanged(String udn, Boolean isPowerOn)
    {
        this.udn = udn;
        this.isPowerOn = isPowerOn;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DevicePowerChanged [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("isPowerOn=").append(this.isPowerOn).append(", ");
        sb.append("]");
        return sb.toString();
    }

}