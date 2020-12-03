package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class MediaRendererSetVolume
{

    public String rendererUDN;
    public Integer volume;

    public MediaRendererSetVolume()
    {
    }

    public MediaRendererSetVolume(String rendererUDN, Integer volume)
    {
        this.rendererUDN = rendererUDN;
        this.volume = volume;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaRendererSetVolume [");
        sb.append("rendererUDN=").append(this.rendererUDN).append(", ");
        sb.append("volume=").append(this.volume).append(", ");
        sb.append("]");
        return sb.toString();
    }

}