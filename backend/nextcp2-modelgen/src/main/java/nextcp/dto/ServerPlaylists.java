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
    public List<String> playlists;
    public List<ServerPlaylistDto> serverPlaylists;

    public ServerPlaylists()
    {
    }

    public ServerPlaylists(String mediaServerUdn, List<String> playlists, List<ServerPlaylistDto> serverPlaylists)
    {
        this.mediaServerUdn = mediaServerUdn;
        this.playlists = playlists;
        this.serverPlaylists = serverPlaylists;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerPlaylists [");
        sb.append("mediaServerUdn=").append(this.mediaServerUdn).append(", ");
        sb.append("playlists=").append(this.playlists).append(", ");
        sb.append("serverPlaylists=").append(this.serverPlaylists).append(", ");
        sb.append("]");
        return sb.toString();
    }

}