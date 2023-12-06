package nextcp.rest;

import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
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
import nextcp.dto.BrowseRequestDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.SearchRequestDto;
import nextcp.dto.SearchResultDto;
import nextcp.dto.ToastrMessage;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/ContentDirectoryService")
public class RestContentDirectoryService extends BaseRestService {

	private static final Logger log = LoggerFactory.getLogger(RestContentDirectoryService.class.getName());

	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	private ApplicationEventPublisher publisher = null;

	@PostMapping("/rescanContent")
	public void rescanContent(@RequestBody String mediaServerUDN) {
		try {
			if (StringUtils.isBlank(mediaServerUDN)) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
			}
			MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(mediaServerUDN));
			if (device == null) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + mediaServerUDN);
			}
			device.rescan();
		} catch (Exception e) {
			log.error("rescan content :", e);
			publisher.publishEvent(new ToastrMessage(null, "error", "rescan content", e.getMessage()));
		}
	}

	@PostMapping("/browseChildren")
	public ContainerItemDto browse(@RequestBody BrowseRequestDto browseRequest) {
		try {
			checkUdn(browseRequest);
			UDN udn = new UDN(browseRequest.mediaServerUDN);
			MediaServerDevice device = deviceRegistry.getMediaServerByUDN(udn);
			if (device == null) {
				device = deviceRegistry.getInactiveMediaServerList().get(udn);
				if (device != null) {
					publisher.publishEvent(
						new ToastrMessage(null, "error", "server", "media server '" + device.getFriendlyName() + "' is unavailable "));
				} else {
					publisher.publishEvent(new ToastrMessage(null, "error", "server", "media server is unavailable "));
				}
				return new ContainerItemDto();
			}
			checkDeviceAvailability(browseRequest, device);
			BrowseInput inp = new BrowseInput();
			inp.ObjectID = browseRequest.objectID;
			inp.SortCriteria = browseRequest.sortCriteria;
			inp.StartingIndex = browseRequest.start;
			inp.RequestedCount = browseRequest.count;
			inp.Filter = browseRequest.filter;
			return device.browseChildren(inp);
		} catch (Exception e) {
			log.error("cannot browse children : " + browseRequest.toString(), e);
			publisher.publishEvent(new ToastrMessage(null, "error", "browse server", e.getMessage()));
		}
		return new ContainerItemDto();
	}

	@PostMapping("/quickSearch")
	public SearchResultDto quickSearch(@RequestBody SearchRequestDto searchRequest) {
		try {
			if (StringUtils.isBlank(searchRequest.mediaServerUDN)) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
			}

			MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
			if (device == null) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
			}
			return device.quickSearch(searchRequest);
		} catch (Exception e) {
			log.error("quick search : " + searchRequest.toString(), e);
			publisher.publishEvent(new ToastrMessage(null, "error", "quick search server", e.getMessage()));
		}
		return new SearchResultDto();
	}

	@PostMapping("/searchAllItems")
	public SearchResultDto searchAllItems(@RequestBody SearchRequestDto searchRequest) {
		try {
			if (StringUtils.isBlank(searchRequest.mediaServerUDN)) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
			}
			MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
			if (device == null) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
			}
			return device.searchAllItems(searchRequest);
		} catch (Exception e) {
			log.error("search all items : " + searchRequest.toString(), e);
			publisher.publishEvent(new ToastrMessage(null, "error", "search all  items", e.getMessage()));
		}
		return new SearchResultDto();
	}

	@PostMapping("/searchAllPlaylist")
	public SearchResultDto searchAllPlaylist(@RequestBody SearchRequestDto searchRequest) {
		try {
			if (StringUtils.isBlank(searchRequest.mediaServerUDN)) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
			}
			MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
			if (device == null) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
			}
			return device.searchAllPlaylist(searchRequest);
		} catch (Exception e) {
			log.error("search all playlists : " + searchRequest.toString(), e);
			publisher.publishEvent(new ToastrMessage(null, "error", "search all playlists", e.getMessage()));
		}
		return new SearchResultDto();
	}

	@PostMapping("/searchAllAlbum")
	public SearchResultDto searchAllAlbum(@RequestBody SearchRequestDto searchRequest) {
		try {
			if (StringUtils.isBlank(searchRequest.mediaServerUDN)) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
			}
			MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
			if (device == null) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
			}
			return device.searchAllAlbum(searchRequest);
		} catch (Exception e) {
			log.error("search all album : " + searchRequest.toString(), e);
			publisher.publishEvent(new ToastrMessage(null, "error", "search all  album", e.getMessage()));
		}
		return new SearchResultDto();
	}

	@PostMapping("/searchAllArtists")
	public SearchResultDto searchAllArtists(@RequestBody SearchRequestDto searchRequest) {
		try {
			if (StringUtils.isBlank(searchRequest.mediaServerUDN)) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
			}
			MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
			if (device == null) {
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
			}
			return device.searchAllArtists(searchRequest);
		} catch (Exception e) {
			log.error("search all artists : " + searchRequest.toString(), e);
			publisher.publishEvent(new ToastrMessage(null, "error", "search all artists", e.getMessage()));
		}
		return new SearchResultDto();
	}
}
