package nextcp.indexer.service;

import org.springframework.beans.factory.annotation.Autowired;

import nextcp.mymusic.MyMusicRepository;

// Off @Service
public class MyMusicService
{
    
    @Autowired
    private MyMusicRepository myMusicRepository = null;

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
