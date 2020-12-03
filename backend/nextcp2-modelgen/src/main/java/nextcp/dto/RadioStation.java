package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class RadioStation
{

    public Long id;
    public String stationName;
    public String resourceUrl;
    public String artworkUrl;

    public RadioStation()
    {
    }

    public RadioStation(Long id, String stationName, String resourceUrl, String artworkUrl)
    {
        this.id = id;
        this.stationName = stationName;
        this.resourceUrl = resourceUrl;
        this.artworkUrl = artworkUrl;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioStation [");
        sb.append("id=").append(this.id).append(", ");
        sb.append("stationName=").append(this.stationName).append(", ");
        sb.append("resourceUrl=").append(this.resourceUrl).append(", ");
        sb.append("artworkUrl=").append(this.artworkUrl).append(", ");
        sb.append("]");
        return sb.toString();
    }

}