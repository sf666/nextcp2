package nextcp.rating.repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import nextcp.rating.RatingConfig;
import nextcp.rating.RatingException;
import nextcp.rating.domain.SongRating;

@Service
public class RepositoryAdminService
{
    private static final Logger log = LoggerFactory.getLogger(RepositoryAdminService.class.getName());

    private RatingConfig config = null;

    private PathMatcher matcher = null;

    private final int TARGET_DB_SCHEMA = 1;
    private final int MAX_COMMIT = 200;

    @Autowired
    private SessionManager sessionManager = null;

    @Autowired
    public RepositoryAdminService(RatingConfig config, SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
        this.config = config;

        if (StringUtils.isBlank(config.supportedFileTypes))
        {
            log.warn("Rating support disabled: Supportes 'supportedFileTypes' should not be empty.");
        }
        else
        {
            matcher = FileSystems.getDefault().getPathMatcher(String.format("glob:**/*.{%s}", config.supportedFileTypes.replaceAll("\\s", "")));
        }

        updateDatabaseToCurrentSchema();
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        try
        {
            rescanDirectory();
        }
        catch (Exception e)
        {
            log.warn("Rating service is disabled because of missing valid configuration.", e);
        }
    }

    /**
     * Rescan music directory. DB will be updated on a copy on write logic.
     */
    @Async
    public void rescanDirectory()
    {
        if (matcher == null)
        {
            return;
        }

        if (sessionManager.getSessionFactory() == null)
        {
            log.debug("session factory is null. Rating support is disabled.");
            return;
        }
        Path rootDir = Paths.get(config.musicDirectory);
        if (!rootDir.toFile().exists())
        {
            log.warn("Music root directory not supplied. Rating support is disabled.");
            return;
        }
        if (!rootDir.toFile().isDirectory())
        {
            log.warn(String.format("'musicRootPath' %s must point to a directory.", config.musicDirectory));
            return;
        }

        String sqlScriptPath = String.format("/sql/%s.sql", "createTmpTable");
        runScript(sqlScriptPath);

        AtomicInteger inserts = new AtomicInteger(0);
        try (SqlSession session = sessionManager.getSessionFactory().openSession(false))
        {
            long start = System.currentTimeMillis();
            Files.walk(rootDir).filter(matcher::matches).forEach(p -> updateDatabase(p, session, inserts));
            if (inserts.get() > 0)
            {
                session.commit(true);
            }
            log.info(String.format("finished directory scanning in %d seconds.", (System.currentTimeMillis() - start) / 1000));
        }
        catch (Exception e)
        {
            log.error("rescanDirectory", e);
        }

        sqlScriptPath = String.format("/sql/%s.sql", "renameTmpTable");
        runScript(sqlScriptPath);
    }

    private void updateDatabase(Path p, SqlSession session, AtomicInteger inserts)
    {
        SongRating song = new SongRating();
        song.setFilePath(p.toString());
        AudioFile audioFile;
        try
        {
            audioFile = AudioFileIO.read(p.toFile());

            Tag tag = audioFile.getTag();
            song.setAcoustID(tag.getFirst(FieldKey.ACOUSTID_ID));
            song.setMusicBrainzID(tag.getFirst(FieldKey.MUSICBRAINZ_TRACK_ID));
            song.setRatingFromTag(tag);

            session.insert("nextcp.rating.repository.sql.RatingMapping.insertSong", song);

            if (inserts.incrementAndGet() == MAX_COMMIT)
            {
                session.commit(true);
                inserts.set(0);
            }
        }
        catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e)
        {
            log.warn("cannot read audio tag", e);
        }
    }

    /**
     * Easy schema update concept ... just increment sql-script number ... On heavy concurrent work, this will not suit. Use something like firefly instead
     */
    public void updateDatabaseToCurrentSchema()
    {
        try
        {
            int currentVersion = getCurrentSchemaVersion();
            while (currentVersion < TARGET_DB_SCHEMA)
            {
                currentVersion++;
                String sqlScriptPath = String.format("/sql/%d.sql", currentVersion);
                runScript(sqlScriptPath);
            }            
        }
        catch (Exception e)
        {
            log.warn("cannot update database.", e);
        }
    }

    private void runScript(String sqlScriptPath)
    {
        try (InputStreamReader isr = new InputStreamReader(RepositoryAdminService.class.getResourceAsStream(sqlScriptPath)))
        {
            ScriptRunner scriptRunner = new ScriptRunner(sessionManager.getSessionFactory().openSession().getConnection());
            scriptRunner.runScript(isr);

        }
        catch (IOException e)
        {
            log.error("sql script error", e);
        }
    }

    /**
     * 
     * @return
     */
    public int getCurrentSchemaVersion()
    {
        if (sessionManager.getSessionFactory() == null)
        {
            log.debug("database session canot be opend ... ");
            throw new RatingException(RatingException.DATABASE_ACCESS_ERROR, "SessionFactory is null. Database cannot be accessed.");
        }

        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            Integer count = session.selectOne("nextcp.rating.repository.sql.DatabaseVersion.findRating", "DATABASE_CONFIG");
            if (count == 0)
            {
                return 0;
            }
            return session.selectOne("nextcp.rating.repository.sql.DatabaseVersion.selectSchemaVersion");
        }
        catch (Exception e)
        {
            rebuildDbFromScratch();
            // remove this exception, after above method is implemented
            log.error("DB error", e);
            throw new RuntimeException("Database is corrupt.");
        }
    }

    private void rebuildDbFromScratch()
    {
        // drop all known tables and start the update-process
    }

}
