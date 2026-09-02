package nextcp.service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextcp.dto.MusicItemDto;
import nextcp.dto.TrackInfoDto;

/**
 * Shows what a continuous stream served by UMS is playing right now. Where UMS gets that from
 * differs per stream - ICY blocks for a plain internet radio, the AudioAddict API for a channel,
 * its own playback state for a curated playlist - but all of them arrive here the same way: as a
 * GENA event that {@link nextcp.upnp.device.mediaserver.UmsServerDevice} hands over. No polling.
 */
@Component
public class WebRadioNowPlayingService
{
    private static final Logger log = LoggerFactory.getLogger(WebRadioNowPlayingService.class.getName());

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Renderers currently playing a broadcast stream, keyed by renderer UDN.
    private final Map<String, StreamContext> activeStreams = new ConcurrentHashMap<>();

    // The enriched events we published ourselves, so they are ignored when they come back to us.
    private final Map<String, TrackInfoDto> selfPublished = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher publisher;

    public WebRadioNowPlayingService(ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
    }

    /**
     * Remembers which renderer plays which stream, so an incoming title can be matched to it.
     */
    @EventListener
    public void onTrackInfo(TrackInfoDto event)
    {
        if (event == null || event.mediaRendererUdn == null)
        {
            return;
        }
        String udn = event.mediaRendererUdn;
        if (event == selfPublished.get(udn))
        {
            return;
        }

        MusicItemDto track = event.currentTrack;
        if (!isBroadcast(track))
        {
            if (activeStreams.remove(udn) != null)
            {
                selfPublished.remove(udn);
                log.debug("web radio now-playing disabled for {}", udn);
            }
            return;
        }

        StreamContext existing = activeStreams.get(udn);
        if (existing != null && StringUtils.equals(existing.objectId, track.objectID))
        {
            // Genuine renderer update for the same stream: keep the last live title so the display
            // does not fall back to the station name.
            existing.baseInfo = event;
            if (existing.lastInfo != null)
            {
                publishEnriched(udn, existing, existing.lastInfo);
            }
            return;
        }
        StreamContext ctx = new StreamContext(track.objectID, track.title, track.artistName, event);
        activeStreams.put(udn, ctx);
        log.debug("web radio now-playing enabled for {} ({})", udn, track.objectID);
    }

    /**
     * @param nowPlaying the JSON payload of the UMS "WebStreamNowPlaying" state variable.
     */
    public void onWebStreamNowPlaying(String nowPlaying)
    {
        if (StringUtils.isBlank(nowPlaying))
        {
            return;
        }
        String objectId;
        NowPlaying info;
        try
        {
            JsonNode node = OBJECT_MAPPER.readTree(nowPlaying);
            objectId = node.path("objectID").asText("");
            info = new NowPlaying(
                StringUtils.trimToNull(node.path("artist").asText("")),
                StringUtils.trimToNull(node.path("title").asText("")),
                StringUtils.trimToNull(node.path("artUrl").asText("")),
                StringUtils.trimToNull(node.path("streamTitle").asText("")));
        }
        catch (Exception e)
        {
            log.warn("cannot parse now-playing event : {}", nowPlaying, e);
            return;
        }
        if (StringUtils.isBlank(objectId))
        {
            return;
        }
        for (Map.Entry<String, StreamContext> entry : activeStreams.entrySet())
        {
            StreamContext ctx = entry.getValue();
            if (!StringUtils.equals(ctx.objectId, objectId) || Objects.equals(ctx.lastInfo, info))
            {
                continue;
            }
            ctx.lastInfo = info.isEmpty() ? null : info;
            publishEnriched(entry.getKey(), ctx, ctx.lastInfo);
        }
    }

    /**
     * The station or channel name moves to the album so the context stays visible, and the live
     * track takes the title. A source that only knows one line (ICY) leaves artist and cover alone.
     */
    private void publishEnriched(String udn, StreamContext ctx, NowPlaying info)
    {
        TrackInfoDto base = ctx.baseInfo;
        if (base == null || base.currentTrack == null)
        {
            return;
        }
        MusicItemDto ct = base.currentTrack;
        if (info == null)
        {
            ct.title = ctx.stationName;
            ct.album = "";
            ct.artistName = ctx.stationArtist;
        }
        else
        {
            ct.album = ctx.stationName;
            ct.title = StringUtils.defaultIfBlank(info.title, info.streamTitle);
            // A line without an artist - a jingle, an ad - must not keep the artist of the last track.
            ct.artistName = info.artist != null ? info.artist : ctx.stationArtist;
            if (info.artUrl != null)
            {
                ct.albumArtUrl = info.artUrl;
            }
        }
        selfPublished.put(udn, base);
        publisher.publishEvent(base);
    }

    private record NowPlaying(String artist, String title, String artUrl, String streamTitle)
    {
        private boolean isEmpty()
        {
            return StringUtils.isAllBlank(artist, title, streamTitle);
        }
    }

    private static boolean isBroadcast(MusicItemDto track)
    {
        if (track == null || StringUtils.isBlank(track.objectID))
        {
            return false;
        }
        return StringUtils.startsWith(track.objectClass, "object.item.audioItem.audioBroadcast")
            || (track.audioFormat != null && Boolean.TRUE.equals(track.audioFormat.isStreaming));
    }

    private static final class StreamContext
    {
        private final String objectId;
        // The station name as the media server announced it, kept as context once a title arrives.
        private final String stationName;
        // What the media server announced as the artist, restored whenever a live title has none.
        private final String stationArtist;
        private volatile TrackInfoDto baseInfo;
        private volatile NowPlaying lastInfo;

        private StreamContext(String objectId, String stationName, String stationArtist, TrackInfoDto baseInfo)
        {
            this.objectId = objectId;
            this.stationName = stationName;
            this.stationArtist = stationArtist;
            this.baseInfo = baseInfo;
        }
    }
}
