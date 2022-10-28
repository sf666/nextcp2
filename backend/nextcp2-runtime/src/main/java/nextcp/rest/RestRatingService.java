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

import nextcp.dto.MusicItemIdDto;
import nextcp.service.RatingService;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/RatingService")
public class RestRatingService extends BaseRestService
{
    private static final Logger log = LoggerFactory.getLogger(RestRatingService.class.getName());

    @Autowired
    private RatingService serviceDelegate = null;


    @PostMapping("/setStarRating/{rating}/{mediaServerDevice}")
    public void setRatingInStarsByMusicBrainzId(@PathVariable("rating") Integer rating, @PathVariable("mediaServerDevice") String udn, @RequestBody MusicItemIdDto ids)
    {
        ExtendedApiMediaDevice device = getExtendedMediaServerByUdn(udn);
        serviceDelegate.setRatingInStars(ids, rating, device);
    }

    @PostMapping("/syncRatingsFromMusicBrainzToBackend")
    public void syncRatingsFromMusicBrainzToFiles(@RequestBody String serverUdn)
    {
        serviceDelegate.syncRatingsFromMusicBrainz(getExtendedMediaServerByUdn(serverUdn));
    }
}
