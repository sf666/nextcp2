package nextcp.upnp.device.mediaserver.ums;

import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jupnp.model.meta.DeviceDetails;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteDeviceIdentity;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.UDADeviceType;
import org.jupnp.model.types.UDN;
import org.junit.jupiter.api.Test;
import nextcp.dto.WebStreamNowPlayingDto;
import nextcp.service.WebRadioNowPlayingService;
import nextcp.upnp.device.mediaserver.UmsServerDevice;
import static org.junit.jupiter.api.Assertions.*;

class TestUmsMetadataEvents {
    @Test
    void extendedEventsPublishArtistTitleAndCoverAndReportADeadSubscription() throws Exception {
        RemoteDevice remote = new RemoteDevice(
            new RemoteDeviceIdentity(new UDN("ums-test"), 600, URI.create("http://127.0.0.1/device").toURL(), null, InetAddress.getLoopbackAddress()),
            new UDADeviceType("MediaServer"), new DeviceDetails("Test UMS"), new RemoteService[0]);
        List<Object> published = new ArrayList<>();
        WebRadioNowPlayingService service = new WebRadioNowPlayingService(published::add);
        UmsServerDevice server = new UmsServerDevice(remote) {
            @Override public void newUmsConfig() { }
            @Override public void webStreamNowPlaying(String value) { service.onWebStreamNowPlaying(value); }
        };
        UmsExtendedServicesServiceEventListener listener = new UmsExtendedServicesServiceEventListener(remote, server);
        assertFalse(listener.isReceivingEvents());
        listener.webStreamNowPlayingChange("""
            {"objectID":"2044","streamTitle":"Oli Silk - Sanctuary St.",
             "artist":"Oli Silk","title":"Sanctuary St.","artUrl":"//cdn-images.audioaddict.com/cover.jpg"}
            """);
        listener.eventProcessed();
        WebStreamNowPlayingDto dto = (WebStreamNowPlayingDto) published.getFirst();
        assertEquals("2044", dto.objectID);
        assertEquals("Oli Silk", dto.artist);
        assertEquals("Sanctuary St.", dto.title);
        assertEquals("//cdn-images.audioaddict.com/cover.jpg", dto.artUrl);
        assertTrue(listener.isReceivingEvents());
        listener.failed(null);
        assertFalse(listener.isReceivingEvents());
        listener.eventProcessed();
        assertTrue(listener.isReceivingEvents());
    }
}
