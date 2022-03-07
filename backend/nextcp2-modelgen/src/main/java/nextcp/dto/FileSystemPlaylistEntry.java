package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class FileSystemPlaylistEntry
{

    public String playlistName;
    public String musicBrainzId;

    public FileSystemPlaylistEntry()
    {
    }

    public FileSystemPlaylistEntry(String playlistName, String musicBrainzId)
    {
        this.playlistName = playlistName;
        this.musicBrainzId = musicBrainzId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FileSystemPlaylistEntry [");
        sb.append("playlistName=").append(this.playlistName).append(", ");
        sb.append("musicBrainzId=").append(this.musicBrainzId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}