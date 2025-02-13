package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class RadioNetwork
{

    public String network;
    public String displayName;
    public List<RadioNetworkQuality> quality;
    public String albumArt;

    public RadioNetwork()
    {
    }

    public RadioNetwork(String network, String displayName, List<RadioNetworkQuality> quality, String albumArt)
    {
        this.network = network;
        this.displayName = displayName;
        this.quality = quality;
        this.albumArt = albumArt;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioNetwork [");
        sb.append("network=").append(this.network).append(", ");
        sb.append("displayName=").append(this.displayName).append(", ");
        sb.append("quality=").append(this.quality).append(", ");
        sb.append("albumArt=").append(this.albumArt).append(", ");
        sb.append("]");
        return sb.toString();
    }

}