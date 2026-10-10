package nextcp.upnp.device.mediarenderer.ohtime;

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
import nextcp.dto.TrackTimeDto;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.avopenhomeorg.time1.actions.TimeOutput;
import static org.junit.jupiter.api.Assertions.*;

class TestOhTimeServiceEventListener {
    @Test
    void snapshotsSeedPartialEventsWithoutPretendingThatGenaIsHealthy() throws Exception {
        RemoteDevice remote = new RemoteDevice(
            new RemoteDeviceIdentity(new UDN("time-test"), 600, URI.create("http://127.0.0.1/device").toURL(), null, InetAddress.getLoopbackAddress()),
            new UDADeviceType("MediaRenderer"), new DeviceDetails("Test renderer"), new RemoteService[0]);
        MediaRendererDevice device = new MediaRendererDevice(remote, true);
        List<Object> published = new ArrayList<>();
        OhTimeServiceEventListener listener = new OhTimeServiceEventListener(published::add, device);
        TimeOutput snapshot = new TimeOutput();
        snapshot.Seconds = 308L;
        snapshot.Duration = 0L;
        snapshot.TrackCount = 126L;
        TrackTimeDto dto = listener.fromSnapshot(snapshot);
        assertEquals("05:08", dto.secondsDisp);
        assertEquals("streaming", dto.durationDisp);
        assertTrue(dto.streaming);
        assertFalse(listener.hasRecentEvents(5000));
        listener.secondsChange(309L);
        listener.eventProcessed();
        assertTrue(listener.hasRecentEvents(5000));
        TrackTimeDto event = (TrackTimeDto) published.getFirst();
        assertEquals(309L, event.seconds);
        assertEquals(126L, event.trackCount);
        assertEquals(0L, event.duration);
        listener.failed(null);
        assertFalse(listener.hasRecentEvents(5000));
    }

    @Test
    void durationsUseElapsedHoursInsteadOfTheLocalTimeZone() {
        TrackTimeDto dto = OhTimeServiceEventListener.trackTime("renderer", 3661L, 7200L, 1L);
        assertEquals("01:01:01", dto.secondsDisp);
        assertEquals("02:00:00", dto.durationDisp);
        assertFalse(dto.streaming);
        TrackTimeDto unknown = OhTimeServiceEventListener.trackTime("renderer", null, null, null);
        assertEquals(0L, unknown.seconds);
        assertEquals("00:00", unknown.secondsDisp);
    }
}
