package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ServerDeviceConfiguration
{

    public String ip;
    public String displayString;
    public Boolean enabled;
    public MediaServerDto mediaServer;
    public String playistObjectId;

    public ServerDeviceConfiguration()
    {
    }

    public ServerDeviceConfiguration(String ip, String displayString, Boolean enabled, MediaServerDto mediaServer, String playistObjectId)
    {
        this.ip = ip;
        this.displayString = displayString;
        this.enabled = enabled;
        this.mediaServer = mediaServer;
        this.playistObjectId = playistObjectId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerDeviceConfiguration [");
        sb.append("ip=").append(this.ip).append(", ");
        sb.append("displayString=").append(this.displayString).append(", ");
        sb.append("enabled=").append(this.enabled).append(", ");
        sb.append("mediaServer=").append(this.mediaServer).append(", ");
        sb.append("playistObjectId=").append(this.playistObjectId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}