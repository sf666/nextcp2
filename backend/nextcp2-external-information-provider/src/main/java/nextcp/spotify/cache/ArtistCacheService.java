package nextcp.spotify.cache;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wrapper.spotify.model_objects.specification.Artist;

@Service
public class ArtistCacheService
{
    private static final Logger log = LoggerFactory.getLogger(ArtistCacheService.class.getName());

    private ObjectMapper om = new ObjectMapper();
    private SpotifySessionFactory sessionFactory = null;

    @Autowired
    public ArtistCacheService(SpotifySessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public int insertArtist(Artist artist)
    {
        try (SqlSession session = sessionFactory.openSession(true))
        {
            SpotifyMapping spotifyMapping = session.getMapper(SpotifyMapping.class);
            int num = spotifyMapping.insertArtist(artist.getUri(), artist.getName(), getAsJson(artist));
            session.commit(true);
            log.debug("Added artist to spotify cache db with name : " + artist.getName());
            return num;
        }
    }

    public Artist getArtistByName(String name)
    {
        try (SqlSession session = sessionFactory.openSession(true))
        {
            SpotifyMapping spotifyMapping = session.getMapper(SpotifyMapping.class);
            String json = spotifyMapping.selectArtistByName(name);
            return getObjectFromJson(json, Artist.class);
        }
        catch (Exception e)
        {
            log.error("could not read artist.", e);
            return null;
        }
    }

    public Artist getArtistBySpotifyId(String id)
    {
        try (SqlSession session = sessionFactory.openSession(true))
        {
            SpotifyMapping spotifyMapping = session.getMapper(SpotifyMapping.class);
            String json = spotifyMapping.selectArtistBySpotifyId(id);
            return getObjectFromJson(json, Artist.class);
        }
        catch (Exception e)
        {
            log.error("could not read artist.", e);
            return null;
        }
    }

    private <T> T getObjectFromJson(String json, Class<T> cls)
    {
        if (StringUtils.isAllBlank(json))
        {
            return null;
        }
        try
        {
            T obj = om.readValue(json, cls);
            return obj;
        }
        catch (IOException e)
        {
            log.error("could not read json string", e);
            return null;
        }
    }

    private String getAsJson(Object o)
    {
        ObjectWriter writer = om.writer();
        try
        {
            // withDefaultPrettyPrinter().
            return writer.writeValueAsString(o);
        }
        catch (IOException e)
        {
            log.error("could not write config", e);
            return "";
        }
    }
}
