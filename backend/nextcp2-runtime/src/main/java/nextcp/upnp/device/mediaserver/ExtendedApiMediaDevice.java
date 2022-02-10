package nextcp.upnp.device.mediaserver;

/**
 * Devices with extended API support
 */
public interface ExtendedApiMediaDevice
{
    public boolean isAlbumLiked(String uuid);

    public void likeAlbum(String uuid);

    public void dislikeAlbum(String uuid);

    public void likeSong(String uuid);

    public void dislikeSong(String uuid);
}
