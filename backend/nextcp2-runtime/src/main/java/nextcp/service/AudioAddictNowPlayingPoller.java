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
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.AudioAddictPlaylistNowPlaying;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

/**
 * Enriches the control point's "now playing" display for AudioAddict/DI.fm streams.
 * <p>
 * A renderer only reports the static channel/playlist (e.g. "Chill &amp; Tropical House") as the
 * current track and, in the case of LUMIN, does not surface ICY in-band metadata. UMS emits the
 * AudioAddict channel or playlist id into the DIDL {@code ums-tags} desc block, which the control
 * point parses into {@link MusicItemDto}. This component remembers which renderers play an
 * AudioAddict stream and periodically resolves the live track:
 * <ul>
 * <li><b>Radio channels</b> - queried directly from the public AudioAddict track-history API.</li>
 * <li><b>Curated playlists</b> - the current track is UMS-internal playback state, so it is fetched
 * from the serving UMS media server via the UmsExtendedServices {@code getPlaylistNowPlaying}
 * action.</li>
 * </ul>
 * On change the renderer's {@link TrackInfoDto} is re-published with artist/title/cover folded in,
 * so the whole UI shows it consistently through the existing track-info flow.
 */
@Component
public class AudioAddictNowPlayingPoller
{
    private static final Logger log = LoggerFactory.getLogger(AudioAddictNowPlayingPoller.class.getName());

    private static final long POLL_INTERVAL_MS = 15000;

    // Renderers currently playing an AudioAddict stream, keyed by renderer UDN.
    private final Map<String, StreamContext> activeStreams = new ConcurrentHashMap<>();

    // The enriched TrackInfoDto instances we published ourselves, to ignore them when they come
    // back to us as events (avoids a feedback loop and cache pollution).
    private final Map<String, TrackInfoDto> selfPublished = new ConcurrentHashMap<>();

    private final AudioAddictNowPlayingService nowPlayingService;
    private final DeviceRegistry deviceRegistry;
    private final ApplicationEventPublisher publisher;

    public AudioAddictNowPlayingPoller(AudioAddictNowPlayingService nowPlayingService, DeviceRegistry deviceRegistry,
        ApplicationEventPublisher publisher)
    {
        this.nowPlayingService = nowPlayingService;
        this.deviceRegistry = deviceRegistry;
        this.publisher = publisher;
    }

    /**
     * Tracks which renderers are currently playing an AudioAddict channel or playlist. Starts
     * enrichment when one begins playing and stops when the renderer plays something else.
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
        StreamContext next = toContext(track, event);
        if (next == null)
        {
            if (activeStreams.remove(udn) != null)
            {
                selfPublished.remove(udn);
                log.debug("AudioAddict now-playing enrichment disabled for {}", udn);
            }
            return;
        }
        StreamContext existing = activeStreams.get(udn);
        if (existing == null || !existing.sameStream(next))
        {
            activeStreams.put(udn, next);
            log.debug("AudioAddict now-playing enrichment enabled for {} ({})", udn, next);
        }
        else
        {
            // Genuine renderer update for the same stream: refresh base info and re-apply the last
            // known live track so the enrichment is not lost (avoids flicker back to channel name).
            existing.baseInfo = event;
            if (existing.lastKey != null)
            {
                publishEnriched(udn, existing, existing.lastArtist, existing.lastTitle, existing.lastArtUrl);
            }
        }
    }

    private static StreamContext toContext(MusicItemDto track, TrackInfoDto base)
    {
        if (track == null)
        {
            return null;
        }
        if (track.audioAddictChannelId != null && StringUtils.isNotBlank(track.audioAddictNetwork))
        {
            return StreamContext.radio(track.audioAddictNetwork, track.audioAddictChannelId, track.title, base);
        }
        if (track.audioAddictPlaylistId != null)
        {
            return StreamContext.playlist(track.audioAddictPlaylistId, track.title, base);
        }
        return null;
    }

    @Scheduled(fixedRate = POLL_INTERVAL_MS)
    public void poll()
    {
        for (Map.Entry<String, StreamContext> entry : activeStreams.entrySet())
        {
            String udn = entry.getKey();
            StreamContext ctx = entry.getValue();
            try
            {
                if (ctx.channelId != null)
                {
                    pollRadio(udn, ctx);
                }
                else if (ctx.playlistId != null)
                {
                    pollPlaylist(udn, ctx);
                }
            }
            catch (Exception e)
            {
                log.warn("AudioAddict now-playing poll failed for {} : {}", udn, e.getMessage());
            }
        }
    }

    private void pollRadio(String udn, StreamContext ctx)
    {
        TrackHistoryItem item = nowPlayingService.getCurrentTrack(ctx.network, ctx.channelId);
        if (item == null)
        {
            return;
        }
        String key = "t:" + item.trackId;
        if (key.equals(ctx.lastKey))
        {
            return;
        }
        String artist = StringUtils.isNotBlank(item.displayArtist) ? item.displayArtist : item.artist;
        String title = StringUtils.isNotBlank(item.displayTitle) ? item.displayTitle : item.title;
        applyAndPublish(udn, ctx, key, artist, title, item.artUrl);
    }

    private void pollPlaylist(String udn, StreamContext ctx)
    {
        AudioAddictPlaylistNowPlaying np = fetchPlaylistNowPlaying(ctx.playlistId);
        if (np == null || (StringUtils.isBlank(np.title) && StringUtils.isBlank(np.artist)))
        {
            return;
        }
        String key = "p:" + np.artist + "|" + np.title;
        if (key.equals(ctx.lastKey))
        {
            return;
        }
        applyAndPublish(udn, ctx, key, np.artist, np.title, np.artUrl);
    }

    /**
     * Asks the serving UMS media server for the playlist's current track. There is normally a single
     * UMS; if several expose the extended API the first that knows the playlist wins.
     */
    private AudioAddictPlaylistNowPlaying fetchPlaylistNowPlaying(int playlistId)
    {
        for (MediaServerDevice server : deviceRegistry.getAvailableMediaServer())
        {
            if (server instanceof ExtendedApiMediaDevice ext)
            {
                try
                {
                    AudioAddictPlaylistNowPlaying np = ext.getPlaylistNowPlaying(playlistId);
                    if (np != null)
                    {
                        return np;
                    }
                }
                catch (Exception e)
                {
                    log.debug("getPlaylistNowPlaying failed on {} : {}", server.getFriendlyName(), e.getMessage());
                }
            }
        }
        return null;
    }

    private void applyAndPublish(String udn, StreamContext ctx, String key, String artist, String title, String artUrl)
    {
        ctx.lastKey = key;
        ctx.lastArtist = artist;
        ctx.lastTitle = title;
        ctx.lastArtUrl = artUrl;
        publishEnriched(udn, ctx, artist, title, artUrl);
    }

    /**
     * Re-publishes the renderer's current {@link TrackInfoDto} with the live AudioAddict track folded
     * into the current track: the live artist/title (and cover) are shown, while the channel/playlist
     * name is kept as the album so the context stays visible.
     */
    private void publishEnriched(String udn, StreamContext ctx, String artist, String title, String artUrl)
    {
        TrackInfoDto base = ctx.baseInfo;
        if (base == null || base.currentTrack == null)
        {
            return;
        }
        MusicItemDto ct = base.currentTrack;
        if (StringUtils.isNotBlank(ctx.streamName))
        {
            ct.album = ctx.streamName;
        }
        if (StringUtils.isNotBlank(artist))
        {
            ct.artistName = artist;
        }
        if (StringUtils.isNotBlank(title))
        {
            ct.title = title;
        }
        if (StringUtils.isNotBlank(artUrl))
        {
            ct.albumArtUrl = artUrl;
        }
        selfPublished.put(udn, base);
        publisher.publishEvent(base);
    }

    private static final class StreamContext
    {
        // Radio: network + channelId set. Playlist: playlistId set.
        private final String network;
        private final Integer channelId;
        private final Integer playlistId;
        private final String streamName;
        // Most recent genuine renderer TrackInfoDto (carries codec/bitrate/uri).
        private volatile TrackInfoDto baseInfo;
        // Last published live track, so we can re-apply on genuine renderer events and de-duplicate.
        private volatile String lastKey;
        private volatile String lastArtist;
        private volatile String lastTitle;
        private volatile String lastArtUrl;

        private StreamContext(String network, Integer channelId, Integer playlistId, String streamName, TrackInfoDto baseInfo)
        {
            this.network = network;
            this.channelId = channelId;
            this.playlistId = playlistId;
            this.streamName = streamName;
            this.baseInfo = baseInfo;
        }

        private static StreamContext radio(String network, int channelId, String streamName, TrackInfoDto baseInfo)
        {
            return new StreamContext(network, channelId, null, streamName, baseInfo);
        }

        private static StreamContext playlist(int playlistId, String streamName, TrackInfoDto baseInfo)
        {
            return new StreamContext(null, null, playlistId, streamName, baseInfo);
        }

        private boolean sameStream(StreamContext other)
        {
            return java.util.Objects.equals(channelId, other.channelId) && java.util.Objects.equals(network, other.network)
                && java.util.Objects.equals(playlistId, other.playlistId);
        }

        @Override
        public String toString()
        {
            return channelId != null ? "radio network=" + network + " channel=" + channelId : "playlist=" + playlistId;
        }
    }
}
