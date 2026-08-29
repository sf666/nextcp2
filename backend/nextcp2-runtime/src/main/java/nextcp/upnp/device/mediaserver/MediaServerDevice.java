package nextcp.upnp.device.mediaserver;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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
import nextcp.config.ServerConfig;
import nextcp.domainmodel.device.mediaserver.search.SearchSupport;
import nextcp.dto.ToastrMessage;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerUpdateIdsDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicAlbumIds;
import nextcp.dto.MusicItemDto;
import nextcp.dto.SearchRequestDto;
import nextcp.dto.SearchResultDto;
import nextcp.dto.ServerDeviceConfiguration;
import nextcp.dto.ServerPlaylistDto;
import nextcp.dto.ServerPlaylists;
import nextcp.upnp.GenActionException;
import nextcp.upnp.device.BaseDevice;
import nextcp.upnp.device.mediaserver.cds.ContentDirectoryEventListener;
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

	private ContentDirectoryEventListener contentDirectoryEventListener;

	private SearchSupport searchSupportDelegate = null;

    @Autowired
    private ServerConfig serverConfigService = null;
	
	@Autowired
	private ServerConfig serverConfig = null;

	public MediaServerDevice(RemoteDevice device) {
		super(device);
	}

	@PostConstruct
	private void init() {
		createContentDirectoryService();
	}

	private void createContentDirectoryService() {
		this.contentDirectoryService = new ContentDirectoryService(getUpnpService(), getDevice());
		// Tells us when a container changed after we already browsed it.
		this.contentDirectoryEventListener = new ContentDirectoryEventListener(getDevice(), this);
		this.contentDirectoryService.addSubscriptionEventListener(contentDirectoryEventListener);
		try {
			searchSupportDelegate = new SearchSupport(contentDirectoryService, this);
		} catch (Exception e) {
			log.info("search support ...", e);
		}
	}

	/**
	 * Subscribes to the content directory again when the subscription is not carrying events.
	 *
	 * The subscription is set up once, while the device is being added. If that attempt does not come up -
	 * on startup the control point can reach the server before its own callback endpoint is listening -
	 * nothing used to try again: a media server that was already running when we started never sent us a
	 * single event, until it restarted and was discovered anew. Called periodically by the device registry.
	 */
	public void ensureContentDirectorySubscription() {
		if (contentDirectoryEventListener != null && contentDirectoryEventListener.isSubscribed()) {
			return;
		}
		log.info("content directory of {} is not subscribed, subscribing again", getFriendlyName());
		createContentDirectoryService();
	}

	/**
	 * Passes on the containers the server reported as changed, so the views showing one of them
	 * browse again.
	 */
	public void containerContentChanged(List<String> containerIds) {
		getEventPublisher().publishEvent(new ContainerUpdateIdsDto(getUDN().getIdentifierString(), containerIds));
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

	public SearchResultDto searchAllPlaylist(SearchRequestDto searchRequest) {
		return searchSupportDelegate.searchAllPlaylist(searchRequest);
	}

	/**
	 * Read playlist from given folder
	 * 
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
					// No browse per playlist here. Asking a media server for the number of entries makes it
					// read the playlist and resolve every track, so filling the sidebar cost one full
					// playlist scan per entry - for a number the sidebar does not even show. The count is
					// fetched where it is displayed, in the add to playlist dialog.
					// strip extension if delivered
					String title = pl.title.lastIndexOf(".") > -1 ? pl.title.substring(0, pl.title.lastIndexOf(".")) : pl.title;
					ServerPlaylistDto dto = new ServerPlaylistDto(pl.albumartUri, title, pl.id, null, null);
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
		ServerDeviceConfiguration sc = serverConfigService.getMediaServerConfig(getUdnAsString());
		ServerPlaylists spl = searchMyPlaylistsItems(sc.playistObjectId);
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
			result.allTracksSameAlbumIds = allSongsSameAlbum(result.musicItemDto);
		} else {
			log.warn("DIDL is null");
		}

		return result;
	}

	private MusicAlbumIds allSongsSameAlbum(List<MusicItemDto> musicItemDto) {
		MusicAlbumIds result = new MusicAlbumIds();
		if (musicItemDto.size() < 1) {
			log.debug("allSongsSameAlbum: no music items found ... ");
			return result;
		}
		// UMS sends the releaseTrackId as releaseId ... maybe we need to refactor
		String firstMB = null;
		Long firstDiscogs = null;
		
		if (musicItemDto.get(0).musicBrainzId != null) {
			firstMB = musicItemDto.get(0).musicBrainzId.ReleaseTrackId;
		}
		if (musicItemDto.get(0).discogsId != null) {
			firstDiscogs = musicItemDto.get(0).discogsId.ReleaseId;
		}

		boolean allSameMB = !StringUtils.isAllBlank(firstMB);
		boolean allSameDiscogs = (firstDiscogs != null);

		for (MusicItemDto item : musicItemDto) {
			if (allSameMB && item.musicBrainzId != null && item.musicBrainzId.ReleaseTrackId != null) {
				if (!firstMB.equals(item.musicBrainzId.ReleaseTrackId)) {
					allSameMB = false;
				}
			} else {
				allSameMB = false;
			}
			if (allSameDiscogs && item.discogsId != null && item.discogsId.ReleaseId != null) {
				if (!firstDiscogs.equals(item.discogsId.ReleaseId)) {
					allSameDiscogs = false;
				}
			} else {
				allSameDiscogs = false;
			}
		}
		
		if (allSameMB) {
			log.debug("same musicbrainz release id : {}", firstMB);
			result.musicBrainzAlbumId = firstMB;
		}
		if (allSameDiscogs) {
			log.debug("same discogs release id : {}", firstDiscogs);
			result.discogsReleaseId = firstDiscogs;
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
				log.info("[requestContent] Response Objects : {}", out.NumberReturned);
				if (log.isDebugEnabled()) {
					log.debug("DIDL Object : " + out.Result);
				}
			}
			return out;
		} catch (Exception e) {
			// No stack trace : ActionCallback already logged the device and the SOAP fault, and the trace
			// is always the same fixed chain (ActionCallback -> Browse -> requestContent). It cost 66 log
			// lines per refused browse for nothing.
			log.warn("cannot browse to {} ({})", inp.ObjectID, e.getClass().getSimpleName());
			log.debug("browse failure detail", e);
			// A refused browse used to be indistinguishable from an empty folder: the UI rendered nothing
			// and said nothing, which looks like nextCP lost the content. It regularly is the media server
			// failing on its own (UMS has been seen answering a Browse with UPnP error 501 out of a
			// NullPointerException in its own sort comparator), so say who refused what.
			publishBrowseFailure(inp, e);
			// An empty result with the counters left at null makes every caller do arithmetic on null.
			BrowseOutput failed = new BrowseOutput();
			failed.NumberReturned = 0L;
			failed.TotalMatches = 0L;
			failed.Result = "";
			return failed;
		}
	}

	/** Object id of the last refused browse, to keep one failing click from raising several toasts. */
	private volatile String lastBrowseFailureId = null;
	private volatile long lastBrowseFailureAtMs = 0;

	private static final long BROWSE_FAILURE_QUIET_MS = 5000;

	private void publishBrowseFailure(BrowseInput inp, Exception e) {
		if (getEventPublisher() == null) {
			return;
		}
		// One click can trigger several browse requests for the same folder (a metadata probe, the
		// listing itself, further pages); the user needs to hear about it once.
		long now = System.currentTimeMillis();
		if (StringUtils.equals(lastBrowseFailureId, inp.ObjectID)
				&& now - lastBrowseFailureAtMs < BROWSE_FAILURE_QUIET_MS) {
			return;
		}
		lastBrowseFailureId = inp.ObjectID;
		lastBrowseFailureAtMs = now;

		String reason = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.getClass().getSimpleName();
		String body = String.format("'%s' refused to list this folder (object %s): %s", getFriendlyName(),
				inp.ObjectID, StringUtils.abbreviate(reason, 200));
		getEventPublisher().publishEvent(new ToastrMessage(null, "error", "media server", body));
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
			log.info("[browseMetadataMeta] Response Objects : {}", out.NumberReturned);
			if (out.NumberReturned == 1) {
				DIDLContent didl = generateDidlContent(out.Result);
				if (didl != null && didl.getContainers().size() > 0) {
					result = getDtoBuilder().buildContainerDto(didl.getFirstContainer());
				} else {
					log.warn("DIDL containers are null or empty for objectId : {}", objectId);
				}
			}
			result.mediaServerUDN = getUDN().getIdentifierString();
			return result;
		} catch (GenActionException e) {
			// Metadata only names the container in the breadcrumb. Failing the whole browse over it
			// would throw away children that were fetched just fine, and leaves the client with an
			// error instead of a usable folder.
			log.warn("cannot read metadata of {} : {}", objectId, e.description);
			log.debug("", e);
			return result;
		} catch (Exception e) {
			log.warn("cannot read metadata of {} : {}", objectId, e.getMessage());
			log.debug("", e);
			return result;
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
		return new MediaServerDto(getBiggestIconUrl(), getUDN().getIdentifierString(), getFriendlyName(), false);
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
	
	public ServerDeviceConfiguration getNewServerConfig() {
        ServerDeviceConfiguration c = new ServerDeviceConfiguration();
        c.enabled = true;
        c.ip = getDevice().getIdentity().getDescriptorURL().getHost();
        c.displayString = getDevice().getDisplayString();
        c.mediaServer = getAsDto();
		return c;
	}

	public void updateCurrentConfigState(ServerDeviceConfiguration c) {
        c.ip = getDevice().getIdentity().getDescriptorURL().getHost();
        c.displayString = getDevice().getDisplayString();
        c.mediaServer = getAsDto();
		serverConfig.updateServerDevice(c);        
	}
}
