package nextcp.rest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import nextcp.dto.ContainerDto;
import nextcp.dto.ServerDeleteObjectRequest;
import nextcp.service.ToastEventPublisher;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.UmsServerDevice;
import nextcp.util.BackendException;

class TestPlaylistLinkRemoval {
	private static final String PLAYLIST_CLASS = "object.container.playlistContainer";
	private static final String FOLDER_CLASS = "object.container.storageFolder";

	private final UmsServerDevice server = mock(UmsServerDevice.class);
	private final RestMediaServerPlaylistService service = new RestMediaServerPlaylistService() {
		@Override
		protected ExtendedApiMediaDevice findExtendedMediaServerByUdn(String udn) {
			return server;
		}
	};

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(service, "toast", mock(ToastEventPublisher.class));
	}

	@Test
	void removesOnlyTheFolderOrPlaylistEntry() {
		when(server.browseMetadataMeta("parent")).thenReturn(container("parent", "0", PLAYLIST_CLASS));
		when(server.browseMetadataMeta("folder-link")).thenReturn(container("folder-link", "parent", FOLDER_CLASS));
		when(server.browseMetadataMeta("playlist-link")).thenReturn(container("playlist-link", "parent", PLAYLIST_CLASS));

		for (String id : new String[] {"folder-link", "playlist-link"}) {
			ServerDeleteObjectRequest request = request(id);
			service.removePlaylistLink(request);
			verify(server).deleteObject(id);
		}
		verify(server, never()).deleteObject("parent");
	}

	@Test
	void rejectsAnOriginalPlaylistOrFolder() {
		when(server.browseMetadataMeta("parent")).thenReturn(container("parent", "0", FOLDER_CLASS));
		when(server.browseMetadataMeta("folder")).thenReturn(container("folder", "parent", FOLDER_CLASS));
		when(server.browseMetadataMeta("playlist")).thenReturn(container("playlist", "parent", PLAYLIST_CLASS));

		for (String id : new String[] {"folder", "playlist"}) {
			ServerDeleteObjectRequest request = request(id);
			assertThrows(BackendException.class, () -> service.removePlaylistLink(request));
			verify(server, never()).deleteObject(id);
		}
	}

	private static ContainerDto container(String id, String parentId, String objectClass) {
		ContainerDto dto = new ContainerDto();
		dto.id = id;
		dto.parentID = parentId;
		dto.objectClass = objectClass;
		return dto;
	}

	private static ServerDeleteObjectRequest request(String id) {
		ServerDeleteObjectRequest request = new ServerDeleteObjectRequest();
		request.serverUdn = "ums";
		request.objectId = id;
		return request;
	}
}
