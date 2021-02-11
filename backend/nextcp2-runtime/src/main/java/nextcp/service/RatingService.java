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
import nextcp.rating.repository.UserRatingPersistenceService;

/**
 * Logic for rating backends.
 */
@Service
public class RatingService
{
    @Autowired
    private Config config = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    //
    // Central user ratig service
    //
    @Autowired
    private UserRatingPersistenceService userRatingPersistenceService = null;

    //
    // Available rating backends
    // ====================================================================
    @Autowired
    private MusicBrainzService musicBrainzService = null;

    @Autowired
    private LocalRatingService localRatingService = null;

    public Integer getRatingInStarsByMusicBrainzId(String musicBrainzID)
    {
        try
        {
            Integer rating = userRatingPersistenceService.getRatingByMusicBrainzID(musicBrainzID);
            return rating;
        }
        catch (Exception e)
        {
            this.publisher.publishEvent(new ToastrMessage("", "error", "read rating", "couldn't read "));
            return null;
        }
    }

    public int setRatingInStarsByMusicBrainzId(String musicBrainzID, Integer rating)
    {
        if (rating == null)
        {
            rating = 0;
        }

        UserRating ur = new UserRating(musicBrainzID, null, rating);
        int numUpdated = userRatingPersistenceService.insertOrUpdateUserRating(ur);

        updateLocalFileBackend(musicBrainzID, rating);
        updateMusicBrainzBackend(musicBrainzID, rating);

        return numUpdated;
    }

    public int syncRatingsFromAudioFile()
    {
        int num = userRatingPersistenceService.syncRating();
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
            int numUpdated = userRatingPersistenceService.insertOrUpdateUserRating(ur);
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
                localRatingService.setMusicBrainzRatingInStars(musicBrainzID, rating);
                this.publisher.publishEvent(new ToastrMessage("", "sucess", "local file", "Rating successfully applied to local file."));
            }
        }
        catch (Exception e)
        {
            this.publisher.publishEvent(new ToastrMessage("", "error", "Local DB Rating", "couldn't save : " + e.getMessage()));
        }
    }
}
