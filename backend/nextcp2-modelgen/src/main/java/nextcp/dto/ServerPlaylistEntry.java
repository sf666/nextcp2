package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ServerPlaylistEntry
{

    public String serverUdn;
    public String playlistName;
    public Integer songid;

    public ServerPlaylistEntry()
    {
    }

    public ServerPlaylistEntry(String serverUdn, String playlistName, Integer songid)
    {
        this.serverUdn = serverUdn;
        this.playlistName = playlistName;
        this.songid = songid;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerPlaylistEntry [");
        sb.append("serverUdn=").append(this.serverUdn).append(", ");
        sb.append("playlistName=").append(this.playlistName).append(", ");
        sb.append("songid=").append(this.songid).append(", ");
        sb.append("]");
        return sb.toString();
    }

}