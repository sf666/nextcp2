package nextcp.rating;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import nextcp.musicbrainz.SpringTestConfiguration;
import nextcp.service.RatingService;

@SpringBootTest
@Configuration
@ContextConfiguration(classes = SpringTestConfiguration.class)
@ComponentScan(
{ "nextcp" })
public class TestRatingService
{
    @Autowired
    private RatingService ratingService = null;

    @Test
    public void testRating()
    {
        String trackId = "8ee8158a-060c-4225-9258-b6d2206549ca";

        int numUp = ratingService.setRatingInStarsByMusicBrainzId(trackId, 3);
        assertTrue(numUp == 1);

        int rating = ratingService.getRatingInStarsByMusicBrainzId(trackId);
        assertTrue(rating == 3);
    }
}
