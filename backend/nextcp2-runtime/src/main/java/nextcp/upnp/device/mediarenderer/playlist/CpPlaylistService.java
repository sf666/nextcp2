package nextcp.upnp.device.mediarenderer.playlist;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.domainmodel.device.services.IPlaylistService;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlaylistState;
import nextcp.dto.RendererPlaylist;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.avtransport.BaseAvTransportChangeEventImpl;
import nextcp.upnp.device.mediarenderer.playlist.playStrategy.IPlaylistFillStrategy;
import nextcp.upnp.device.mediarenderer.playlist.playStrategy.RandomPlaybackStrategy;
import nextcp.upnp.device.mediarenderer.playlist.playStrategy.SequencialPlaybackStrategy;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.InsertInput;
import nextcp.util.BackendException;

/**
 * central playlist service. Implementation sucks a little.
 * 
 */
public class CpPlaylistService extends BaseAvTransportChangeEventImpl implements IPlaylistService
{
    public enum TransportState
    {
        Stopped,
        Playing,
        Paused,
        Buffering
    };

    private static final Logger log = LoggerFactory.getLogger(CpPlaylistService.class.getName());

    private static final Long MAX_TRACKS = 16384L;

    //
    // state handling
    // ===========================================================================================

    private PlaylistState state = new PlaylistState();

    // Playlist with songs
    private LinkedList<MusicItemDto> playlistItems = new LinkedList<>();

    // Index list : intention is to define play order. Songs will be played from start to end of this list.
    private LinkedList<Integer> playbackItems = new LinkedList<Integer>();

    //
    // convenience states for playlistItems
    // ===========================================================================================

    // URL's of songs. Index is must macht 1:1 to playlistItems !
    private LinkedList<String> songUrls = new LinkedList<String>();

    private LinkedList<String> oldPlaybackItems = new LinkedList<String>();

    private String nextSongUrl = null;

    private int currentSongIdx = 0;

    private MediaRendererDevice device;

    public CpPlaylistService(MediaRendererDevice device)
    {
        this.device = device;
        state.udn = device.getUdnAsString();
        state.Id = 0L;
        state.ProtocolInfo = "";
        state.Repeat = false;
        state.Shuffle = false;
        state.TracksMax = MAX_TRACKS;
        state.TransportState = TransportState.Stopped.name();

        if (device != null)
        {
            this.device = device;
            if (device.hasUpnpAvTransport())
            {
                log.info("starting internal playlist implementation for device : " + device.getFriendlyName());
                getEventPublisher().publishEvent(state);
            }
        }
    }

    protected MediaRendererDevice getDevice()
    {
        return device;
    }

    private ApplicationEventPublisher getEventPublisher()
    {
        return getDevice().getEventPublisher();
    }

    private DtoBuilder getDtoBuilder()
    {
        return getDevice().getDtoBuilder();
    }

    private void reset()
    {
        currentSongIdx = 0;
        playlistItems.clear();
        playbackItems.clear();
        songUrls.clear();
        oldPlaybackItems.clear();

        updateSongUrls();
    }

    @Override
    public void transportStateChange(String value)
    {
        if ("PAUSED_PLAYBACK".equalsIgnoreCase(value))
        {
            proceedToNextSongToPlayNoNextUriSupport();
        }
        else if ("STOPPED".equalsIgnoreCase(value) || "TRANSITIONING".equalsIgnoreCase(value))
        {
            proceedToNextSongToPlayNoNextUriSupport();
        }
        else if ("PLAYING".equalsIgnoreCase(value))
        {
            log.debug("processed : " + value);
        }
        else
        {
            log.debug("not processed : " + value);
        }
    }

    private boolean hasNextUriSupport()
    {
        log.debug("nextAVTransportUri : " + device.getAvTransportEventListener().getCurrentAvTransportState().NextAVTransportURI);
        return !"NOT_IMPLEMENTED".equalsIgnoreCase(device.getAvTransportEventListener().getCurrentAvTransportState().NextAVTransportURI);
    }

    private void proceedToNextSongToPlayNoNextUriSupport()
    {
        // if device has no nextUrl support, it goes into PAUSED state
        if (!hasNextUriSupport())
        {
            if (isPlaylistPlaying())
            {
                MusicItemDto nextSong = moveToNextTrack();
                if (nextSong != null)
                {
                    getDevice().getAvTransportServiceBridge().setUrl(nextSong.streamingURL, nextSong.currentTrackMetadata);
                    getDevice().getAvTransportServiceBridge().play();
                }
                else
                {
                    stop();
                }
            }
        }
    }

    @Override
    public void currentTrackURIChange(String value)
    {
        super.currentTrackURIChange(value);

        proceedToNextSongNextUriSupport(value);
    }

    private void proceedToNextSongNextUriSupport(String value)
    {
        if (hasNextUriSupport())
        {
            if (StringUtils.isEmpty(value))
            {
                log.debug("device has no current track uri set.");
                return;
            }

            if (isPlaylistPlaying())
            {
                if (getCurrentTrack() != null && getCurrentTrack().streamingURL.equals(value))
                {
                    log.debug("media renderer device is playing current song : " + value);
                }
                else if (nextSongUrl != null && nextSongUrl.contentEquals(value))
                {
                    log.debug("media renderer device proceeded to next song in queue : " + value);
                    moveToNextTrack();
                    setNextSongFromQueue();
                }
                else
                {
                    log.info("media renderer device is controlled by another device. pause playing queue until playback stops ... current uri : " + value);
                }
            }
        }
    }

    private boolean isPlaylistPlaying()
    {
        return TransportState.Playing.name().equals(state.TransportState);
    }

    /**
     * Initializes playlist for
     */
    public void initPlaylist()
    {
        currentSongIdx = 0;
        playbackItems = getFillStrategy().init(playlistItems);
        updateSongUrls();
    }

    private void updateSongUrls()
    {
        songUrls.clear();
        for (MusicItemDto song : playlistItems)
        {
            songUrls.add(song.streamingURL);
        }
    }

    /**
     * returns playlist strategy depending on shuffle mode
     * 
     * @return
     */
    private IPlaylistFillStrategy getFillStrategy()
    {
        if (isRandomPlayback())
        {
            return new RandomPlaybackStrategy();
        }
        else
        {
            return new SequencialPlaybackStrategy();
        }
    }

    /**
     * @param url
     * @return true if url is the current active song url
     */
    public boolean isActiveSongUrl(String url)
    {
        return url.equals(playlistItems.get(currentSongIdx).streamingURL);
    }

    public boolean isSongInPlaylist(String url)
    {
        return songUrls.contains(url);
    }

    public boolean isRandomPlayback()
    {
        return state.Shuffle;
    }

    public void setRandomPlayback(boolean randomPlayback)
    {
        state.Shuffle = randomPlayback;
        initPlaylist();
    }

    public boolean isRepeatPlayback()
    {
        return state.Repeat;
    }

    public void setRepeatPlayback(boolean repeatPlayback)
    {
        this.state.Repeat = repeatPlayback;
        getDevice().getEventPublisher().publishEvent(state);
    }

    public List<MusicItemDto> getPlaylist()
    {
        return Collections.unmodifiableList(playlistItems);
    }

    public MusicItemDto getCurrentTrack()
    {
        if (playlistItems.isEmpty())
        {
            return null;
        }
        return playlistItems.get(playbackItems.get(currentSongIdx));
    }

    /**
     * Initializes new playlist
     * 
     * @param songs
     */
    public void setPlaylist(LinkedList<MusicItemDto> playlist)
    {
        this.playlistItems = playlist;
        getEventPublisher().publishEvent(createPlaylistEventObject());
    }

    public void addSongToPlaylist(MusicItemDto song)
    {
        addSongToPlaylist(song, playbackItems.size());
    }

    public void addSongToPlaylist(MusicItemDto song, int position)
    {
        if (!playlistItems.contains(song))
        {
            playlistItems.addLast(song);
            playbackItems = getFillStrategy().addElement(playbackItems, position);
            updateSongUrls();
            getEventPublisher().publishEvent(createPlaylistEventObject());
        }
        else
        {
            throw new BackendException(BackendException.PLAYLIST_ALREADY_ADDED, "song already added");
        }
    }

    public int addAllSongToPlaylist(List<MusicItemDto> songsToAdd)
    {
        // remove double entries ...
        songsToAdd.removeAll(playlistItems);

        if (!songsToAdd.isEmpty())
        {
            playlistItems.addAll(songsToAdd);
            updateSongUrls();
            int firstElementPosition = playlistItems.indexOf(songsToAdd.get(0));
            int lastElementPosition = playlistItems.indexOf(songsToAdd.get(songsToAdd.size() - 1));
            playbackItems = getFillStrategy().addAllElement(playbackItems, firstElementPosition, lastElementPosition);
            getEventPublisher().publishEvent(createPlaylistEventObject());
            return songsToAdd.size();
        }
        return 0;
    }

    public void removeSongFromPlaylist(MusicItemDto song)
    {
        if (playlistItems.remove(song))
        {
            getEventPublisher().publishEvent(createPlaylistEventObject());
        }
        updateSongUrls();
    }

    public void clear()
    {
        reset();

        getEventPublisher().publishEvent(createPlaylistEventObject());

        if (log.isInfoEnabled())
        {
            log.info("playlist cleared.");
        }
    }

    private PlaylistChangedEvent createPlaylistEventObject()
    {
        RendererPlaylist play = new RendererPlaylist(getDevice().getUDN().getIdentifierString(), getPlaylist());
        return new PlaylistChangedEvent(play);
    }

    private Integer peekNextSongIndex()
    {
        int peekSongIdx = this.currentSongIdx;

        if (peekSongIdx < playbackItems.size() - 1)
        {
            peekSongIdx++;
        }
        else
        {
            if (state.Repeat)
            {
                if (playbackItems.size() > 0)
                {
                    peekSongIdx = 0;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        return peekSongIdx;
    }

    private MusicItemDto peekNextSongItem()
    {
        if (peekNextSongIndex() != null)
        {
            return playlistItems.get(playbackItems.get(peekNextSongIndex()));
        }
        else
        {
            return null;
        }
    }

    /**
     * moves the currentSongIndex to next track. No AV Transport Services are called here.
     * 
     * @return current selected track
     */
    public MusicItemDto moveToNextTrack()
    {
        log.info("moving to next track ... ");

        Integer nextSongIdx = peekNextSongIndex();

        if (nextSongIdx == null)
        {
            log.info("reached end of playlist. stop playing ... ");
            stop();
            return null;
        }
        else if (nextSongIdx < currentSongIdx)
        {
            log.info("reached end of playlist. ststart from beginning ... ");
        }

        currentSongIdx = nextSongIdx;

        if (log.isInfoEnabled())
        {
            log.info(String.format("moving to next track : %s", playlistItems.get(currentSongIdx)));
        }

        return playlistItems.get(currentSongIdx);
    }

    /**
     * 
     * @return
     */
    public MusicItemDto peekNextTrack()
    {
        if (playbackItems.peekFirst() != null)
        {
            return playlistItems.get(playbackItems.peekFirst());
        }
        else
        {
            return null;
        }
    }

    /**
     * If an active playlist song is manually played (by clicking) this song will be consumed for this run.
     * 
     * @param currentSong
     */
    public void consume(MusicItemDto currentSong)
    {
        // Maybe later ... check user experience first.
    }

    /**
     * Removes top song;
     */
    public MusicItemDto consumeHead()
    {
        MusicItemDto headSong = null;
        if (!playbackItems.isEmpty())
        {
            headSong = playlistItems.get(playbackItems.removeFirst());
            String consumedUrl = headSong.streamingURL;
            oldPlaybackItems.add(consumedUrl);
            if (playbackItems.isEmpty() && isRepeatPlayback())
            {
                log.info("repeat playback: init playlist and play again ... ");
                initPlaylist();
            }
        }
        return headSong;
    }

    public MusicItemDto getFileDtoFromURI(String newCurrentTrackURI)
    {
        for (MusicItemDto MusicItemDto : playlistItems)
        {
            if (MusicItemDto.streamingURL.equals(newCurrentTrackURI))
            {
                return MusicItemDto;
            }
        }
        return null;
    }

    public boolean isAlreadyPlayedSong(String songUrl)
    {
        return oldPlaybackItems.contains(songUrl);
    }

    @Override
    public void setShuffle(boolean inp)
    {
        setRandomPlayback(inp);
    }

    @Override
    public void pause()
    {
        setPlaylistTransportState(TransportState.Paused);
        getDevice().getAvTransportServiceBridge().pause();
        getEventPublisher().publishEvent(state);
    }

    @Override
    public long getTracksMax()
    {
        return 16384;
    }

    @Override
    public boolean getShuffle()
    {
        return false;
    }

    @Override
    public void deleteAll()
    {
        clear();
    }

    @Override
    public boolean getRepeat()
    {
        return state.Repeat;
    }

    @Override
    public void deleteId(long id)
    {
        Optional<MusicItemDto> item = playlistItems.stream().filter(musicItem -> musicItem.objectID.equals(Long.toString(id))).findFirst();
        if (item.isPresent())
        {
            removeSongFromPlaylist(item.get());
        }
    }

    @Override
    public void play()
    {
        setPlaylistTransportState(TransportState.Playing);

        MusicItemDto song = getCurrentTrack();
        if (song == null)
        {
            log.info("cannot play playlist. playlist is empty.");
            return;
        }

        log.debug("set first song url  : " + song.streamingURL);
        log.debug("set first song meta : " + song.currentTrackMetadata);

        getDevice().getAvTransportServiceBridge().setUrl(song.streamingURL, song.currentTrackMetadata);
        getDevice().getAvTransportServiceBridge().play();

        setNextSongFromQueue();

        getEventPublisher().publishEvent(state);
    }

    private void setPlaylistTransportState(TransportState newState)
    {
        state.TransportState = newState.name();
        getEventPublisher().publishEvent(state);
    }

    /**
     * 
     * @param song
     */
    private void setNextSongFromQueue()
    {
        if (hasNextUriSupport())
        {
            MusicItemDto nextSong = peekNextSongItem();
            if (nextSong != null)
            {
                nextSongUrl = nextSong.streamingURL;
                getDevice().getAvTransportServiceBridge().setNextUrl(nextSong.streamingURL, nextSong.currentTrackMetadata);
            }
            else
            {
                log.info("playing last song in playlist queue.");
            }
        }
        else
        {
            log.debug("NO 'setNextUrl' support by device.");
        }
    }

    @Override
    public void next()
    {
        getDevice().getAvTransportServiceBridge().next();
        // moveToNextTrack();
    }

    @Override
    public void setRepeat(boolean repeat)
    {
        setRepeatPlayback(repeat);
    }

    @Override
    public void previous()
    {

    }

    @Override
    public void seekId(long id)
    {
        // TODO Auto-generated method stub

    }

    public long insert(MusicItemDto song)
    {
        // TODO : handle inp.AfterId

        addSongToPlaylist(song);
        return 0;
    }

    @Override
    public void seekSecondRelative(int sec)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void seekSecondAbsolute(long sec)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public PlaylistState getState()
    {
        return state;
    }

    @Override
    public List<MusicItemDto> getPlaylistItems()
    {
        return playlistItems;
    }

    @Override
    public void stop()
    {
        setPlaylistTransportState(TransportState.Stopped);
        getDevice().getAvTransportServiceBridge().stop();
        getEventPublisher().publishEvent(state);
    }

    @Override
    public void insertContainer(ContainerItemDto items)
    {
        addAllSongToPlaylist(items.musicItemDto);
    }

    @Override
    public long insert(InsertInput inp)
    {
        MusicItemDto song = getDtoBuilder().extractXmlAsMusicItem(inp.Metadata);
        song.streamingURL = inp.Uri;
        song.currentTrackMetadata = inp.Metadata;

        insert(song);
        // TODO return actual position
        return 0;
    }

    @Override
    public void insertAndPlayContainer(ContainerItemDto items)
    {
        insertContainer(items);
        play();
    }

}
