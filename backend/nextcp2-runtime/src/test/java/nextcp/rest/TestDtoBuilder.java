package nextcp.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.jupnp.support.model.DIDLAttribute;
import org.jupnp.support.model.DIDLObject.Property;
import org.jupnp.support.model.ProtocolInfo;
import org.jupnp.support.model.Res;
import org.jupnp.support.model.item.MusicTrack;
import nextcp.dto.AudioFormat;
import nextcp.dto.MusicItemDto;

public class TestDtoBuilder
{
    @Test
    public void testAudioDuration()
    {
        DtoBuilder db = new DtoBuilder();
        Res res = buildResObject("3:20"); // 3*60 + 20 = 200L
        AudioFormat af = db.extractAudioFormatFromResourceField(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("03:20"); // 3*60 + 20 = 200L
        af = db.extractAudioFormatFromResourceField(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("3:20.0"); // 3*60 + 20 = 200L
        af = db.extractAudioFormatFromResourceField(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("3:20.00"); // 3*60 + 20 = 200L
        af = db.extractAudioFormatFromResourceField(res);
        assertTrue(af.durationInSeconds.equals(200L));

        res = buildResObject("0:3:20.0"); // 3*60 + 20 = 200L
        af = db.extractAudioFormatFromResourceField(res);
        assertTrue(af.durationInSeconds.equals(3 * 60L + 20L));

        res = buildResObject("12:4:59.0"); // 3*60 + 20 = 200L
        af = db.extractAudioFormatFromResourceField(res);
        assertTrue(af.durationInSeconds.equals(12 * 60 * 60L + 4 * 60L + 59L));
    }

    @Test
    public void testSelectsLargestAlbumArtProfile()
    {
        DtoBuilder db = new DtoBuilder();
        MusicTrack track = new MusicTrack();
        track.addProperty(albumArtUri("http://ums:5001/get/0$1$5/thumbnail0000JPEG_TN_cover.jpg", "JPEG_TN"));
        track.addProperty(albumArtUri("http://ums:5001/get/0$1$5/thumbnail0000JPEG_MED_cover.jpg", "JPEG_MED"));

        // The largest advertised profile wins, no matter in which order the server lists them.
        assertEquals("http://ums:5001/get/0$1$5/thumbnail0000JPEG_LRG_cover.jpg", db.selectLargeAlbumArtUri(track));
    }

    @Test
    public void testKeepsSingleAlbumArtWithoutProfile()
    {
        DtoBuilder db = new DtoBuilder();
        MusicTrack track = new MusicTrack();
        String uri = "http://minim:9790/minimserver/*/music/album/$!picture-176-167405.jpg";
        track.addProperty(albumArtUri(uri, null));

        assertEquals(uri, db.selectLargeAlbumArtUri(track));
        assertNull(db.selectLargeAlbumArtUri(new MusicTrack()));
    }

    @Test
    public void testRewritesUmsThumbnailProfileOnly()
    {
        DtoBuilder db = new DtoBuilder();
        assertEquals("http://ums:5001/get/0$1$5/thumbnail0000JPEG_LRG_a.jpg",
                db.withUmsThumbnailProfile("http://ums:5001/get/0$1$5/thumbnail0000JPEG_TN_a.jpg", "JPEG_LRG"));
        // A large URL is scaled back down for the grid variant.
        assertEquals("http://ums:5001/get/0$1$5/thumbnail0000JPEG_MED_a.jpg",
                db.withUmsThumbnailProfile("http://ums:5001/get/0$1$5/thumbnail0000JPEG_LRG_a.jpg", "JPEG_MED"));
        // Already the wanted profile, and URLs of other servers, must stay untouched.
        assertEquals("http://ums:5001/get/0$1$5/thumbnail0000JPEG_LRG_a.jpg",
                db.withUmsThumbnailProfile("http://ums:5001/get/0$1$5/thumbnail0000JPEG_LRG_a.jpg", "JPEG_LRG"));
        assertEquals("http://minim:9790/music/$!picture-1-2.jpg",
                db.withUmsThumbnailProfile("http://minim:9790/music/$!picture-1-2.jpg", "JPEG_LRG"));
        assertNull(db.withUmsThumbnailProfile(null, "JPEG_LRG"));
    }

    @Test
    public void testSelectsMediumProfileForGridTiles()
    {
        DtoBuilder db = new DtoBuilder();
        MusicTrack track = new MusicTrack();
        track.addProperty(albumArtUri("http://ums:5001/get/0$1$5/thumbnail0000JPEG_TN_cover.jpg", "JPEG_TN"));

        // A grid tile is small, so it asks UMS for the mid size, not the full size cover.
        assertEquals("http://ums:5001/get/0$1$5/thumbnail0000JPEG_MED_cover.jpg", db.selectMediumAlbumArtUri(track));

        // A server that offers no profiles at all can only be taken as-is.
        MusicTrack minim = new MusicTrack();
        String uri = "http://minim:9790/minimserver/*/music/album/$!picture-176-167405.jpg";
        minim.addProperty(albumArtUri(uri, null));
        assertEquals(uri, db.selectMediumAlbumArtUri(minim));
        assertNull(db.selectMediumAlbumArtUri(new MusicTrack()));
    }

    @Test
    public void testWebRadioEntryStaysPlayableWithoutAudioContentFormat()
    {
        DtoBuilder db = new DtoBuilder();

        // A server side radio playlist regularly announces something generic instead of audio/*.
        // Refusing that left the entry without a stream URL, and the UI could only do nothing.
        assertEquals("http://radio.example/stream/piano",
                db.extractXmlAsMusicItem(radioEntry("http-get:*:application/octet-stream:*")).streamingURL);
        assertEquals("http://radio.example/stream/piano",
                db.extractXmlAsMusicItem(radioEntry("http-get:*:audio/mpeg:*")).streamingURL);

        // Without any resource there is nothing to fall back to.
        String withoutRes = radioEntry("http-get:*:audio/mpeg:*").replaceAll("<res[^>]*>[^<]*</res>", "");
        assertEquals("", db.extractXmlAsMusicItem(withoutRes).streamingURL);
    }

    @Test
    public void testWebRadioIsMarkedAsStreamingAndKeepsStationMetadata()
    {
        DtoBuilder db = new DtoBuilder();

        // A live stream announces neither a size nor a duration, so the size heuristic cannot decide.
        // The audioBroadcast class can, and the station tags arrive as upnp:genre.
        MusicItemDto item = db.extractXmlAsMusicItem(radioEntry("http-get:*:audio/mpeg:*"));
        assertTrue(item.audioFormat.isStreaming);
        assertNull(item.audioFormat.durationInSeconds);
        assertEquals("classic rock / oldies", item.genre);
        assertEquals(Integer.valueOf(4), item.rating);

        // A server that does not even announce an audio content format still yields the flag.
        item = db.extractXmlAsMusicItem(radioEntry("http-get:*:application/octet-stream:*"));
        assertTrue(item.audioFormat.isStreaming);
    }

    private String radioEntry(String protocolInfo)
    {
        return "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\""
                + " xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\">"
                + "<item id=\"pl$3$1\" parentID=\"pl$3\" restricted=\"1\">"
                + "<dc:title>Radio Art - Piano</dc:title>"
                + "<upnp:genre>classic rock / oldies</upnp:genre>"
                + "<upnp:rating>4</upnp:rating>"
                + "<upnp:class>object.item.audioItem.audioBroadcast</upnp:class>"
                + "<res protocolInfo=\"" + protocolInfo + "\">http://radio.example/stream/piano</res>"
                + "</item></DIDL-Lite>";
    }

    private Property<URI> albumArtUri(String uri, String dlnaProfile)
    {
        Property<URI> property = new Property.UPNP.ALBUM_ART_URI(URI.create(uri));
        if (dlnaProfile != null)
        {
            property.addAttribute(new Property.DLNA.PROFILE_ID(
                    new DIDLAttribute(Property.DLNA.NAMESPACE.URI, "dlna", dlnaProfile)));
        }
        return property;
    }

    private Res buildResObject(String duration)
    {
        Res res = new Res();
        res.setProtocolInfo(new ProtocolInfo("*:*:audio:*"));
        res.setBitsPerSample(2L);
        res.setBitrate(192 * 125L);
        res.setSampleFrequency(1140L);
        res.setNrAudioChannels(2L);
        res.setDuration(duration);
        return res;
    }
}
