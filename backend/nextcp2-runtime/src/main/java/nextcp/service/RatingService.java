package nextcp.service;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nextcp.dto.Config;
import nextcp.dto.MusicItemIdDto;
import nextcp.dto.ToastrMessage;
import nextcp.dto.UpdateStarRatingRequest;
import nextcp.musicbrainz.MusicBrainzService;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import jakarta.annotation.PreDestroy;

/**
 * Rating logic. This service tries to keep song rating information local to this control point.
 * 
 * Song ratings (writing) will be promoted to backends. If a device is able to supply a song rating, this information will be used, cached rating will be used.
 */
@Service
public class RatingService
{
    private static final Logger log = LoggerFactory.getLogger(RatingService.class.getName());

    /** A MusicBrainz id is a UUID. The web service refuses anything else with "400 : Invalid mbid.". */
    private static final Pattern MUSICBRAINZ_ID = Pattern
            .compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    /**
     * One thread on purpose - musicbrainz.org asks callers to stay at one request per second, and a
     * queue of one keeps that promise without any rate limiting of its own.
     */
    private final ExecutorService musicBrainzExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "musicbrainz-rating");
        thread.setDaemon(true);
        return thread;
    });

    @PreDestroy
    public void shutdown()
    {
        musicBrainzExecutor.shutdown();
    }
    
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

    public void setRatingInStars(UpdateStarRatingRequest updateRequest, ExtendedApiMediaDevice device)
    {
        // Send rating to device. The device only needs the objectID, so this works for
        // any kind of resource : songs as well as containers like folders and playlists.
        if (device != null)
        {
            device.rateSong(updateRequest);
        }

        // send rating to musicBrainz. Only songs carry a MusicBrainz track id, containers do not.
        if (updateRequest.musicItemIdDto != null && !StringUtils.isAllBlank(updateRequest.musicItemIdDto.musicBrainzIdTrackId))
        {
            updateMusicBrainzBackend(updateRequest.musicItemIdDto.musicBrainzIdTrackId, updateRequest.newRating);
        }
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
        	// TODO : search for objectIDs by MusicBrainzId and then do UpdateObject
            // device.rateSongByMusicBrainzID(uuid, ratings.get(uuid));
            num++;
        }
        this.publisher.publishEvent(new ToastrMessage("", "info", "musicbrainz.org import", num + " entries were imported"));
    }

    private void updateMusicBrainzBackend(String musicBrainzID, Integer rating)
    {
        if (StringUtils.isAllBlank(config.musicbrainzSupport.username))
        {
            log.trace("musicbrainz.org username not set in config. musicbrainz.org update is disabled.");
            return;
        }
        if (!MUSICBRAINZ_ID.matcher(musicBrainzID).matches())
        {
            log.warn("not rating on musicbrainz.org, '{}' is not a MusicBrainz id", musicBrainzID);
            return;
        }
        musicBrainzExecutor.execute(() -> sendRatingToMusicBrainz(musicBrainzID, rating));
    }

    /** Runs on {@link #musicBrainzExecutor}, never on the thread that served the rating request. */
    private void sendRatingToMusicBrainz(String musicBrainzID, Integer rating)
    {
        try
        {
            musicBrainzService.setRating(musicBrainzID, rating);
            publisher.publishEvent(new ToastrMessage("", "sucess", "MusicBrainz Rating", "successfully send to musicbrainz.org"));
        }
        catch (Exception e)
        {
            // Which recording failed is the first thing worth knowing, and it used to be missing.
            log.warn("cannot rate {} on musicbrainz.org", musicBrainzID, e);
            publisher.publishEvent(new ToastrMessage("", "error", "MusicBrainz Rating",
                    "couldn't save " + musicBrainzID + " : " + e.getMessage()));
        }
    }

}
