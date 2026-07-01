package nextcp.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import nextcp.audioaddict.AudioAddictNowPlayingService;
import nextcp.audioaddict.mapper.TrackHistoryItem;
import nextcp.dto.MusicItemDto;
import nextcp.dto.TrackInfoDto;

/**
 * Enriches the control point's "now playing" display for AudioAddict/DI.fm radio channels.
 * <p>
 * A renderer only reports the static channel (e.g. "Chill &amp; Tropical House") as the current
 * track and, in the case of LUMIN, does not surface the ICY in-band metadata. UMS emits the
 * AudioAddict channel id/network into the DIDL {@code ums-tags} desc block, which the control point
 * parses into {@link MusicItemDto}. This component remembers which renderers are playing an
 * AudioAddict channel, periodically polls the AudioAddict API for the live track and re-publishes
 * the renderer's {@link TrackInfoDto} with the current artist/title/cover folded in - so the whole
 * UI shows it consistently through the existing track-info flow, independently of the renderer.
 */
@Component
public class AudioAddictNowPlayingPoller
{
    private static final Logger log = LoggerFactory.getLogger(AudioAddictNowPlayingPoller.class.getName());

    private static final long POLL_INTERVAL_MS = 15000;

    // Renderers currently playing an AudioAddict channel, keyed by renderer UDN.
    private final Map<String, ChannelContext> activeChannels = new ConcurrentHashMap<>();

    // The enriched TrackInfoDto instances we published ourselves, so we can ignore them when they
    // come back to us as events (avoids a feedback loop and cache pollution).
    private final Map<String, TrackInfoDto> selfPublished = new ConcurrentHashMap<>();

    private final AudioAddictNowPlayingService nowPlayingService;
    private final ApplicationEventPublisher publisher;

    public AudioAddictNowPlayingPoller(AudioAddictNowPlayingService nowPlayingService, ApplicationEventPublisher publisher)
    {
        this.nowPlayingService = nowPlayingService;
        this.publisher = publisher;
    }

    /**
     * Tracks which renderers are currently playing an AudioAddict channel. Starts enrichment when a
     * channel begins playing and stops when the renderer plays something else.
     */
    @EventListener
    public void onTrackInfo(TrackInfoDto event)
    {
        if (event == null || event.mediaRendererUdn == null)
        {
            return;
        }
        String udn = event.mediaRendererUdn;
        // Ignore the enriched event we published ourselves.
        if (event == selfPublished.get(udn))
        {
            return;
        }

        MusicItemDto track = event.currentTrack;
        if (track != null && track.audioAddictChannelId != null && StringUtils.isNotBlank(track.audioAddictNetwork))
        {
            ChannelContext ctx = activeChannels.get(udn);
            if (ctx == null || ctx.channelId != track.audioAddictChannelId.intValue() || !track.audioAddictNetwork.equals(ctx.network))
            {
                ctx = new ChannelContext(track.audioAddictNetwork, track.audioAddictChannelId, track.title, event);
                activeChannels.put(udn, ctx);
                log.debug("AudioAddict now-playing enrichment enabled for {} (network={}, channel={})", udn, ctx.network, ctx.channelId);
            }
            else
            {
                // Genuine renderer update for the same channel: refresh the base info and re-apply the
                // last known live track so the enrichment is not lost (avoids flicker back to channel).
                ctx.baseInfo = event;
                if (ctx.lastTrack != null)
                {
                    publishEnriched(udn, ctx);
                }
            }
        }
        else if (activeChannels.remove(udn) != null)
        {
            selfPublished.remove(udn);
            log.debug("AudioAddict now-playing enrichment disabled for {}", udn);
        }
    }

    @Scheduled(fixedRate = POLL_INTERVAL_MS)
    public void poll()
    {
        for (Map.Entry<String, ChannelContext> entry : activeChannels.entrySet())
        {
            String udn = entry.getKey();
            ChannelContext ctx = entry.getValue();
            try
            {
                TrackHistoryItem item = nowPlayingService.getCurrentTrack(ctx.network, ctx.channelId);
                if (item == null)
                {
                    continue;
                }
                if (ctx.lastTrack != null && ctx.lastTrack.trackId == item.trackId)
                {
                    // Same track as last poll - nothing changed.
                    continue;
                }
                ctx.lastTrack = item;
                publishEnriched(udn, ctx);
            }
            catch (Exception e)
            {
                log.warn("AudioAddict now-playing poll failed for {} : {}", udn, e.getMessage());
            }
        }
    }

    /**
     * Re-publishes the renderer's current {@link TrackInfoDto} with the live AudioAddict track folded
     * into the current track: the live artist/title (and cover) are shown, while the channel name is
     * kept as the album so the channel context stays visible.
     */
    private void publishEnriched(String udn, ChannelContext ctx)
    {
        TrackInfoDto base = ctx.baseInfo;
        TrackHistoryItem live = ctx.lastTrack;
        if (base == null || base.currentTrack == null || live == null)
        {
            return;
        }
        MusicItemDto ct = base.currentTrack;
        if (StringUtils.isNotBlank(ctx.channelName))
        {
            ct.album = ctx.channelName;
        }
        String artist = StringUtils.isNotBlank(live.displayArtist) ? live.displayArtist : live.artist;
        String title = StringUtils.isNotBlank(live.displayTitle) ? live.displayTitle : live.title;
        if (StringUtils.isNotBlank(artist))
        {
            ct.artistName = artist;
        }
        if (StringUtils.isNotBlank(title))
        {
            ct.title = title;
        }
        else if (StringUtils.isNotBlank(live.track))
        {
            ct.title = live.track;
        }
        if (StringUtils.isNotBlank(live.artUrl))
        {
            ct.albumArtUrl = live.artUrl;
        }
        selfPublished.put(udn, base);
        publisher.publishEvent(base);
    }

    private static final class ChannelContext
    {
        private final String network;
        private final int channelId;
        private final String channelName;
        // The most recent genuine renderer TrackInfoDto for this channel (carries codec/bitrate/uri).
        private volatile TrackInfoDto baseInfo;
        // The most recent live track fetched from the AudioAddict API.
        private volatile TrackHistoryItem lastTrack;

        private ChannelContext(String network, int channelId, String channelName, TrackInfoDto baseInfo)
        {
            this.network = network;
            this.channelId = channelId;
            this.channelName = channelName;
            this.baseInfo = baseInfo;
        }
    }
}
