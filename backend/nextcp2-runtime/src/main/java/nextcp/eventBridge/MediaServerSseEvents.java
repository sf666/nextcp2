package nextcp.eventBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import nextcp.dto.ContainerUpdateIdsDto;
import nextcp.dto.ServerPlaylists;
import nextcp.dto.WebStreamNowPlayingDto;

@Controller
public class MediaServerSseEvents
{
    public static final String DEVICE_MEDIASERVER_PLAYLIST_STATE = "DEVICE_MEDIASERVER_PLAYLIST_STATE";
    public static final String DEVICE_MEDIASERVER_RECENT_PLAYLIST_STATE = "DEVICE_MEDIASERVER_RECENT_PLAYLIST_STATE";
    public static final String DEVICE_MEDIASERVER_CONTAINER_UPDATE_IDS = "DEVICE_MEDIASERVER_CONTAINER_UPDATE_IDS";
    public static final String DEVICE_MEDIASERVER_WEB_STREAM_NOW_PLAYING = "DEVICE_MEDIASERVER_WEB_STREAM_NOW_PLAYING";

    
    @Autowired
    private SsePublisher ssePublisher = null;
    
    @EventListener
    public void mediaServerPlaylistChanged(ServerPlaylists serverPlaylists)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIASERVER_PLAYLIST_STATE, serverPlaylists);
    }
    
    public void mediaServerRecentPlaylistChanged(ServerPlaylists serverPlaylists)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIASERVER_RECENT_PLAYLIST_STATE, serverPlaylists);
    }

    @EventListener
    public void containerUpdateIdsChanged(ContainerUpdateIdsDto containerUpdateIds)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIASERVER_CONTAINER_UPDATE_IDS, containerUpdateIds);
    }

    /**
     * The live title of a continuous stream, for a player that has no renderer of its own: the
     * browser plays the url itself, so nothing on the backend knows what it is playing. It gets the
     * event by its objectID and decides itself whether it is the one playing.
     */
    @EventListener
    public void webStreamNowPlayingChanged(WebStreamNowPlayingDto nowPlaying)
    {
        ssePublisher.sendObjectAsJson(DEVICE_MEDIASERVER_WEB_STREAM_NOW_PLAYING, nowPlaying);
    }
}
