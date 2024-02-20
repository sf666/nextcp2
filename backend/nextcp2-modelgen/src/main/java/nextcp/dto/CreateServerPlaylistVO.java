package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class CreateServerPlaylistVO
{

    public String mediaServerUdn;
    public String containerId;
    public String playlistName;

    public CreateServerPlaylistVO()
    {
    }

    public CreateServerPlaylistVO(String mediaServerUdn, String containerId, String playlistName)
    {
        this.mediaServerUdn = mediaServerUdn;
        this.containerId = containerId;
        this.playlistName = playlistName;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CreateServerPlaylistVO [");
        sb.append("mediaServerUdn=").append(this.mediaServerUdn).append(", ");
        sb.append("containerId=").append(this.containerId).append(", ");
        sb.append("playlistName=").append(this.playlistName).append(", ");
        sb.append("]");
        return sb.toString();
    }

}