package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ServerPlaylists
{

    public String mediaServerUdn;
    public String containerId;
    public List<ServerPlaylistDto> serverPlaylists;

    public ServerPlaylists()
    {
    }

    public ServerPlaylists(String mediaServerUdn, String containerId, List<ServerPlaylistDto> serverPlaylists)
    {
        this.mediaServerUdn = mediaServerUdn;
        this.containerId = containerId;
        this.serverPlaylists = serverPlaylists;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerPlaylists [");
        sb.append("mediaServerUdn=").append(this.mediaServerUdn).append(", ");
        sb.append("containerId=").append(this.containerId).append(", ");
        sb.append("serverPlaylists=").append(this.serverPlaylists).append(", ");
        sb.append("]");
        return sb.toString();
    }

}