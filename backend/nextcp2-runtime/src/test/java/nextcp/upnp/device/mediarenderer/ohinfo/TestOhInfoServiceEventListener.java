package nextcp.upnp.device.mediarenderer.ohinfo;

import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.meta.DeviceDetails;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteDeviceIdentity;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.UDADeviceType;
import org.jupnp.model.types.UDN;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import nextcp.dto.TrackInfoDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.CountersOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.DetailsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.MetatextOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.TrackOutput;
import static org.junit.jupiter.api.Assertions.*;

class TestOhInfoServiceEventListener {
    private final List<Object> published = new ArrayList<>();

    private OhInfoServiceEventListener listener() throws Exception {
        RemoteDevice remote = new RemoteDevice(
            new RemoteDeviceIdentity(new UDN("info-test"), 600, URI.create("http://127.0.0.1/device").toURL(), null, InetAddress.getLoopbackAddress()),
            new UDADeviceType("MediaRenderer"), new DeviceDetails("Test renderer"), new RemoteService[0]);
        DtoBuilder builder = new DtoBuilder();
        MediaRendererDevice renderer = new MediaRendererDevice(remote, true) {
            @Override public ApplicationEventPublisher getEventPublisher() { return published::add; }
            @Override public DtoBuilder getDtoBuilder() { return builder; }
        };
        return new OhInfoServiceEventListener(renderer);
    }

    private String metadata(String title) {
        return """
            <DIDL-Lite xmlns="urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:upnp="urn:schemas-upnp-org:metadata-1-0/upnp/">
              <item id="track-1" parentID="0" restricted="1">
                <dc:title>%s</dc:title><upnp:artist>Queen</upnp:artist>
                <upnp:albumArtURI>http://example.test/cover.jpg</upnp:albumArtURI>
                <upnp:class>object.item.audioItem.musicTrack</upnp:class>
                <res protocolInfo="http-get:*:audio/mpeg:*">http://example.test/audio</res>
              </item>
            </DIDL-Lite>
            """.formatted(title);
    }

    @Test
    void partialRadioUpdatesKeepTheCoverAndTrackChangesReplaceTheTitle() throws Exception {
        OhInfoServiceEventListener listener = listener();
        assertFalse(listener.isReceivingEvents());
        listener.metadataChange(metadata("Radio"));
        listener.metatextCountChange(1L);
        listener.metatextChange("Queen - Flash");
        listener.eventProcessed();
        TrackInfoDto first = (TrackInfoDto) published.getLast();
        assertEquals("info-test", first.mediaRendererUdn);
        assertEquals("Flash", first.currentTrack.title);
        assertEquals("Queen", first.currentTrack.artistName);
        assertEquals("Radio", first.currentTrack.album);
        assertEquals("http://example.test/cover.jpg", first.currentTrack.albumArtUrl);
        assertTrue(listener.isReceivingEvents());
        listener.metatextChange("Queen - Another One Bites The Dust");
        listener.eventProcessed();
        assertEquals("Another One Bites The Dust", ((TrackInfoDto) published.getLast()).currentTrack.title);
        listener.metatextChange("");
        listener.metadataChange(metadata("Flash (Album)"));
        listener.eventProcessed();
        assertEquals("Flash (Album)", ((TrackInfoDto) published.getLast()).currentTrack.title);
        listener.ended(CancelReason.RENEWAL_FAILED, null);
        assertFalse(listener.isReceivingEvents());
    }

    @Test
    void snapshotsCarryDetailsAndDoNotHideFailureOrOverwriteNewerEvents() throws Exception {
        OhInfoServiceEventListener listener = listener();
        listener.metadataChange(metadata("New track"));
        DetailsOutput details = new DetailsOutput();
        details.Duration = 180L;
        details.SampleRate = 44100L;
        details.BitDepth = 16L;
        details.CodecName = "FLAC";
        CountersOutput counters = new CountersOutput();
        counters.TrackCount = 42L;
        counters.MetatextCount = 0L;
        TrackOutput track = new TrackOutput();
        track.Metadata = metadata("Old track");
        track.Uri = "http://example.test/audio";
        MetatextOutput metatext = new MetatextOutput();
        metatext.Value = "";
        TrackInfoDto snapshot = listener.fromSnapshot(details, counters, track, metatext);
        assertEquals("Old track", snapshot.currentTrack.title);
        assertEquals("03:00", snapshot.duration);
        assertEquals(44100L, snapshot.sampleRate);
        assertEquals(16L, snapshot.bitDepth);
        assertEquals(42L, snapshot.trackCount);
        assertFalse(listener.isReceivingEvents());
        listener.eventProcessed();
        assertEquals("New track", ((TrackInfoDto) published.getLast()).currentTrack.title);
        listener.failed(null);
        assertFalse(listener.isReceivingEvents());
    }

    @Test
    void malformedMetadataDoesNotBreakEventDelivery() throws Exception {
        OhInfoServiceEventListener listener = listener();
        listener.metadataChange("invalid XML");
        listener.metatextCountChange(1L);
        listener.metatextChange("Queen - Flash");
        listener.eventProcessed();
        TrackInfoDto dto = (TrackInfoDto) published.getLast();
        assertNotNull(dto.currentTrack);
        assertEquals("Flash", dto.currentTrack.title);
    }
}
