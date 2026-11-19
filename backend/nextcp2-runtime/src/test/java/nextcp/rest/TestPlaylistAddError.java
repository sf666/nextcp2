package nextcp.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.UmsServerDevice;
import nextcp.util.BackendException;

class TestPlaylistAddError {
	@Test
	void explainsAnUnstorableContainerInTheHttpResponse() throws Exception {
		UmsServerDevice server = mock(UmsServerDevice.class);
		when(server.addSongToPlaylist(anyString(), anyString())).thenThrow(new BackendException(
			BackendException.GENERIC_ERROR, "java.io.IOException: The container has no persistent file path or URL."));
		RestMediaServerPlaylistService service = new RestMediaServerPlaylistService() {
			@Override
			protected ExtendedApiMediaDevice getExtendedMediaServerByUdn(String udn) {
				return server;
			}
		};
		MockMvcBuilders.standaloneSetup(service).setControllerAdvice(new BackendExceptionAdvice()).build()
			.perform(post("/MediaServerPlaylistService/addToServerPlaylist")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"serverUdn\":\"ums\",\"songObjectId\":\"album\",\"playlistObjectId\":\"playlist\"}"))
			.andExpect(status().isExpectationFailed())
			.andExpect(content().string("Cannot add this album or folder to the playlist because the media server cannot store a link to it. Try adding its tracks individually."));
	}
}
