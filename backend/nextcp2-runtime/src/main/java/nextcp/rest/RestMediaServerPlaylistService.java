package nextcp.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
import org.jupnp.support.model.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import nextcp.dto.CreateServerPlaylistVO;
import nextcp.dto.ServerDeleteObjectRequest;
import nextcp.dto.ServerPlaylistDto;
import nextcp.dto.ServerPlaylistEntry;
import nextcp.dto.ServerPlaylists;
import nextcp.dto.ToastrMessage;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MediaServerPlaylistService")
public class RestMediaServerPlaylistService {

	private static final Logger log = LoggerFactory.getLogger(RestMediaServerPlaylistService.class.getName());

	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	private ApplicationEventPublisher publisher = null;
	
    @Autowired
    nextcp.eventBridge.MediaServerSseEvents mediaServerSseEvents =  null;
	

	private HashMap<String, LinkedList<String>> recentPlaylistsIds = new HashMap();

	public RestMediaServerPlaylistService() {
	}

	/**
	 * MediaServerAction
	 * 
	 * @param addRequest
	 */
	@PostMapping("/addToServerPlaylist")
	public void addToServerPlaylist(@RequestBody ServerPlaylistEntry addRequest) {
		try {
			getExtendedMediaServerByUdn(addRequest.serverUdn).addSongToPlaylist(addRequest.songObjectId, addRequest.playlistObjectId);
			getRecentObjectIds(addRequest.serverUdn).addFirst(addRequest.playlistObjectId);
			if (getRecentObjectIds(addRequest.serverUdn).size() > 3) {
				getRecentObjectIds(addRequest.serverUdn).removeLast();
			}
			mediaServerSseEvents.mediaServerRecentPlaylistChanged(getRecentServerPlaylists(addRequest.serverUdn));
		} catch (Exception e) {
			String errorText = e.getMessage();
			publisher.publishEvent(new ToastrMessage(null, "error", "add to playlist", errorText));
			log.warn("adding song to server playlist", e);
		}
	}

	private LinkedList<String> getRecentObjectIds(String udn) {
		LinkedList<String> ll = recentPlaylistsIds.get(udn);
		if (ll == null) {
			ll = new LinkedList<String>();
			recentPlaylistsIds.put(udn, ll);
		}
		return ll;
	}
	
	/**
	 * 
	 * @param serverUdn
	 * @return
	 */
	@PostMapping("/getServerPlaylists")
	public ServerPlaylists getServerPlaylists(@RequestBody String serverUdn) {
		try {
			return getExtendedMediaServerByUdn(serverUdn).getServerPlaylists();
		} catch (Exception e) {
			log.warn("getServerPlaylists", e);
			return new ServerPlaylists();
		}
	}

	/**
	 * 
	 * @param serverUdn
	 * @return
	 */
	@PostMapping("/getRecentServerPlaylists")
	public ServerPlaylists getRecentServerPlaylists(@RequestBody String serverUdn) {
		try {
			ServerPlaylists all =  getExtendedMediaServerByUdn(serverUdn).getServerPlaylists();
			// TODO search fpr objectID to hable playlists outside of managed folder
			LinkedList<String> recent = getRecentObjectIds(serverUdn);
			
			List<ServerPlaylistDto> recentDto = all.serverPlaylists.stream().filter(dto -> recent.contains(dto.playlistId)).toList();
			all.serverPlaylists.clear();
			all.serverPlaylists.addAll(recentDto);
			return all;
			
		} catch (Exception e) {
			log.warn("getServerPlaylists", e);
			ServerPlaylists spl = new ServerPlaylists();
			spl.serverPlaylists = new ArrayList<>();
			return new ServerPlaylists();
		}
	}

	/**
	 * Creates a server based playlist.
	 * 
	 * @param createPlaylistVo
	 */
	@PostMapping("/createPlaylist")
	public String createPlaylist(@RequestBody CreateServerPlaylistVO createPlaylistVo) {
		try {
			Item pi = getExtendedMediaServerByUdn(createPlaylistVo.mediaServerUdn).createPlaylist(createPlaylistVo.containerId,
				createPlaylistVo.playlistName);
			return pi.getId();
		} catch (Exception e) {
			log.warn("createPlaylist", e);
			return "";
		}
	}

	/**
	 * media server action
	 * 
	 * @param deleteRequest
	 */
	@PostMapping("/deleteObject")
	public void removeFromServerPlaylist(@RequestBody ServerDeleteObjectRequest deleteRequest) {
		try {
			getExtendedMediaServerByUdn(deleteRequest.serverUdn).deleteObject(deleteRequest.objectId);
		} catch (Exception e) {
			log.warn("removing song from server playlist", e);
			publisher.publishEvent(new ToastrMessage(null, "error", "edit playlist", "Removing song failed. Message : " + e.getMessage()));
		}
	}

	protected ExtendedApiMediaDevice getExtendedMediaServerByUdn(String udn) {
		if (udn == null || StringUtils.isBlank(udn)) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "please provide output device (media-renderer).");
		}

		MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(udn));
		if (device == null) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Media-Server not found : " + udn);
		}

		if (device instanceof ExtendedApiMediaDevice) {
			return ((ExtendedApiMediaDevice) device);
		}
		throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "extended features not availbale : " + udn);
	}

}
