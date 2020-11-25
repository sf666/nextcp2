package nextcp.domainmodel.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.musicbrainz.MusicBrainzService;
import nextcp.rating.LocalRatingService;

@Service
public class RatingService
{
    @Autowired
    private MusicBrainzService musicBrainzService = null;
    
    @Autowired
    private LocalRatingService localRatingService = null;
    
    
}
