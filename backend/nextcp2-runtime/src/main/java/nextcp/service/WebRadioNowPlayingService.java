package nextcp.service;

import java.util.Map;
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
 * Shows the live title a web radio announces via ICY. Unlike the AudioAddict enrichment this needs
 * no polling: UMS reads the ICY blocks while it serves the stream and pushes every change as a GENA
 * event, which {@link nextcp.upnp.device.mediaserver.UmsServerDevice} hands over here.
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
            if (existing.lastTitle != null)
            {
                publishEnriched(udn, existing, existing.lastTitle);
            }
            return;
        }
        StreamContext ctx = new StreamContext(track.objectID, track.title, event);
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
        String streamTitle;
        try
        {
            JsonNode node = OBJECT_MAPPER.readTree(nowPlaying);
            objectId = node.path("objectID").asText("");
            streamTitle = node.path("streamTitle").asText("");
        }
        catch (Exception e)
        {
            log.warn("cannot parse web stream now-playing event : {}", nowPlaying, e);
            return;
        }
        if (StringUtils.isBlank(objectId))
        {
            return;
        }
        for (Map.Entry<String, StreamContext> entry : activeStreams.entrySet())
        {
            StreamContext ctx = entry.getValue();
            if (!StringUtils.equals(ctx.objectId, objectId) || StringUtils.equals(ctx.lastTitle, streamTitle))
            {
                continue;
            }
            ctx.lastTitle = StringUtils.trimToNull(streamTitle);
            publishEnriched(entry.getKey(), ctx, ctx.lastTitle);
        }
    }

    /**
     * ICY carries a single unstructured string, so it becomes the title while the station name moves
     * to the album - the same shape the AudioAddict enrichment uses.
     */
    private void publishEnriched(String udn, StreamContext ctx, String streamTitle)
    {
        TrackInfoDto base = ctx.baseInfo;
        if (base == null || base.currentTrack == null)
        {
            return;
        }
        MusicItemDto ct = base.currentTrack;
        if (StringUtils.isBlank(streamTitle))
        {
            ct.title = ctx.stationName;
            ct.album = "";
        }
        else
        {
            ct.title = streamTitle;
            ct.album = ctx.stationName;
        }
        selfPublished.put(udn, base);
        publisher.publishEvent(base);
    }

    private static boolean isBroadcast(MusicItemDto track)
    {
        if (track == null || StringUtils.isBlank(track.objectID))
        {
            return false;
        }
        // An AudioAddict channel is an audioBroadcast too, but its live track comes from the
        // AudioAddict API - leave those to AudioAddictNowPlayingPoller so the two do not overwrite
        // each other's TrackInfoDto.
        if (track.audioAddictChannelId != null || track.audioAddictPlaylistId != null)
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
        private volatile TrackInfoDto baseInfo;
        private volatile String lastTitle;

        private StreamContext(String objectId, String stationName, TrackInfoDto baseInfo)
        {
            this.objectId = objectId;
            this.stationName = stationName;
            this.baseInfo = baseInfo;
        }
    }
}
