package nextcp.audioaddict;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextcp.audioaddict.mapper.TrackHistoryItem;

/**
 * Standalone client for the public AudioAddict track-history API (DI.fm, RadioTunes, JazzRadio,
 * ...). Given an AudioAddict network short name and a channel id it returns the track currently
 * playing on that channel. This is used to enrich the "now playing" display in the control point
 * independently of whatever metadata the renderer itself exposes.
 */
@Service
public class AudioAddictNowPlayingService
{
    private static final Logger log = LoggerFactory.getLogger(AudioAddictNowPlayingService.class.getName());

    private static final String TRACK_HISTORY_URL = "https://api.audioaddict.com/v1/%s/track_history/channel/%d";

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();

    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Returns the track currently playing on the given AudioAddict channel, or {@code null} if it
     * cannot be determined (network error, empty history, unknown channel). The AudioAddict
     * track_history endpoint returns the recent tracks most-recent-first, so element 0 is the one
     * playing now.
     *
     * @param network   AudioAddict network short name (e.g. "di")
     * @param channelId numeric AudioAddict channel id
     */
    public TrackHistoryItem getCurrentTrack(String network, int channelId)
    {
        if (network == null || network.isBlank())
        {
            return null;
        }
        String url = String.format(TRACK_HISTORY_URL, network, channelId);
        try
        {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
            {
                log.warn("AudioAddict track_history for {}/{} returned HTTP {}", network, channelId, response.statusCode());
                return null;
            }
            TrackHistoryItem[] items = objectMapper.readValue(response.body(), TrackHistoryItem[].class);
            if (items == null || items.length == 0)
            {
                return null;
            }
            return items[0];
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            log.warn("AudioAddict track_history request interrupted for {}/{}", network, channelId);
            return null;
        }
        catch (Exception e)
        {
            log.warn("AudioAddict track_history request failed for {}/{} : {}", network, channelId, e.getMessage());
            return null;
        }
    }
}
