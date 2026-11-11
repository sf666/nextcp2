package nextcp.upnp.device.mediaserver.ums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.InetAddress;
import java.net.URI;
import java.util.List;
import org.jupnp.model.meta.DeviceDetails;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteDeviceIdentity;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.UDADeviceType;
import org.jupnp.model.types.UDN;
import org.jupnp.support.model.container.Container;
import org.junit.jupiter.api.Test;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerItemDto;
import nextcp.upnp.device.mediaserver.UmsServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;

class TestUmsRecordingImport {
    @Test
    void reusesExistingFolderExposedAsMusicAlbum() throws Exception {
        RemoteDevice remote = new RemoteDevice(
            new RemoteDeviceIdentity(new UDN("ums-test"), 600, URI.create("http://127.0.0.1/device").toURL(), null, InetAddress.getLoopbackAddress()),
            new UDADeviceType("MediaServer"), new DeviceDetails("Test UMS"), new RemoteService[0]);
        ContainerDto album = new ContainerDto();
        album.id = "12397";
        album.title = "Good Life";
        ContainerItemDto children = new ContainerItemDto();
        children.containerDto = List.of();
        children.albumDto = List.of(album);
        children.totalMatches = 1L;

        UmsServerDevice server = new UmsServerDevice(remote) {
            @Override public void newUmsConfig() { }

            @Override
            public ContainerItemDto browseChildren(BrowseInput input) {
                return children;
            }

            @Override
            public Container createFolder(String parentContainerId, String folderName) {
                return fail("Import must reuse the existing album folder");
            }
        };

        assertEquals(album.id, server.getOrCreateChildFolderId("1664", album.title));
    }
}
