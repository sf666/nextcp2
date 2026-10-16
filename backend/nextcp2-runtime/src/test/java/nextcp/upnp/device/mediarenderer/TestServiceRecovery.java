package nextcp.upnp.device.mediarenderer;

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
import nextcp.dto.TrackInfoDto;
import nextcp.dto.ItemDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.ohinfo.OhInfoServiceEventListener;
import nextcp.upnp.modelGen.avopenhomeorg.info1.InfoService;
import org.springframework.context.ApplicationEventPublisher;
import static org.junit.jupiter.api.Assertions.*;

class TestServiceRecovery {
    @Test
    void missingInfoEventsPublishSnapshotsWhileHealthyQuietSubscriptionsDoNotPoll() throws Exception {
        RemoteDevice remote = new RemoteDevice(
            new RemoteDeviceIdentity(new UDN("info-recovery-test"), 600, URI.create("http://127.0.0.1/device").toURL(), null, InetAddress.getLoopbackAddress()),
            new UDADeviceType("MediaRenderer"), new DeviceDetails("Test renderer"), new RemoteService[0]);
        List<Object> published = new ArrayList<>();
        class Renderer extends MediaRendererDevice {
            int snapshots;
            final DtoBuilder builder = new DtoBuilder();

            Renderer() {
                super(remote, true);
                oh_infoService = new InfoService(null, remote);
                ohInfoServiceEventListener = new OhInfoServiceEventListener(this);
                infoService = () -> {
                    snapshots++;
                    TrackInfoDto dto = new TrackInfoDto();
                    dto.currentTrack = new ItemDto();
                    return dto;
                };
            }

            @Override public ApplicationEventPublisher getEventPublisher() { return published::add; }
            @Override public DtoBuilder getDtoBuilder() { return builder; }
        }
        Renderer renderer = new Renderer();
        var publisher = MediaRendererDevice.class.getDeclaredField("eventPublisher");
        publisher.setAccessible(true);
        publisher.set(renderer, (ApplicationEventPublisher) published::add);
        renderer.tick(5);
        renderer.tick(10);
        assertEquals(2, renderer.snapshots, "Snapshots must not hide the missing subscription");
        assertEquals(2, published.size());
        assertInstanceOf(TrackInfoDto.class, published.getLast());
        renderer.ohInfoServiceEventListener.eventProcessed();
        renderer.tick(15);
        assertEquals(2, renderer.snapshots, "Info events are naturally quiet between metadata changes");
        renderer.ohInfoServiceEventListener.failed(null);
        renderer.tick(20);
        assertEquals(3, renderer.snapshots, "A failed subscription must resume publishing fallback metadata");
    }

    @Test
    void recoveryRebuildsTheBridgesAndListenersAndRetriesAfterFailure() throws Exception {
        RemoteDevice remote = new RemoteDevice(
            new RemoteDeviceIdentity(new UDN("recovery-test"), 600, URI.create("http://127.0.0.1/device").toURL(), null, InetAddress.getLoopbackAddress()),
            new UDADeviceType("MediaRenderer"), new DeviceDetails("Test renderer"), new RemoteService[0]);
        class Renderer extends MediaRendererDevice {
            int initializations;
            boolean fail = true;

            Renderer() { super(remote, true); }

            @Override public void initServices() {
                initializations++;
                setServicesOffline(false);
                if (fail) throw new IllegalStateException("Device temporarily unavailable");
            }
        }
        Renderer renderer = new Renderer();
        renderer.setServicesOffline(true);
        renderer.checkServicesOnline();
        assertEquals(1, renderer.initializations, "Recovery must rebuild listeners and bridges, not only service wrappers");
        renderer.fail = false;
        renderer.checkServicesOnline();
        assertEquals(2, renderer.initializations, "A failed initialization must remain eligible for recovery");
        renderer.checkServicesOnline();
        assertEquals(2, renderer.initializations, "An online renderer must not be initialized again");
    }
}
