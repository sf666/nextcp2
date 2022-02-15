package nextcp.indexer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.mymusic.MyMusicAlbumRepository;

@Service
public class MyMusicAlbumService
{
    
    @Autowired
    private MyMusicAlbumRepository myMusicRepository = null;

    public void likeAlbum(String uuid)
    {
        myMusicRepository.likeAlbum(uuid);
    }

    public void deleteAlbumLike(String uuid)
    {
        myMusicRepository.deleteAlbumLike(uuid);
    }

    public boolean isAlbumLiked(String uuid)
    {
        return myMusicRepository.isAlbumLiked(uuid);
    }
        
}
