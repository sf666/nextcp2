package nextcp.domainmodel.services;

import org.springframework.stereotype.Service;

import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;

@Service
public class MyMusicService
{
    public void likeAlbum(String uuid, ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            return;
        }
        device.likeAlbum(uuid);
    }

    public void dislikeAlbum(String uuid, ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            return;
        }
        device.dislikeAlbum(uuid);
    }

    public boolean isAlbumLiked(String uuid, ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            return false;
        }
        return device.isAlbumLiked(uuid);
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
    
}
