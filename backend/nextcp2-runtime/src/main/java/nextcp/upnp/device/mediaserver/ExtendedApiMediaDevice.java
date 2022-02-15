package nextcp.upnp.device.mediaserver;

/**
 * Devices with extended API support
 */
public interface ExtendedApiMediaDevice
{
    public boolean isAlbumLiked(String musicBrainzReleaseId);

    public void likeAlbum(String musicBrainzReleaseId);

    public void dislikeAlbum(String musicBrainzReleaseId);

    public void likeSong(String musicBrainzTrackId);

    public void dislikeSong(String musicBrainzTrackId);
    
    public boolean isSongLiked(String musicBrainzTrackId);
    
    public void rateSong(String musicBrainzTrackId, int stars);

    public int getSongRating(String musicBrainzTrackId);
}
