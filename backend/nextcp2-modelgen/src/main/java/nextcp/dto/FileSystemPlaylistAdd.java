package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class FileSystemPlaylistAdd
{

    public String playlistName;
    public String musicBrainzId;

    public FileSystemPlaylistAdd()
    {
    }

    public FileSystemPlaylistAdd(String playlistName, String musicBrainzId)
    {
        this.playlistName = playlistName;
        this.musicBrainzId = musicBrainzId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FileSystemPlaylistAdd [");
        sb.append("playlistName=").append(this.playlistName).append(", ");
        sb.append("musicBrainzId=").append(this.musicBrainzId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}