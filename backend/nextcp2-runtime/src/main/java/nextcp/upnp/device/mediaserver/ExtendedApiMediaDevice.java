package nextcp.upnp.device.mediaserver;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Devices with extended API support
 */
public interface ExtendedApiMediaDevice
{
    // Like Services
    
    public boolean isAlbumLiked(String musicBrainzReleaseId);

    public void likeAlbum(String musicBrainzReleaseId);

    public void dislikeAlbum(String musicBrainzReleaseId);

    public void likeSong(String musicBrainzTrackId);

    public void dislikeSong(String musicBrainzTrackId);
    
    public boolean isSongLiked(String musicBrainzTrackId);
    
    public void rateSong(String musicBrainzTrackId, int stars);

    public int getSongRating(String musicBrainzTrackId);

    // Backup Services
    
    public void backupMyMusic();

    public void restoreMyMusic();
    
    // PlaylistServices
    
    public void createPlaylist(String playlistName);
    
    public void addSongToPlaylist(Integer audiotraclId, String playlistName);

    public void removeSongFromPlaylist(Integer audiotracId, String playlistName);
    
    public List<String> getAllPlaylists() throws JsonMappingException, JsonProcessingException;
}
