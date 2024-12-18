package nextcp.upnp.device.mediaserver;

import java.io.File;
import org.jupnp.support.model.container.Container;
import org.jupnp.support.model.item.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import nextcp.dto.ServerPlaylists;
import nextcp.dto.UpdateAlbumArtUriRequest;
import nextcp.dto.UpdateStarRatingRequest;

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
    
    public void rateSong(UpdateStarRatingRequest updateRequest);

    public void updateAlbumArtURI(UpdateAlbumArtUriRequest updateRequest);
    
    // Backup Services
    
    public void backupMyMusic();

    public void restoreMyMusic();
    
    // PlaylistServices
    
    public Container createPlaylist(String parentContainerId, String playlistName) throws Exception;
    
    public String addSongToPlaylist(String objectId, String playlistName);

    public void deleteObject(String objectId);
    
    public ServerPlaylists getServerPlaylists() throws JsonMappingException, JsonProcessingException;
    
    // Create Folder & Import Item
    
    public Container createFolder(String parentContainerId, String folderName) throws Exception;

	public Item createItem(String parentContainerId, File file) throws Exception;
	
	public String getOrCreateChildFolderId(String parentContainerId, String folderName) throws Exception;
	
	public String getOrCreateItem(String parentContainerId, File file) throws Exception;
    
}
