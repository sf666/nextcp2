package nextcp.musicbrainz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@Configuration
@ContextConfiguration(classes = SpringTestConfiguration.class)
@ComponentScan(
{ "nextcp" })
public class TestMusicBrainz
{
    @Autowired
    private MusicBrainzService musicBrainzService = null;

    @Test
    public void testDbScripts() throws Exception
    {
        int targetRating = 3;
//        
//        String trackId = "8ee8158a-060c-4225-9258-b6d2206549ca";
//        musicBrainzService.setRating(trackId, targetRating);
//        assertEquals(targetRating, musicBrainzService.getRating(trackId));
    }

}
