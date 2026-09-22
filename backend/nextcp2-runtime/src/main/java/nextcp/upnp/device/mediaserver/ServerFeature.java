package nextcp.upnp.device.mediaserver;

/**
 * What a media server can do, read off the actions it announces instead of off a version number.
 *
 * Part of the REST contract ({@code MediaServerDto.features}), mirrored in {@code service/server-feature.ts}.
 * Add, never rename.
 */
public final class ServerFeature {

	/** The server answers UPnP Search, so the global search box reaches the whole library. */
	public static final String UPNP_SEARCH = "UPNP_SEARCH";

	/** The server can search on {@code upnp:rating}, which is how the sidebar finds liked playlists. */
	public static final String LIKED_PLAYLISTS = "LIKED_PLAYLISTS";

	/** Albums and containers can be liked and unliked. */
	public static final String ALBUM_LIKES = "ALBUM_LIKES";

	/** A star rating can be written into the audio file's metadata. */
	public static final String RATING_TAG = "RATING_TAG";

	/** Ratings and likes can be written to, and read back from, a backup file on the server. */
	public static final String RATING_BACKUP = "RATING_BACKUP";

	/** Stations can be searched at radio-browser.info and added to a server side playlist. */
	public static final String RADIO_BROWSER = "RADIO_BROWSER";

	/** The server reports what a web stream is playing, and pushes every change. */
	public static final String WEB_STREAM_NOW_PLAYING = "WEB_STREAM_NOW_PLAYING";

	/** How a station's ICY title is to be read can be stored per station. */
	public static final String WEB_STREAM_ICY_ORDER = "WEB_STREAM_ICY_ORDER";

	/** The server streams the Audio Addict networks for a subscription nextcp hands it. */
	public static final String AUDIO_ADDICT = "AUDIO_ADDICT";

	/** The folder holding the artist folders can be stored on the server, for artist images. */
	public static final String ARTIST_FOLDER = "ARTIST_FOLDER";

	/** The media library, or one folder of it, can be rescanned on request. */
	public static final String MEDIA_RESCAN = "MEDIA_RESCAN";

	/** The server reports what a curated playlist is playing right now. */
	public static final String PLAYLIST_NOW_PLAYING = "PLAYLIST_NOW_PLAYING";

	private ServerFeature() {
	}
}
