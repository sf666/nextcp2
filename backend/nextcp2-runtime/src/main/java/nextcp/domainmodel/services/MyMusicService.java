package nextcp.domainmodel.services;

import org.springframework.stereotype.Service;
import nextcp.dto.MusicAlbumIds;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;

@Service
public class MyMusicService
{
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

    public void backupMyMusic(ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            throw new RuntimeException("device not supplied");
        }
        device.backupMyMusic();
    }

    public void restoreMyMusic(ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            throw new RuntimeException("device not supplied");
        }
        device.restoreMyMusic();
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
    }
    
}
