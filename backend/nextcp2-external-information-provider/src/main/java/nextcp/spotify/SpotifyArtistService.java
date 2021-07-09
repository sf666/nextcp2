package nextcp.spotify;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;

import nextcp.spotify.cache.ArtistCacheService;

@Service
public class SpotifyArtistService
{
    private static final Logger log = LoggerFactory.getLogger(SpotifyArtistService.class.getName());

    @Autowired
    private SpotifyService spotifyService = null;

    @Autowired
    private ArtistCacheService artistCacheService = null;

    public SpotifyArtistService()
    {
    }

    public Artist getArtistByName(String name)
    {
        Artist artist = artistCacheService.getArtistByName(name);
        if (artist == null)
        {
            artist = getArtistByNameFromSpotify(name);
            if (artist != null)
            {
                artistCacheService.insertArtist(artist);
            }
        }
        return artist;
    }

    public String getArtistImageUrlByName(String name)
    {
        Artist artist = getArtistByName(name);
        if (artist != null)
        {
            if (artist.getImages().length > 0)
            {
                return artist.getImages()[0].getUrl();
            }
        }
        return null;
    }

    public Artist getArtistByNameFromSpotify(String name)
    {
        Paging<Artist> artists;
        try
        {
            artists = spotifyService.getSpotifyApi().searchArtists(name).build().execute();
            if (artists.getTotal() > 0)
            {
                if (artists.getTotal() > 1)
                {
                    log.debug("artist search deliverd more than 1 hits. Taking first one ... ");
                }
                if (artists.getItems().length > 0)
                {
                    return artists.getItems()[0];
                }
                else
                {
                    log.debug("no artist image available for artist : " + artists.getItems()[0].getName());
                }
            }
            else if (artists.getTotal() == 0)
            {
                log.debug("artist search deliverd 0 hits");
            }
        }
        catch (ParseException | IOException e)
        {
            log.warn("Error accessing spotify search api.", e);
        }
        catch (SpotifyWebApiException e)
        {
            spotifyService.renewToken();
        }
        return null;
    }

}
