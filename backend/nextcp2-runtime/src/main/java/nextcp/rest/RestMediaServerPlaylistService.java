package nextcp.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
import org.jupnp.support.model.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.CreateServerPlaylistVO;
import nextcp.dto.ServerDeleteObjectRequest;
import nextcp.dto.ServerPlaylistDto;
import nextcp.dto.ServerPlaylistEntry;
import nextcp.dto.ServerPlaylists;
import nextcp.service.ToastEventPublisher;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MediaServerPlaylistService")
public class RestMediaServerPlaylistService extends BaseRestService {

	private static final Logger log = LoggerFactory.getLogger(RestMediaServerPlaylistService.class.getName());

	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	private ToastEventPublisher toast = null;

	@Autowired
	nextcp.eventBridge.MediaServerSseEvents mediaServerSseEvents = null;

	private HashMap<String, LinkedList<String>> recentPlaylistsIds = new HashMap<>();

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
			log.info("adding song id {} to playlist with id {}", addRequest.songObjectId, addRequest.playlistObjectId);
			getExtendedMediaServerByUdn(addRequest.serverUdn).addSongToPlaylist(addRequest.songObjectId, addRequest.playlistObjectId);
			toast.publishSuccessMessage(null, "playlist", "sond added to playlist");
			mediaServerSseEvents.mediaServerRecentPlaylistChanged(getRecentServerPlaylists(addRequest.serverUdn));
		} catch (Exception e) {
			String errorText = e.getMessage();
			toast.publishErrorMessage(null, "playlist", errorText);
			log.warn("adding song to server playlist", e);
		} finally {
			addPlaylistToRecent(addRequest.serverUdn, addRequest.playlistObjectId);
		}
	}

	private void addPlaylistToRecent(String udn, String objectId) {
		try {
			if (!getRecentObjectIds(udn).contains(objectId)) {
				getRecentObjectIds(udn).addFirst(objectId);
				log.debug("recent playlists : added objectId {} to server with udn {}", objectId, udn);
				if (getRecentObjectIds(udn).size() > 3) {
					String removed = getRecentObjectIds(udn).removeLast();
					log.debug("removing last object from recent playlists with ID : ", removed);
				}
				mediaServerSseEvents.mediaServerRecentPlaylistChanged(getRecentServerPlaylists(udn));
			} else {
				log.debug("objectId {} already in recent playlists ... ");
			}
		} catch (Exception e) {
			log.error("cannot add playlistid {} to current playlists on server with udn {}", objectId, udn);
		}
	}

	private void removePlaylistFromRecent(String udn, String objectId) {
		getRecentObjectIds(udn).remove(objectId);
		mediaServerSseEvents.mediaServerRecentPlaylistChanged(getRecentServerPlaylists(udn));
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
			ServerPlaylists all = new ServerPlaylists();
			all.mediaServerUdn = serverUdn;
			all.containerId = "-1";
			all.serverPlaylists = new ArrayList<>();
			LinkedList<String> recent = getRecentObjectIds(serverUdn);
			for (String id : recent) {
		        ContainerItemDto playlists = getMediaServerByUdn(serverUdn).browseChildren(id, 0L);
		        ContainerDto pl = playlists.currentContainer;
				if ("object.container.playlistContainer".equalsIgnoreCase(pl.objectClass)) {
					// strip extension if delivered 
					String title = pl.title.lastIndexOf(".") > -1 ? pl.title.substring(0, pl.title.lastIndexOf(".")) : pl.title;
					ServerPlaylistDto dto = new ServerPlaylistDto(pl.albumartUri, title, pl.id, playlists.totalMatches, null);
					all.serverPlaylists.add(dto);
					log.info("Found server based playlist name : {}", dto);
				}
			}

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
			Container pi = getExtendedMediaServerByUdn(createPlaylistVo.mediaServerUdn).createPlaylist(createPlaylistVo.containerId,
				createPlaylistVo.playlistName);
			toast.publishSuccessMessage(null, "playlist", "playlist created : " + createPlaylistVo.playlistName);
			addPlaylistToRecent(createPlaylistVo.mediaServerUdn, pi.getId());
			return pi.getId();
		} catch (Exception e) {
			log.warn("createPlaylist", e);
			toast.publishErrorMessage(null, "playlist", "create playlis failed : " + e.getMessage());
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
			toast.publishSuccessMessage(null, "playlist", "removed");
			removePlaylistFromRecent(deleteRequest.serverUdn, deleteRequest.objectId);
		} catch (Exception e) {
			log.warn("removing song from server playlist", e);
			toast.publishErrorMessage(null, "playlist", "Removing song failed. Message : " + e.getMessage());
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
