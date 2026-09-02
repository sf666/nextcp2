package nextcp.domainmodel.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import nextcp.dto.MusicAlbumIds;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;

@Service
public class MyMusicService
{
    private static final Logger log = LoggerFactory.getLogger(MyMusicService.class.getName());

    private final ApplicationEventPublisher publisher;

    public MyMusicService(ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
    }

    public void likeAlbum(MusicAlbumIds albumIds, ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            return;
        }
        device.likeAlbum(albumIds);
    }

    public void dislikeAlbum(MusicAlbumIds albumIds, ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            return;
        }
        device.dislikeAlbum(albumIds);
    }

    public boolean isAlbumLiked(MusicAlbumIds albumIds, ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            return false;
        }
        return device.isAlbumLiked(albumIds);
    }

    public void backupRatings(ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            throw new RuntimeException("device not supplied");
        }
        device.backupRatings();
    }

    public void restoreRatings(ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            throw new RuntimeException("device not supplied");
        }
        device.restoreRatings();
        publishLikedPlaylists(device);
    }

    /**
     * The sidebar list is a search on the liked rating, so a restore changes it. Nothing on the
     * media server announces that - it sends no ContainerUpdateIDs for a rating - and the list was
     * only refetched when the selected server changed, which is why it took a browser reload.
     */
    private void publishLikedPlaylists(ExtendedApiMediaDevice device)
    {
        try
        {
            publisher.publishEvent(device.getServerPlaylists());
        }
        catch (Exception e)
        {
            log.warn("could not refresh the liked playlists after the restore", e);
        }
    }
    
}
