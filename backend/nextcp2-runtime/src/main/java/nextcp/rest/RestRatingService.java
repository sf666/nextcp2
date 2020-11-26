package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.domainmodel.rating.RatingService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/RatingService")
public class RestRatingService
{
    private static final Logger log = LoggerFactory.getLogger(RestRatingService.class.getName());
    
    
    @Autowired
    private RatingService serviceDelegate = null;

    @PostMapping("/getStarRatingByMusicBrainzID")
    public Integer getRatingInStarsByMusicBrainzId(@RequestBody String musicBrainzID)
    {
        try
        {
            return serviceDelegate.getRatingInStarsByMusicBrainzId(musicBrainzID);
        }
        catch (Exception e)
        {
            log.warn("", e);
            return null;
        }
    }

    @PostMapping("/setStarRatingByMusicBrainzID/{rating}")
    public void setRatingInStarsByMusicBrainzId(@PathVariable("rating") Integer rating, @RequestBody String musicBrainzID)
    {
        serviceDelegate.setRatingInStarsByMusicBrainzId(musicBrainzID, rating);
    }
}
