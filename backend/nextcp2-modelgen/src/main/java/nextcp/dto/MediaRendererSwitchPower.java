package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class MediaRendererSwitchPower
{

    public String rendererUDN;
    public Boolean standby;

    public MediaRendererSwitchPower()
    {
    }

    public MediaRendererSwitchPower(String rendererUDN, Boolean standby)
    {
        this.rendererUDN = rendererUDN;
        this.standby = standby;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaRendererSwitchPower [");
        sb.append("rendererUDN=").append(this.rendererUDN).append(", ");
        sb.append("standby=").append(this.standby).append(", ");
        sb.append("]");
        return sb.toString();
    }

}