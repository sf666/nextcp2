package nextcp.service;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nextcp.dto.Config;
import nextcp.dto.MusicItemIdDto;
import nextcp.dto.ToastrMessage;
import nextcp.musicbrainz.MusicBrainzService;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;

/**
 * Rating logic. This service tries to keep song rating information local to this control point.
 * 
 * Song ratings (writing) will be promoted to backends. If a device is able to supply a song rating, this information will be used, cached rating will be used.
 */
@Service
public class RatingService
{
    private static final Logger log = LoggerFactory.getLogger(RatingService.class.getName());
    
    @Autowired
    private Config config = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    //
    // Available rating backends
    // 1. musicBrainz
    // 2. 
    // ====================================================================
    @Autowired
    private MusicBrainzService musicBrainzService = null;

    public void setRatingInStars(MusicItemIdDto ids, Integer rating, ExtendedApiMediaDevice device)
    {
        if (rating == null)
        {
            rating = 0;
        }
        
        // Send rating to device
        if (device != null)
        {
            device.rateSong(ids.umsAudiotrackId, rating);
        }
        
        // send rating to musicBrainz
        updateMusicBrainzBackend(ids.musicBrainzIdTrackId, rating);
    }


    public void syncRatingsFromMusicBrainz(ExtendedApiMediaDevice device)
    {
        if (device == null)
        {
            this.publisher.publishEvent(new ToastrMessage("", "error", "musicbrainz.org import","select media server first"));
            return;
        }
        
        HashMap<String, Integer> ratings = musicBrainzService.getAllUserRatings();
        int num = 0;
        for (String uuid : ratings.keySet())
        {
            device.rateSongByMusicBrainzID(uuid, ratings.get(uuid));
            num++;
        }
        this.publisher.publishEvent(new ToastrMessage("", "info", "musicbrainz.org import", num + " entries were imported"));
    }

    private void updateMusicBrainzBackend(String musicBrainzID, Integer rating)
    {
        try
        {
            if (!StringUtils.isAllBlank(config.musicbrainzSupport.username))
            {
                musicBrainzService.setRating(musicBrainzID, rating);
                this.publisher.publishEvent(new ToastrMessage("", "sucess", "MusicBrainz Rating", "successfully send to musicbrainz.org"));
            } else {
                log.trace("musicbrainz.org username not set in config. musicbrainz.orgupdate is disabled.");
            }
        }
        catch (Exception e)
        {
            this.publisher.publishEvent(new ToastrMessage("", "error", "MusicBrainz Rating", "couldn't save : " + e.getMessage()));
        }
    }

}
