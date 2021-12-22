package nextcp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.indexer.service.MyMusicService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MyMusicService")
public class RestMyMusicService
{
    @Autowired
    @Lazy
    private MyMusicService myMusicService = null;
    
    public RestMyMusicService()
    {
    }

    @PostMapping("/likeAlbum")
    public void likeAlbum(@RequestBody String uuid)
    {
        myMusicService.likeAlbum(uuid);
    }

    @PostMapping("/deleteAlbumLike")
    public void deleteAlbumLike(@RequestBody String uuid)
    {
        myMusicService.deleteAlbumLike(uuid);
    }

    @PostMapping("/isAlbumLiked")
    public boolean isAlbumLiked(@RequestBody String uuid)
    {
        return myMusicService.isAlbumLiked(uuid);
    }

}
