/**
 * What the selected media server can do, as reported in `MediaServerDto.features`.
 *
 * Mirrors `nextcp.upnp.device.mediaserver.ServerFeature` on the backend. Add, never rename.
 */
export const ServerFeature = {
  /** UPnP Search, so the global search box reaches the whole library. */
  UPNP_SEARCH: 'UPNP_SEARCH',
  /** Search on upnp:rating, which is how the sidebar finds liked playlists. */
  LIKED_PLAYLISTS: 'LIKED_PLAYLISTS',
  /** Albums and containers can be liked and unliked. */
  ALBUM_LIKES: 'ALBUM_LIKES',
  /** A star rating can be written into the audio file's metadata. */
  RATING_TAG: 'RATING_TAG',
  /** Ratings and likes can be backed up on the server and restored from there. */
  RATING_BACKUP: 'RATING_BACKUP',
  /** Stations can be searched at radio-browser.info and added to a playlist. */
  RADIO_BROWSER: 'RADIO_BROWSER',
  /** The server reports, and pushes, what a web stream is playing. */
  WEB_STREAM_NOW_PLAYING: 'WEB_STREAM_NOW_PLAYING',
  /** How a station's ICY title is read can be stored per station. */
  WEB_STREAM_ICY_ORDER: 'WEB_STREAM_ICY_ORDER',
  /** The Audio Addict networks are streamed by the server. */
  AUDIO_ADDICT: 'AUDIO_ADDICT',
  /** The folder holding the artist folders can be stored on the server. */
  ARTIST_FOLDER: 'ARTIST_FOLDER',
  /** The media library, or one folder of it, can be rescanned. */
  MEDIA_RESCAN: 'MEDIA_RESCAN',
  /** The server reports what a curated playlist is playing right now. */
  PLAYLIST_NOW_PLAYING: 'PLAYLIST_NOW_PLAYING',
} as const;

export type ServerFeatureName =
  (typeof ServerFeature)[keyof typeof ServerFeature];

/**
 * What to tell someone whose media server cannot do this - in their words, not in tokens.
 *
 * Detection asks per action on purpose, but a user cannot act on "no ALBUM_LIKES"; they can act on
 * "UMS 16 or newer". So the version belongs here, in the sentence, and nowhere in the logic.
 */
export const FEATURE_REQUIREMENT: Record<ServerFeatureName, string> = {
  UPNP_SEARCH:
    'Searching the whole library needs a media server that answers UPnP search, such as UMS or MinimServer.',
  LIKED_PLAYLISTS:
    'Liked playlists need a media server that can search on ratings. UMS 16 or newer can.',
  ALBUM_LIKES:
    'Liking albums and folders needs a media server that stores ratings. UMS 16 or newer does.',
  RATING_TAG:
    'Writing a star rating into the audio file needs UMS 16 or newer.',
  RATING_BACKUP:
    'Backing up and restoring ratings is done by the media server. UMS 16 or newer offers it.',
  RADIO_BROWSER:
    'Searching radio stations runs on the media server. UMS 16 or newer carries the radio-browser client.',
  WEB_STREAM_NOW_PLAYING:
    'Live titles of web streams are read by the media server. UMS 16 or newer does that.',
  WEB_STREAM_ICY_ORDER:
    'How a station announces its titles is stored by the media server. UMS 16 or newer can.',
  AUDIO_ADDICT:
    'The Audio Addict networks are streamed by the media server. UMS 16 or newer offers them.',
  ARTIST_FOLDER:
    'Storing the artist folder needs UMS 16 or newer.',
  MEDIA_RESCAN:
    'Rescanning the library is done by the media server. UMS 16 or newer offers it.',
  PLAYLIST_NOW_PLAYING:
    'What a curated playlist is playing is reported by the media server. UMS 16 or newer does that.',
};
