package nextcp.rest;

import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
import nextcp.dto.UpdateAlbumArtUriRequest;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
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
			throw failed("Cannot start the rescan", e);
		}
	}

	/**
	 * A failed browse answers with an error instead of an empty container : the two are
	 * indistinguishable to the caller, and a view that renders "empty" for a server that is merely
	 * absent is the confusion this used to produce.
	 */
	@PostMapping("/browseChildren")
	public ContainerItemDto browse(@RequestBody BrowseRequestDto browseRequest) {
		checkUdn(browseRequest);
		UDN udn = new UDN(browseRequest.mediaServerUDN);
		MediaServerDevice device = deviceRegistry.getMediaServerByUDN(udn);
		if (device == null) {
			// Resolved before the try so this diagnosis reaches the client as it is, instead of being
			// wrapped in the generic browse message below.
			MediaServerDevice inactive = deviceRegistry.getInactiveMediaServerList().get(udn);
			if (inactive != null) {
				throw failed("Media server '" + inactive.getFriendlyName() + "' is inactive. Please select one from the active list.", null);
			}
			throw failed("Unknown media server : " + browseRequest.mediaServerUDN, null);
		}
		checkDeviceAvailability(browseRequest, device);
		try {
			BrowseInput inp = new BrowseInput();
			inp.ObjectID = browseRequest.objectID != null ? browseRequest.objectID : "0";
			inp.SortCriteria = browseRequest.sortCriteria;
			inp.StartingIndex = browseRequest.start;
			inp.RequestedCount = browseRequest.count;
			inp.Filter = browseRequest.filter;
			return device.browseChildren(inp);
		} catch (Exception e) {
			log.error("cannot browse children : " + browseRequest.toString(), e);
			throw failed("Cannot browse the media server", e);
		}
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
			throw failed("The search failed", e);
		}
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
			throw failed("The item search failed", e);
		}
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
			throw failed("The playlist search failed", e);
		}
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
			throw failed("The album search failed", e);
		}
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
			throw failed("The artist search failed", e);
		}
	}

	@PostMapping("/updateAlbumArtUri")
	public void updateAlbumArtUri(@RequestBody UpdateAlbumArtUriRequest updateRequest) {
		try {
			ExtendedApiMediaDevice device = getExtendedMediaServerByUdn(updateRequest.mediaServerDevice);
			device.updateAlbumArtURI(updateRequest);
			publisher.publishEvent(new ToastrMessage(null, "success", "album cover", "The album cover was updated."));
		} catch (Exception e) {
			log.error("updateAlbumArtUri failed : {}", updateRequest, e);
			throw failed("Cannot update the album cover", e);
		}
	}
	
	@PostMapping("/updateUmsAlbumArtistDirectory/{udn}")
	public void updateUmsAlbumArtistDirectory(@PathVariable("udn") String udn, @RequestBody() String objectcId) {
		try {
			ExtendedApiMediaDevice device = getExtendedMediaServerByUdn(udn);
			device.setUmsAlbumDirectory(objectcId);
			publisher.publishEvent(new ToastrMessage(null, "info", "Artist Directory", "Update of album artist directory was successful."));
		} catch (Exception e) {
			log.error("updateUmsAlbumArtistDirectory failed : {}", objectcId, e);
			throw failed("Cannot set the album artist directory", e);
		}
	}
}
