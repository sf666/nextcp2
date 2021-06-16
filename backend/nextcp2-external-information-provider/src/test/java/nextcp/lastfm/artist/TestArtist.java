package nextcp.lastfm.artist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import nextcp.SpringLastFMTestConfiguration;
import nextcp.lastfm.dto.artist.info.ArtistInfoResponse;
import nextcp.lastfm.service.LastFmArtistService;

@SpringBootTest
@Configuration
@ContextConfiguration(classes = SpringLastFMTestConfiguration.class)
@ComponentScan(
{ "nextcp.lastfm" })
public class TestArtist
{

    @Autowired
    private LastFmArtistService artistService = null;
    
    public TestArtist()
    {
    }
    
    @Test
    public void testArtist()
    {
        String artist = "Rhye";
        ArtistInfoResponse resp = artistService.getInfoByArtist(artist);
        System.out.println(resp);
    }
}
