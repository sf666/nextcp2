package nextcp.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nextcp.dto.Config;
import nextcp.dto.ToastrMessage;
import nextcp.indexer.service.LocalRatingService;
import nextcp.musicbrainz.MusicBrainzService;
import nextcp.rating.domain.UserRating;
import nextcp.rating.repository.SongPersistenceService;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;

/**
 * Rating logic. This service tries to keep song rating information local to this control point.
 * 
 * Song ratings (writing) will be promoted to backends. If a device is able to supply a song rating, this information will be used, cached rating will be used.
 */
@Service
public class RatingService
{
    @Autowired
    private Config config = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    //
    // database persistence layer
    //
    @Autowired
    private SongPersistenceService ratingPersistenceService = null;

    //
    // Available rating backends
    // 1. musicBrainz
    // 2. 
    // ====================================================================
    @Autowired
    private MusicBrainzService musicBrainzService = null;

    @Autowired
    private LocalRatingService localRatingService = null;

    public Integer getRatingInStarsByMusicBrainzId(String musicBrainzID)
    {
        try
        {
            Integer rating = ratingPersistenceService.getRatingByMusicBrainzID(musicBrainzID);
            return rating;
        }
        catch (Exception e)
        {
            this.publisher.publishEvent(new ToastrMessage("", "error", "read rating", "couldn't read : " + e.getMessage()));
            return null;
        }
    }

    public int setRatingInStarsByMusicBrainzId(String musicBrainzID, Integer rating, ExtendedApiMediaDevice device)
    {
        if (rating == null)
        {
            rating = 0;
        }

        // store user rating in local cache
        UserRating ur = new UserRating(musicBrainzID, null, rating);
        int numUpdated = ratingPersistenceService.insertOrUpdateUserRating(ur);
        
        // Update local file
        updateLocalFileBackend(musicBrainzID, rating);
        updateMusicBrainzBackend(musicBrainzID, rating);

        // inform backend
        if (device != null)
        {
            device.rateSong(musicBrainzID, rating);
        }
        return numUpdated;
    }

    public int syncRatingsFromAudioFile()
    {
        int num = ratingPersistenceService.syncRating();
        this.publisher.publishEvent(new ToastrMessage("", "info", "Import from audiofile", num + " entries were imported"));
        return num;
    }

    public void syncRatingsFromMusicBrainz(boolean storeInSong)
    {
        HashMap<String, Integer> ratings = musicBrainzService.getAllUserRatings();
        int num = 0;
        for (String uuid : ratings.keySet())
        {
            UserRating ur = new UserRating(uuid, null, ratings.get(uuid));
            int numUpdated = ratingPersistenceService.insertOrUpdateUserRating(ur);
            if (numUpdated > 0)
            {
                num++;
            }
            if (storeInSong)
            {
                updateLocalFileBackend(uuid, ratings.get(uuid));
            }
        }
        this.publisher.publishEvent(new ToastrMessage("", "info", "Import from musicbrainz.org", num + " entries were imported"));
    }

    private void updateMusicBrainzBackend(String musicBrainzID, Integer rating)
    {
        try
        {
            if (config.ratingStrategy.updateMusicBrainzRating)
            {
                musicBrainzService.setRating(musicBrainzID, rating);
                this.publisher.publishEvent(new ToastrMessage("", "sucess", "MusicBrainz", "Rating successfully send to musicbrainz.org"));
            }
        }
        catch (Exception e)
        {
            this.publisher.publishEvent(new ToastrMessage("", "error", "Set MusicBrainz Rating", "couldn't save : " + e.getMessage()));
        }
    }

    private void updateLocalFileBackend(String musicBrainzID, Integer rating)
    {
        try
        {
            if (config.ratingStrategy.updateLocalFileRating)
            {
                localRatingService.persistMusicBrainzRatingInStarsLocalFile(musicBrainzID, rating);
                this.publisher.publishEvent(new ToastrMessage("", "sucess", "local file", "Rating successfully applied to local file."));
                ratingPersistenceService.updateStarRatingInSong(musicBrainzID, rating);
            }
        }
        catch (Exception e)
        {
            this.publisher.publishEvent(new ToastrMessage("", "error", "Local DB Rating", "couldn't save : " + e.getMessage()));
        }
    }

    public void indexMusicDirectory()
    {
        localRatingService.rescanMusicDirectory();
    }
}
