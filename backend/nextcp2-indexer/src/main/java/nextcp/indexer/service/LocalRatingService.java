package nextcp.indexer.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.indexer.IndexerException;
import nextcp.rating.domain.SongIndexed;
import nextcp.rating.repository.RepositoryAdminService;
import nextcp.rating.repository.SongPersistenceService;

/**
 * Main Rating-Service class for interaction.
 */
@Service
public class LocalRatingService
{
    private static final Logger log = LoggerFactory.getLogger(LocalRatingService.class.getName());

    @Autowired
    private SongPersistenceService songPersistenceService = null;

    @Autowired
    private RepositoryAdminService persistenceAdminService = null;

    public Integer getRatingInStarsByAcoustID(String acoustID)
    {
        return songPersistenceService.getRatingByAcoustID(acoustID);
    }

    public Integer getRatingInStarsByMusicBrainzID(String musicBrainzID)
    {
        return songPersistenceService.getRatingByMusicBrainzID(musicBrainzID);
    }

    public void rescanMusicDirectory()
    {
        persistenceAdminService.rescanDirectory();
    }

    /**
     * Rating is stars. 0 to 5.
     * 
     * @param acoustId
     * @param ratingInStars
     */
    public void setAcousticRatingInStars(String acoustId, int ratingInStars) throws IndexerException
    {
        if (ratingInStars < 0 || ratingInStars > 5)
        {
            throw new IndexerException(IndexerException.ILLEGAL_RATING_VALUE, "Rating value must be between 0 and 5 (including).");
        }

        if (StringUtils.isBlank(acoustId))
        {
            throw new IndexerException(IndexerException.EMPTY_ACOUSTIC_ID, "AcousticID must not be empty or blanc.");
        }

        SongIndexed song = songPersistenceService.getSongByAcoustId(acoustId);
        persistSongRatingInDbAndMusicFile(ratingInStars, song);
    }

    public void setMusicBrainzRatingInStars(String musicBrainzTitleId, int ratingInStars) throws IndexerException
    {
        if (ratingInStars < 0 || ratingInStars > 5)
        {
            throw new IndexerException(IndexerException.ILLEGAL_RATING_VALUE, "Rating value must be between 0 and 5 (including).");
        }

        if (StringUtils.isBlank(musicBrainzTitleId))
        {
            throw new IndexerException(IndexerException.EMPTY_ACOUSTIC_ID, "MusicBrainz-Title-ID must not be empty or blanc.");
        }

        SongIndexed song = songPersistenceService.getSongByMusicBrainzId(musicBrainzTitleId);
        if (song == null)
        {
            throw new RuntimeException("song not available in local rating DB.");
        }
        else
        {
            persistSongRatingInDbAndMusicFile(ratingInStars, song);
        }
    }

    /**
     * writes rating in database and in audio tag.
     * 
     * @param ratingInStars
     * @param song
     */
    private void persistSongRatingInDbAndMusicFile(int ratingInStars, SongIndexed song)
    {
        AudioFile audioFile;
        try
        {
            audioFile = AudioFileIO.read(new File(song.getFilePath()));
            Tag tag = audioFile.getTag();
            song.setRating(ratingInStars);
            tag.setField(FieldKey.RATING, song.getRatingForTag(tag));
            audioFile.commit();
        }
        catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotWriteException e)
        {
            log.warn("Error writing Tag info.", e);
        }
        songPersistenceService.updateSong(song);
    }

}
