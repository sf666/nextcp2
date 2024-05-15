package nextcp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nextcp.dto.UpdateStarRatingRequest;
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


    @PostMapping("/setStarRating")
    public void setRatingInStarsByMusicBrainzId(@RequestBody UpdateStarRatingRequest request)
    {
        ExtendedApiMediaDevice device = getExtendedMediaServerByUdn(request.mediaServerDevice);
        if (device != null) {
            serviceDelegate.setRatingInStars(request, device);
        } else {
        	log.warn("device not found {}.", request.mediaServerDevice);
        }
    }

    @PostMapping("/syncRatingsFromMusicBrainzToBackend")
    public void syncRatingsFromMusicBrainzToFiles(@RequestBody String serverUdn)
    {
        serviceDelegate.syncRatingsFromMusicBrainz(getExtendedMediaServerByUdn(serverUdn));
    }
}
