package nextcp.upnp.device.mediaserver;

/**
 * The track currently playing on an AudioAddict curated playlist, as reported by the UMS media
 * server via the UmsExtendedServices {@code getPlaylistNowPlaying} action. Unlike radio channels,
 * a playlist's current track is UMS-internal playback state and cannot be queried from AudioAddict
 * directly.
 */
public class AudioAddictPlaylistNowPlaying
{
    public final String artist;
    public final String title;
    public final String artUrl;

    public AudioAddictPlaylistNowPlaying(String artist, String title, String artUrl)
    {
        this.artist = artist;
        this.title = title;
        this.artUrl = artUrl;
    }
}
