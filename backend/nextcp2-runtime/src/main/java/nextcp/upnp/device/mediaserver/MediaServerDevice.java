package nextcp.upnp.device.mediaserver;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.support.model.DIDLContent;
import org.jupnp.support.model.DIDLObject.Property.UPNP;
import org.jupnp.support.model.container.Container;
import org.jupnp.support.model.container.MusicAlbum;
import org.jupnp.support.model.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.annotation.PostConstruct;
import nextcp.domainmodel.device.mediaserver.search.SearchSupport;
import nextcp.dto.Config;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.SearchRequestDto;
import nextcp.dto.SearchResultDto;
import nextcp.dto.ServerPlaylistDto;
import nextcp.dto.ServerPlaylists;
import nextcp.upnp.GenActionException;
import nextcp.upnp.device.BaseDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.ContentDirectoryService;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseOutput;
import nextcp.util.BackendException;

/**
 * This class controls an av media server device
 */
public class MediaServerDevice extends BaseDevice {

	private static final Logger log = LoggerFactory.getLogger(MediaServerDevice.class.getName());

	private ContentDirectoryService contentDirectoryService;

	private SearchSupport searchSupportDelegate = null;

	public MediaServerDevice(RemoteDevice device) {
		super(device);
	}

	@Autowired
	private Config config = null;

	@PostConstruct
	private void init() {
		this.contentDirectoryService = new ContentDirectoryService(getUpnpService(), getDevice());
		try {
			searchSupportDelegate = new SearchSupport(contentDirectoryService, this);
		} catch (Exception e) {
			log.info("search support ...", e);
		}
	}

	public ContentDirectoryService getContentDirectoryService() {
		return contentDirectoryService;
	}

	public SearchResultDto quickSearch(SearchRequestDto searchRequest) {
		return searchSupportDelegate.quickSearch(searchRequest);
	}

	public SearchResultDto searchAllItems(SearchRequestDto searchRequest) {
		return searchSupportDelegate.searchAllItems(searchRequest);
	}

	public SearchResultDto searchAllArtists(SearchRequestDto searchRequest) {
		return searchSupportDelegate.searchAllArtists(searchRequest);
	}

	public SearchResultDto searchAllAlbum(SearchRequestDto searchRequest) {
		return searchSupportDelegate.searchAllAlbum(searchRequest);
	}

	public SearchResultDto searchAllPlaylist(SearchRequestDto searchReques) {
		return searchSupportDelegate.searchAllPlaylist(searchReques);
	}

	/**
	 * Read playlist from given folder
	 * @param folderId
	 * @return
	 */
	public ServerPlaylists searchMyPlaylistsItems(String folderId) {
		ServerPlaylists serverPlaylists = new ServerPlaylists();
		try {
			serverPlaylists.mediaServerUdn = getUdnAsString();
			serverPlaylists.serverPlaylists = new ArrayList<>();
			ContainerItemDto playlistFolder = browseChildren(folderId, 999);
			for (ContainerDto pl : playlistFolder.containerDto) {
				if ("object.container.playlistContainer".equalsIgnoreCase(pl.objectClass)) {
					// strip extension if delivered
					ContainerItemDto playlistChilds = browseChildren(pl.id, 0);
					String title = pl.title.lastIndexOf(".") > -1 ? pl.title.substring(0, pl.title.lastIndexOf(".")) : pl.title;
					ServerPlaylistDto dto = new ServerPlaylistDto(pl.albumartUri, title, pl.id, playlistChilds.totalMatches, null);
					serverPlaylists.serverPlaylists.add(dto);
					log.info("Found server based playlist name : {}", dto);
				}
			}
		} catch (Exception e) {
			log.error("search exception", e);
		}
		return serverPlaylists;
	}

	/**
	 * Browse children with given BrowseInput
	 * 
	 * @param inp
	 * @return
	 */
	public ContainerItemDto browseChildren(BrowseInput inp) {
		BrowseOutput out = requestContent(inp);
		DIDLContent didl = generateDidlContent(out);
		ContainerItemDto result = initEmptyContainerItemDto();
		result.totalMatches = out.TotalMatches;

		return fillResultStructureExtracted(inp, didl, result);
	}

	/**
	 * Browse children with objectId & count
	 * 
	 * @param objectId
	 * @param count
	 * @return
	 */
	public ContainerItemDto browseChildren(String objectId, long count) {
		BrowseInput inp = new BrowseInput();
		inp.ObjectID = objectId;
		inp.SortCriteria = "";
		inp.StartingIndex = 0L;
		inp.RequestedCount = count;
		inp.Filter = "*";
		ContainerItemDto resultContainer = browseChildren(inp);
		return resultContainer;
	}

	public ServerPlaylists getServerPlaylists() throws JsonMappingException, JsonProcessingException {
		ServerPlaylists spl = searchMyPlaylistsItems(config.applicationConfig.myPlaylistFolderName);
		return spl;
	}

	private ContainerItemDto fillResultStructureExtracted(BrowseInput inp, DIDLContent didl, ContainerItemDto result) {
		ContainerDto curContainer = browseMetadataMeta(inp.ObjectID);
		if (!"-1".equals(curContainer.parentID)) {
			ContainerDto parentContainer = browseMetadataMeta(curContainer.parentID);
			result.parentFolderTitle = parentContainer.title;
		} else {
			result.parentFolderTitle = "";
		}
		result.currentContainer = curContainer;
		if (didl != null) {
			addContainerObjects(result, didl);
			addItemObjects(result.musicItemDto, didl);
		} else {
			log.warn("DIDL is null");
		}

		return result;
	}

	private DIDLContent generateDidlContent(BrowseOutput out) {
		try {
			log.debug("generateDidlContent : {}", out.Result);
			DIDLContent didl = generateDidlContent(out.Result);
			return didl;
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	private BrowseOutput requestContent(BrowseInput inp) {
		try {
			inp.BrowseFlag = "BrowseDirectChildren";
			checkInp(inp);
			BrowseOutput out = contentDirectoryService.browse(inp);
			if (out != null && out.NumberReturned != null) {
				log.info("Response Objects: " + out.NumberReturned);
				if (log.isDebugEnabled()) {
					log.debug("DIDL Object : " + out.Result);
				}
			}
			return out;
		} catch (Exception e) {
			log.error("cannot browse to {}", inp.ObjectID, e);
			return new BrowseOutput();
		}
	}

	public ContainerDto browseMetadataMeta(String objectId) {
		BrowseInput metaInp = new BrowseInput();
		metaInp.ObjectID = objectId;
		metaInp.StartingIndex = 0L;
		metaInp.RequestedCount = 0L;
		metaInp.SortCriteria = "";
		metaInp.BrowseFlag = "BrowseMetadata";
		metaInp.Filter = "*";
		ContainerDto result = new ContainerDto();
		try {
			BrowseOutput out = contentDirectoryService.browse(metaInp);
			log.info("Response Objects: " + out.NumberReturned);
			if (out.NumberReturned == 1) {
				DIDLContent didl = generateDidlContent(out.Result);
				if (didl != null) {
					result = getDtoBuilder().buildContainerDto(didl.getFirstContainer());
				}
			}
			result.mediaServerUDN = getUDN().getIdentifierString();
			return result;
		} catch (GenActionException e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.description);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
		}
	}

	public URI[] getAlbumArtURIs(DIDLContent didl) {
		List<URI> list = didl.getFirstContainer().getPropertyValues(UPNP.ALBUM_ART_URI.class);
		return list.toArray(new URI[list.size()]);
	}

	private ContainerItemDto initEmptyContainerItemDto() {
		ContainerItemDto result = new ContainerItemDto();
		result.containerDto = new ArrayList<>();
		result.musicItemDto = new ArrayList<>();
		result.albumDto = new ArrayList<>();
		result.parentFolderTitle = "";
		result.minimServerSupportTags = new ArrayList<ContainerDto>();
		return result;
	}

	private void addContainerObjects(ContainerItemDto result, DIDLContent didl) {
		if (didl == null) {
			return;
		}
		for (Container didlObject : didl.getContainers()) {
			ContainerDto containerDto = getDtoBuilder().buildContainerDto(didlObject);
			containerDto.mediaServerUDN = getUDN().getIdentifierString();
			if (didlObject.getTitle().startsWith(">>")) {
				// minim server support for tags ...
				if (!didlObject.getId().endsWith("$hchide")) {
					containerDto.title = containerDto.title.substring(3);
					result.minimServerSupportTags.add(containerDto);
				}
			} else {
				if (didlObject instanceof MusicAlbum) {
					result.albumDto.add(containerDto);
				} else {
					result.containerDto.add(containerDto);
				}
			}
		}
	}

	private void checkInp(BrowseInput inp) {
		if ("-1".equals(inp.ObjectID)) {
			log.warn("browsing to objectID -1 is not permited ... ");
		}
		inp.Filter = getDefault(inp.Filter, "*");
		inp.ObjectID = getDefault(inp.ObjectID, "0");
		inp.StartingIndex = getDefault(inp.StartingIndex, 0L);
		inp.RequestedCount = getDefault(inp.RequestedCount, 999L);
		inp.SortCriteria = getDefault(inp.SortCriteria, "");
	}

	public MediaServerDto getAsDto() {
		return new MediaServerDto(getUDN().getIdentifierString(), getFriendlyName(), false);
	}

	private void addItemObjects(List<MusicItemDto> result, DIDLContent didl) {
		if (didl == null) {
			return;
		}
		for (Item item : didl.getItems()) {
			MusicItemDto itemDto = getDtoBuilder().buildItemDto(item, getUDN().getIdentifierString());
			result.add(itemDto);
		}
	}

	public void rescan() {
		log.warn("rescan not implemented for this device : " + getFriendlyName());
	}

	public void rescanFile(String objectId) {
		log.warn("scan file not implemented for this device : " + getFriendlyName());
	}
}
