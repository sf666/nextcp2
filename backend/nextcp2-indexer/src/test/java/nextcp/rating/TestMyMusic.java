package nextcp.rating;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;

import nextcp.indexer.service.MyMusicServiceRepository;

@SpringBootTest
@Configuration
@ContextConfiguration(classes = SpringTestConfiguration.class)
@ComponentScan(
{ "nextcp" })
public class TestMyMusic
{

    private String uuid = "49f0c9d4-6e84-4d43-877a-80942ce2af83";
    
    public TestMyMusic()
    {
    }

    @Autowired
    @Lazy
    private MyMusicServiceRepository myMusicService = null;
    
    @Test
    public void lifecycleTestMyAlbum()
    {
        myMusicService.likeAlbum(uuid);
        boolean liked = myMusicService.isAlbumLiked(uuid);
        assertTrue(liked);
        myMusicService.deleteAlbumLike(uuid);
        liked = myMusicService.isAlbumLiked(uuid);
        assertTrue(!liked);
    }
}
