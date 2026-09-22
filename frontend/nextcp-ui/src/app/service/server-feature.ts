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
