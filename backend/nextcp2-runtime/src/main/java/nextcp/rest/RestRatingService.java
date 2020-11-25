package nextcp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.rating.LocalRatingService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/RatingService")
public class RestRatingService
{
    @Autowired
    private LocalRatingService serviceDelegate = null;

    @PostMapping("/starRatingByAcoustID")
    public Integer getRatingInStarsByAcoustId(@RequestBody String acoustID)
    {
        return serviceDelegate.getRatingInStarsByAcoustID(acoustID);
    }

    @PostMapping("/starRatingByMusicBrainzID")
    public Integer getRatingInStarsByMusicBrainzId(@RequestBody String musicBrainzID)
    {
        return serviceDelegate.getRatingInStarsByAcoustID(musicBrainzID);
    }
}
