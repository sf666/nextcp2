package nextcp.spotify;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nextcp.spotify.cache.ArtistCacheService;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;

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

    /**
     * Names Spotify had nothing for, so a repeated search does not pay for the same miss again.
     * Only the persistent cache remembers hits; a miss used to go to the network on every single
     * search, which is what made a search over unknown artists slow - and unbearably so while the
     * account is not linked, where every one of those calls fails after a token renewal attempt.
     * Entries expire so a freshly linked account is picked up without a restart.
     */
    private static final long MISS_TTL_MILLIS = 6L * 60 * 60 * 1000;
    private static final int MAX_REMEMBERED_MISSES = 5000;
    private final Map<String, Long> missesByName = new ConcurrentHashMap<>();

    public Artist getArtistByName(String name)
    {
        Artist artist = artistCacheService.getArtistByName(name);
        if (artist != null)
        {
            return artist;
        }
        if (isRememberedMiss(name))
        {
            log.debug("Spotify had no artist named '{}' before - not asking again.", name);
            return null;
        }
        artist = getArtistByNameFromSpotify(name);
        if (artist != null)
        {
            artistCacheService.insertArtist(artist);
            missesByName.remove(name);
        }
        else
        {
            rememberMiss(name);
        }
        return artist;
    }

    private boolean isRememberedMiss(String name)
    {
        Long seenAt = missesByName.get(name);
        if (seenAt == null)
        {
            return false;
        }
        if (System.currentTimeMillis() - seenAt > MISS_TTL_MILLIS)
        {
            missesByName.remove(name);
            return false;
        }
        return true;
    }

    private void rememberMiss(String name)
    {
        if (missesByName.size() >= MAX_REMEMBERED_MISSES)
        {
            // A library can hold more artists than we want to remember misses for. Dropping the
            // whole set is enough: it only costs one more lookup per name afterwards.
            missesByName.clear();
        }
        missesByName.put(name, System.currentTimeMillis());
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
        // Spotify is optional: skip the lookup when it cannot succeed anyway. Testing the api
        // object alone was not enough - it exists as soon as a client id is configured, so an
        // unlinked or expired account still cost a failing round trip per artist and per search.
        if (!spotifyService.isAuthorized())
        {
            log.debug("Spotify account not linked - skipping artist image lookup for '{}'.", name);
            return null;
        }
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
