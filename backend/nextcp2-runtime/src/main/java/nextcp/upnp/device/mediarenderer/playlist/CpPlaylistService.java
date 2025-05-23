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
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.InsertInput;
import nextcp.util.BackendException;

/**
 * central playlist service. Implementation sucks a little.
 * 
 */
public class CpPlaylistService extends BaseAvTransportChangeEventImpl implements IPlaylistService {

	public enum TransportState {
		Stopped, Playing, Paused, Buffering
	};

	private static final Logger log = LoggerFactory.getLogger(CpPlaylistService.class.getName());

	private static final Long MAX_TRACKS = 16384L;

	//
	// state handling
	// ===========================================================================================

	private PlaylistState state = new PlaylistState();

	// Playlist with songs
	private LinkedList<MusicItemDto> playlistItems = new LinkedList<>();

	// Index list : intention is to define play order. Songs will be played from
	// start to end of this list.
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

	public CpPlaylistService(MediaRendererDevice device) {
		this.device = device;
		state.udn = device.getUdnAsString();
		state.Id = 0L;
		state.ProtocolInfo = "";
		state.Repeat = false;
		state.Shuffle = false;
		state.TracksMax = MAX_TRACKS;
		state.TransportState = TransportState.Stopped.name();

		if (device != null) {
			this.device = device;
			if (device.hasUpnpAvTransport()) {
				log.info("starting internal playlist implementation for device : " + device.getFriendlyName());
				getEventPublisher().publishEvent(state);
			}
		}
	}

	protected MediaRendererDevice getDevice() {
		return device;
	}

	private ApplicationEventPublisher getEventPublisher() {
		return getDevice().getEventPublisher();
	}

	private DtoBuilder getDtoBuilder() {
		return getDevice().getDtoBuilder();
	}

	private void reset() {
		currentSongIdx = 0;
		playlistItems.clear();
		playbackItems.clear();
		songUrls.clear();
		oldPlaybackItems.clear();

		updateSongUrls();
	}

	@Override
	public void transportStateChange(String value) {
		if (getDevice().hasProductService() && !"Playlist".equalsIgnoreCase(getDevice().getProductService().getCurrentInputSource().Type)) {
			log.debug("[{}] ignoring transport state change, because input source type is not 'Playlist'",
				getDevice().getFriendlyName());
			return;
		}

		log.debug("[{}] transportStateChange to : {}", getDevice().getFriendlyName(), value);
		if ("PAUSED_PLAYBACK".equalsIgnoreCase(value)) {
			log.debug(
				String.format("[%s] transportStateChange to PAUSED_PLAYBACK. Proceeding to next song ...", getDevice().getFriendlyName()));
			proceedToNextSongToPlayNoNextUriSupport();
		} else if ("STOPPED".equalsIgnoreCase(value) || "TRANSITIONING".equalsIgnoreCase(value)) {
			log.debug(String.format("[%s] transportStateChange to STOPPED/TRANSITIONING. Proceeding to next song ...",
				getDevice().getFriendlyName()));
			proceedToNextSongToPlayNoNextUriSupport();
		} else if ("PLAYING".equalsIgnoreCase(value)) {
			log.debug(String.format("[%s] transportStateChange to PLAYING. Do nothing ...", getDevice().getFriendlyName()));
		} else {
			log.debug(String.format("[%s] not processed : %s", getDevice().getFriendlyName(), value));
		}
	}

	private boolean hasNextUriSupport() {
		log.debug("nextAVTransportUri : " + device.getAvTransportEventListener().getCurrentAvTransportState().NextAVTransportURI);
		return !"NOT_IMPLEMENTED".equalsIgnoreCase(device.getAvTransportEventListener().getCurrentAvTransportState().NextAVTransportURI);
	}

	private void proceedToNextSongToPlayNoNextUriSupport() {
		log.debug("proceedToNextSongToPlayNoNextUriSupport ...");
		// if device has no nextUrl support, it goes into PAUSED state
		if (!hasNextUriSupport()) {
			if (isPlaylistPlaying()) {
				MusicItemDto nextSong = moveToNextTrack();
				if (nextSong != null) {
					log.info(String.format("proceedToNextSongToPlayNoNextUriSupport : set URL to %s", nextSong.streamingURL));
					getDevice().getAvTransportBridge().setUrl(nextSong.streamingURL, nextSong.currentTrackMetadata);
					getDevice().getTransportServiceBridge().play();
				} else {
					log.info("proceedToNextSongToPlayNoNextUriSupport : stopping ...");
					stop();
				}
			}
		} else {
			log.debug("device has nextUriSupport. Do nothing ... ");
		}
	}

	@Override
	public void currentTrackURIChange(String value) {
		log.debug("[{}] currentTrackURIChange to : {}", getDevice().getFriendlyName(), value);
		super.currentTrackURIChange(value);
		proceedToNextSongNextUriSupport(value);
	}

	private void proceedToNextSongNextUriSupport(String value) {
		log.debug("current song index is : " + currentSongIdx);
		log.debug("[{}] proceedToNextSongNextUriSupport ...", getDevice().getFriendlyName());
		if (hasNextUriSupport()) {
			if (StringUtils.isEmpty(value)) {
				log.debug(String.format("[%s] device has no 'current track' uri set.", getDevice().getFriendlyName()));
				return;
			}

			if (isPlaylistPlaying()) {
				if (getCurrentTrack() != null && getCurrentTrack().streamingURL.equals(value)) {
					log.debug("[{}] media renderer device is playing current track : {}", getDevice().getFriendlyName(), value);
				} else if (nextSongUrl != null && nextSongUrl.contentEquals(value)) {
					log.debug("[{}] media renderer device proceeded to next song in queue : {}", getDevice().getFriendlyName(), value);
					moveToNextTrack();
					setNextSongFromQueue();
				} else {
					log.info(
						"[{}] media renderer device is controlled by another device. Pause playing queue until playback stops ... current uri : {}",
						getDevice().getFriendlyName(), value);
				}
			}
		} else {
			log.debug("[{}] device has no nextUriSupport. Do nothing ...", getDevice().getFriendlyName());
		}
	}

	private boolean isPlaylistPlaying() {
		return TransportState.Playing.name().equals(state.TransportState);
	}

	/**
	 * Initializes playlist for
	 */
	public void initPlaylist() {
		currentSongIdx = 0;
		playbackItems = getFillStrategy().init(playlistItems);
		updateSongUrls();
	}

	private void updateSongUrls() {
		songUrls.clear();
		for (MusicItemDto song : playlistItems) {
			songUrls.add(song.streamingURL);
		}
	}

	/**
	 * returns playlist strategy depending on shuffle mode
	 * 
	 * @return
	 */
	private IPlaylistFillStrategy getFillStrategy() {
		if (isRandomPlayback()) {
			return new RandomPlaybackStrategy();
		} else {
			return new SequencialPlaybackStrategy();
		}
	}

	/**
	 * @param url
	 * @return true if url is the current active song url
	 */
	public boolean isActiveSongUrl(String url) {
		return url.equals(playlistItems.get(currentSongIdx).streamingURL);
	}

	public boolean isSongInPlaylist(String url) {
		return songUrls.contains(url);
	}

	public boolean isRandomPlayback() {
		return state.Shuffle;
	}

	public void setRandomPlayback(boolean randomPlayback) {
		state.Shuffle = randomPlayback;
		initPlaylist();
	}

	public boolean isRepeatPlayback() {
		return state.Repeat;
	}

	public void setRepeatPlayback(boolean repeatPlayback) {
		this.state.Repeat = repeatPlayback;
		getDevice().getEventPublisher().publishEvent(state);
	}

	public List<MusicItemDto> getPlaylist() {
		return Collections.unmodifiableList(playlistItems);
	}

	public MusicItemDto getCurrentTrack() {
		if (playlistItems.isEmpty()) {
			log.debug("getCurrentTrack is Empty.");
			return null;
		}
		return playlistItems.get(playbackItems.get(currentSongIdx));
	}

	/**
	 * Initializes new playlist
	 * 
	 * @param songs
	 */
	public void setPlaylist(LinkedList<MusicItemDto> playlist) {
		this.playlistItems = playlist;
		getEventPublisher().publishEvent(createPlaylistEventObject());
	}

	public void addSongToPlaylist(MusicItemDto song) {
		addSongToPlaylist(song, playbackItems.size());
	}

	public void addSongToPlaylist(MusicItemDto song, int position) {
		if (!playlistItems.contains(song)) {
			log.debug("adding song to playlist : {}", song != null ? song.toString() : "NULL");
			playlistItems.addLast(song);
			playbackItems = getFillStrategy().addElement(playbackItems, position);
			updateSongUrls();
			getEventPublisher().publishEvent(createPlaylistEventObject());
		} else {
			throw new BackendException(BackendException.PLAYLIST_ALREADY_ADDED, "song already added");
		}
	}

	public int addAllSongToPlaylist(List<MusicItemDto> songsToAdd) {
		log.debug("addAllSongToPlaylist. Number of songs given : {}", songsToAdd.size());
		// remove double entries ...
		songsToAdd.removeAll(playlistItems);

		if (!songsToAdd.isEmpty()) {
			playlistItems.addAll(songsToAdd);
			updateSongUrls();
			int firstElementPosition = playlistItems.indexOf(songsToAdd.get(0));
			int lastElementPosition = playlistItems.indexOf(songsToAdd.get(songsToAdd.size() - 1));
			if (log.isDebugEnabled()) {
				log.debug(String.format("firstElementPosition : %d / lastElementPosition : %d", firstElementPosition, lastElementPosition));
			}
			playbackItems = getFillStrategy().addAllElement(playbackItems, firstElementPosition, lastElementPosition);
			log.info("added {} songs", songsToAdd.size());
			getEventPublisher().publishEvent(createPlaylistEventObject());
			return songsToAdd.size();
		} else {
			log.info("addAllSongToPlaylist: all songs already in playlist.");
		}
		return 0;
	}

	public void removeSongFromPlaylist(MusicItemDto song) {
		if (playlistItems.remove(song)) {
			getEventPublisher().publishEvent(createPlaylistEventObject());
		}
		updateSongUrls();
	}

	public void clear() {
		reset();

		getEventPublisher().publishEvent(createPlaylistEventObject());

		if (log.isInfoEnabled()) {
			log.info("playlist cleared.");
		}
	}

	private PlaylistChangedEvent createPlaylistEventObject() {
		RendererPlaylist play = new RendererPlaylist(getDevice().getUDN().getIdentifierString(), getPlaylist());
		return new PlaylistChangedEvent(play);
	}

	private Integer peekNextSongIndex() {
		int peekSongIdx = this.currentSongIdx;

		if (peekSongIdx < playbackItems.size() - 1) {
			peekSongIdx++;
		} else {
			if (state.Repeat) {
				log.info("repeat playback ...");
				if (playbackItems.size() > 0) {
					peekSongIdx = 0;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		return peekSongIdx;
	}

	private MusicItemDto peekNextSongItem() {
		if (peekNextSongIndex() != null) {
			return playlistItems.get(playbackItems.get(peekNextSongIndex()));
		} else {
			return null;
		}
	}

	/**
	 * moves the currentSongIndex to next track. No AV Transport Services are
	 * called here.
	 * 
	 * @return current selected track
	 */
	public MusicItemDto moveToNextTrack() {

		Integer nextSongIdx = peekNextSongIndex();

		if (nextSongIdx == null) {
			log.info("reached end of playlist. stop playing ... ");
			stop();
			return null;
		} else if (nextSongIdx < currentSongIdx) {
			log.info("reached end of playlist. Start from beginning ... ");
		}

		currentSongIdx = nextSongIdx;
		log.info("new current song index is : {}", currentSongIdx);

		log.info("moving to next track : {}", playlistItems.get(currentSongIdx).title);

		return playlistItems.get(currentSongIdx);
	}

	/**
	 * 
	 * @return
	 */
	public MusicItemDto peekNextTrack() {
		if (playbackItems.peekFirst() != null) {
			return playlistItems.get(playbackItems.peekFirst());
		} else {
			return null;
		}
	}

	/**
	 * If an active playlist song is manually played (by clicking) this song
	 * will be consumed for this run.
	 * 
	 * @param currentSong
	 */
	public void consume(MusicItemDto currentSong) {
		// Maybe later ... check user experience first.
	}

	/**
	 * Removes top song;
	 */
	public MusicItemDto consumeHead() {
		MusicItemDto headSong = null;
		if (!playbackItems.isEmpty()) {
			headSong = playlistItems.get(playbackItems.removeFirst());
			String consumedUrl = headSong.streamingURL;
			oldPlaybackItems.add(consumedUrl);
			if (playbackItems.isEmpty() && isRepeatPlayback()) {
				log.info("repeat playback: init playlist and play again ... ");
				initPlaylist();
			}
		}
		return headSong;
	}

	public MusicItemDto getFileDtoFromURI(String newCurrentTrackURI) {
		for (MusicItemDto MusicItemDto : playlistItems) {
			if (MusicItemDto.streamingURL.equals(newCurrentTrackURI)) {
				return MusicItemDto;
			}
		}
		return null;
	}

	public boolean isAlreadyPlayedSong(String songUrl) {
		return oldPlaybackItems.contains(songUrl);
	}

	@Override
	public void setShuffle(boolean inp) {
		setRandomPlayback(inp);
	}

	@Override
	public void pause() {
		setPlaylistTransportState(TransportState.Paused);
		getDevice().getTransportServiceBridge().pause();
		getEventPublisher().publishEvent(state);
	}

	@Override
	public long getTracksMax() {
		return 16384;
	}

	@Override
	public boolean getShuffle() {
		return false;
	}

	@Override
	public void deleteAll() {
		clear();
	}

	@Override
	public boolean getRepeat() {
		return state.Repeat;
	}

	@Override
	public void deleteId(long id) {
		Optional<MusicItemDto> item = playlistItems.stream().filter(musicItem -> musicItem.objectID.equals(Long.toString(id))).findFirst();
		if (item.isPresent()) {
			removeSongFromPlaylist(item.get());
		}
	}

	@Override
	public void play() {
		device.checkServicesOnline();
		setPlaylistTransportState(TransportState.Playing);

		MusicItemDto song = getCurrentTrack();
		if (song == null) {
			log.info("cannot play playlist. playlist is empty.");
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("set first song url  : {}", song.streamingURL);
			log.debug("set first song meta : {}", song.currentTrackMetadata);
		}

		getDevice().getAvTransportBridge().setUrl(song.streamingURL, song.currentTrackMetadata);
		getDevice().getTransportServiceBridge().play();
		getDevice().setServicesOffline(false);

		setNextSongFromQueue();

		getEventPublisher().publishEvent(state);
	}

	private void setPlaylistTransportState(TransportState newState) {
		state.TransportState = newState.name();
		getEventPublisher().publishEvent(state);
	}

	/**
	 * 
	 * @param song
	 */
	private void setNextSongFromQueue() {
		if (hasNextUriSupport()) {
			MusicItemDto nextSong = peekNextSongItem();
			if (nextSong != null) {
				nextSongUrl = nextSong.streamingURL;
				log.info("setting nextUrl to song : {} ", nextSong.title);
				getDevice().getAvTransportBridge().setNextUrl(nextSong.streamingURL, nextSong.currentTrackMetadata);
			} else {
				log.info("playing last song in playlist queue. NextUrl not set.");
			}
		} else {
			log.debug("NO 'setNextUrl' support by device.");
		}
	}

	@Override
	public void next() {
		getDevice().getTransportServiceBridge().next();
		// moveToNextTrack();
	}

	@Override
	public void setRepeat(boolean repeat) {
		setRepeatPlayback(repeat);
	}

	@Override
	public void previous() {
		log.warn("previous NOT IMPLEMENTED");
	}

	@Override
	public void seekId(long id) {
		log.warn("seekId NOT IMPLEMENTED");
	}

	@Override
	public boolean seekId(String streamUrl) {
		log.info("seeking to url {} ", streamUrl);
		Optional<MusicItemDto> item = playlistItems.stream().filter(musicItem -> musicItem.streamingURL.equals(streamUrl)).findFirst();
		if (item.isPresent()) {
			MusicItemDto dto = item.get();
			int idxSong = playlistItems.indexOf(dto);
			log.info("found song with title {} at index {} ", dto.title, idxSong);
			if (state.Shuffle) {
				boolean removed = playbackItems.remove(Integer.valueOf(idxSong));
				if (!removed) {
					log.warn("playbackIndex couldn't be found for index " + idxSong + " : " + dto);
					return false;
				}
				playbackItems.addFirst(idxSong);
			} else {
				log.info("linear play mode. moving to index : " + idxSong);
				for (int i = 0; i < idxSong; i++) {
					moveToNextTrack();
				}
			}
			play();
			return true;
		}
		return false;
	}

	public long insert(MusicItemDto song) {
		// TODO : handle inp.AfterId
		addSongToPlaylist(song);
		return 0;
	}

	@Override
	public void seekSecondRelative(int sec) {
		log.warn("seekSecondRelative NOT IMPLEMENTED");
	}

	@Override
	public void seekSecondAbsolute(long sec) {
		log.warn("seekSecondAbsolute NOT IMPLEMENTED");
	}

	@Override
	public PlaylistState getState() {
		return state;
	}

	@Override
	public List<MusicItemDto> getPlaylistItems() {
		return playlistItems;
	}

	@Override
	public void stop() {
		log.info("stop playing playlist ...");
		setPlaylistTransportState(TransportState.Stopped);
		getDevice().getTransportServiceBridge().stop();
		getEventPublisher().publishEvent(state);
	}

	@Override
	public void insertContainer(ContainerItemDto items) {
		addAllSongToPlaylist(items.musicItemDto);
	}

	@Override
	public long insertLast(InsertInput inp) {
		MusicItemDto song = getDtoBuilder().extractXmlAsMusicItem(inp.Metadata);
		song.streamingURL = inp.Uri;
		song.currentTrackMetadata = inp.Metadata;

		insert(song);
		// TODO return actual position
		return 0;
	}

	@Override
	public void insertAndPlayContainer(ContainerItemDto items) {
		reset();
		log.info("inserting container {} to current playlist. Container has {} entries.", items.currentContainer.title,
			items.musicItemDto.size());
		insertContainer(items);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			log.warn("interrupted ... ", e);
		}
		play();
	}
	
	@Override
	synchronized public void insertNext(InsertInput inp) {
		MusicItemDto song = getDtoBuilder().extractXmlAsMusicItem(inp.Metadata);
		song.streamingURL = inp.Uri;
		song.currentTrackMetadata = inp.Metadata;
		int nextsongIndex = currentSongIdx +1;
		log.info("adding at song at next position {} : {}", nextsongIndex, song.title);
		playlistItems.add(song);
		playbackItems.add(nextsongIndex, playlistItems.indexOf(song));
	}
}
