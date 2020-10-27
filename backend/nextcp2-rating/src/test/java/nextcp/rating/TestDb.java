package nextcp.rating;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import nextcp.rating.repository.RepositoryAdminService;

@SpringBootTest
@Configuration
@ContextConfiguration(classes = SpringTestConfiguration.class)
@ComponentScan(
{ "nextcp" })
public class TestDb
{

    private static final Logger log = LoggerFactory.getLogger(TestDb.class.getName());

    private File dbFile = null;

    private boolean deleteDbFile = false;

    private boolean skipTest = true;

    public TestDb()
    {
        dbFile = new File("/tmp/db");
        // dbFile = File.createTempFile("db_", "test");
    }

    @Autowired
    private RepositoryAdminService repositoryAdminService = null;

    @Autowired
    private RatingService ratingService = null;

    @Test
    public void testDbScripts() throws IOException
    {
        if (!skipTest)
        {
            try
            {
                // current schema is number "1".
                assertEquals(1, repositoryAdminService.getCurrentSchemaVersion());

                // check
                String acoustid = "fd92e5d0-9edf-4c8d-8c21-3c8dace568a6";
                String brainzId = "b9517b13-015d-4f45-8bed-2c7b207c701d";

                ratingService.setAcousticRatingInStars(acoustid, 2);
                ratingService.setMusicBrainzRatingInStars(brainzId, 1);

                assertEquals(2, ratingService.getRatingInStarsByAcoustID(acoustid));
                assertEquals(1, ratingService.getRatingInStarsByMusicBrainzID(brainzId));
                
            }
            finally
            {
                if (deleteDbFile)
                {
                    System.out.println("deleting database file : " + dbFile.getAbsolutePath());
                    dbFile.delete();
                }
            }
        }
    }

    @Bean
    public RatingConfig getConfig()
    {

        log.info("DB File is : " + dbFile.getAbsolutePath());

        RatingConfig rc = new RatingConfig();
        rc.databaseFilename = dbFile.getAbsolutePath();
        rc.musicDirectory = "/music/";
        rc.supportedFileTypes = "flac, mp3, ogg, ape";
        return rc;
    }
}
