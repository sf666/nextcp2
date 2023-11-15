package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ServerPlaylistDto
{

    public String playlistName;
    public String playlistId;

    public ServerPlaylistDto()
    {
    }

    public ServerPlaylistDto(String playlistName, String playlistId)
    {
        this.playlistName = playlistName;
        this.playlistId = playlistId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerPlaylistDto [");
        sb.append("playlistName=").append(this.playlistName).append(", ");
        sb.append("playlistId=").append(this.playlistId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}