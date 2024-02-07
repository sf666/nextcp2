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
    public String playlistObjectId;
    public String songObjectId;

    public ServerPlaylistEntry()
    {
    }

    public ServerPlaylistEntry(String serverUdn, String playlistObjectId, String songObjectId)
    {
        this.serverUdn = serverUdn;
        this.playlistObjectId = playlistObjectId;
        this.songObjectId = songObjectId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerPlaylistEntry [");
        sb.append("serverUdn=").append(this.serverUdn).append(", ");
        sb.append("playlistObjectId=").append(this.playlistObjectId).append(", ");
        sb.append("songObjectId=").append(this.songObjectId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}