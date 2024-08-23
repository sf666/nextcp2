package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MediaPlayerConfigDto
{

    public String workdir;
    public String script;
    public String playType;
    public Boolean overwrite;
    public Boolean addToPlaylist;
    public ContainerIdDto addToPlaylistId;
    public ContainerIdDto addToFolderId;
    public String mediaServerUdn;

    public MediaPlayerConfigDto()
    {
    }

    public MediaPlayerConfigDto(String workdir, String script, String playType, Boolean overwrite, Boolean addToPlaylist, ContainerIdDto addToPlaylistId, ContainerIdDto addToFolderId, String mediaServerUdn)
    {
        this.workdir = workdir;
        this.script = script;
        this.playType = playType;
        this.overwrite = overwrite;
        this.addToPlaylist = addToPlaylist;
        this.addToPlaylistId = addToPlaylistId;
        this.addToFolderId = addToFolderId;
        this.mediaServerUdn = mediaServerUdn;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaPlayerConfigDto [");
        sb.append("workdir=").append(this.workdir).append(", ");
        sb.append("script=").append(this.script).append(", ");
        sb.append("playType=").append(this.playType).append(", ");
        sb.append("overwrite=").append(this.overwrite).append(", ");
        sb.append("addToPlaylist=").append(this.addToPlaylist).append(", ");
        sb.append("addToPlaylistId=").append(this.addToPlaylistId).append(", ");
        sb.append("addToFolderId=").append(this.addToFolderId).append(", ");
        sb.append("mediaServerUdn=").append(this.mediaServerUdn).append(", ");
        sb.append("]");
        return sb.toString();
    }

}