package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class RadioNetworkQuality
{

    public String key;
    public String displayName;

    public RadioNetworkQuality()
    {
    }

    public RadioNetworkQuality(String key, String displayName)
    {
        this.key = key;
        this.displayName = displayName;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioNetworkQuality [");
        sb.append("key=").append(this.key).append(", ");
        sb.append("displayName=").append(this.displayName).append(", ");
        sb.append("]");
        return sb.toString();
    }

}