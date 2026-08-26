package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AddRadioStationRequest
{

    public String serverUdn;
    public String playlistObjectId;
    public String stationUuid;

    public AddRadioStationRequest()
    {
    }

    public AddRadioStationRequest(String serverUdn, String playlistObjectId, String stationUuid)
    {
        this.serverUdn = serverUdn;
        this.playlistObjectId = playlistObjectId;
        this.stationUuid = stationUuid;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AddRadioStationRequest [");
        sb.append("serverUdn=").append(this.serverUdn).append(", ");
        sb.append("playlistObjectId=").append(this.playlistObjectId).append(", ");
        sb.append("stationUuid=").append(this.stationUuid).append(", ");
        sb.append("]");
        return sb.toString();
    }

}