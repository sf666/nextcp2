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

    public String albumArtUrl;
    public String playlistName;
    public String playlistId;
    public Integer numberOfElements;
    public String totalPlaytime;

    public ServerPlaylistDto()
    {
    }

    public ServerPlaylistDto(String albumArtUrl, String playlistName, String playlistId, Integer numberOfElements, String totalPlaytime)
    {
        this.albumArtUrl = albumArtUrl;
        this.playlistName = playlistName;
        this.playlistId = playlistId;
        this.numberOfElements = numberOfElements;
        this.totalPlaytime = totalPlaytime;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerPlaylistDto [");
        sb.append("albumArtUrl=").append(this.albumArtUrl).append(", ");
        sb.append("playlistName=").append(this.playlistName).append(", ");
        sb.append("playlistId=").append(this.playlistId).append(", ");
        sb.append("numberOfElements=").append(this.numberOfElements).append(", ");
        sb.append("totalPlaytime=").append(this.totalPlaytime).append(", ");
        sb.append("]");
        return sb.toString();
    }

}