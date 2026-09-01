package nextcp.domainmodel.device.mediaserver.search;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jupnp.support.model.DIDLContent;
import org.jupnp.support.model.container.Container;
import org.jupnp.support.model.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.ContainerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.SearchRequestDto;
import nextcp.dto.SearchResultDto;
import nextcp.upnp.GenActionException;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.ContentDirectoryService;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.SearchInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.SearchOutput;
import nextcp.util.DidlContent;

/**
 * Content Directory Service - Search Support Class
 */
public class SearchSupport
{
    private static final Logger log = LoggerFactory.getLogger(SearchSupport.class.getName());

    /** A like is five stars, a dislike is zero, unrated is a third state. */
    private static final int RATING_LIKED = 5;

    private static final String UPNP_RATING = "upnp:rating";

    private ContentDirectoryService contentDirectoryService = null;
    private DidlContent didlContent = new DidlContent();
    private MediaServerDevice mediaServerDevice = null;
    private String searchCaps = "";

    public SearchSupport(ContentDirectoryService contentDirectoryService, MediaServerDevice mediaServerDevice)
    {
        this.contentDirectoryService = contentDirectoryService;
        this.mediaServerDevice = mediaServerDevice;

        try
        {
            searchCaps = contentDirectoryService.getSearchCapabilities().SearchCaps;
        }
        catch (GenActionException e)
        {
            log.warn(String.format("%s -> %s", "No search capability for device", e.description));
        }
        catch (Exception e)
        {
            log.info("no search capability available ... ", e);
        }
    }

    public String getSearchCaps()
    {
        return searchCaps;
    }

    /** Whether this media server accepts the user rating as a search criterion. */
    public boolean supportsRatingSearch()
    {
        return searchCaps != null && searchCaps.toLowerCase().contains(UPNP_RATING);
    }

    /**
     * All liked playlists, wherever they live on the server. Empty if the server cannot search on the rating.
     */
    public List<ContainerDto> searchLikedPlaylists(long requestCount)
    {
        List<ContainerDto> container = new ArrayList<>();
        if (!supportsRatingSearch())
        {
            log.warn("media server does not offer {} as a search criterion, so it has no liked playlists. SearchCaps : {}", UPNP_RATING, searchCaps);
            return container;
        }
        // No SortCriteria : dc:title resolves to the full file path there. The caller sorts by display name.
        String searchCriteria = String.format("( upnp:class derivedfrom \"object.container.playlistContainer\" and %s = \"%d\" )", UPNP_RATING, RATING_LIKED);
        searchContainer(searchCriteria, "", container, requestCount, null);
        return container;
    }

    /**
     * Quick Search delivers first elements of Songs, Albums, Artists and Playlists. Sorting is optimized to deliver liked or better rated music
     * 
     * @param searchRequest
     * @return
     */
    public SearchResultDto quickSearch(SearchRequestDto searchRequest)
    {
        String quickSearch = searchRequest.searchRequest;
        long requestCount = adjustRequestCount(searchRequest.requestCount);

        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddMusicItems(quickSearch, searchRequest.sortCriteria, container, requestCount, searchRequest.parentObjectID);
        searchAndAddArtistContainer(quickSearch, searchRequest.sortCriteria, container, requestCount, searchRequest.parentObjectID);
        searchAndAddAlbumContainer(quickSearch, searchRequest.sortCriteria, container, requestCount, searchRequest.parentObjectID);
        searchAndAddPlaylistContainer(quickSearch, searchRequest.sortCriteria, container, requestCount, searchRequest.parentObjectID);

        return container;
    }

    private long adjustRequestCount(Long givenRequestCount)
    {
        if (givenRequestCount == null)
        {
            return 3;
        }

        givenRequestCount = Math.min(999, givenRequestCount);
        givenRequestCount = Math.max(2, givenRequestCount);
        return givenRequestCount;

    }

    private void searchAndAddPlaylistContainer(String quickSearch, String sortCriteria, SearchResultDto container, long requestCount, String parentObjectID)
    {
    	container.playlistItemsTotal = searchAndAddContainer(quickSearch, sortCriteria, container.playlistItems, "object.container.playlistContainer", requestCount, parentObjectID);
    }

    private void searchAndAddAlbumContainer(String quickSearch, String sortCriteria, SearchResultDto container, long requestCount, String objectId)
    {
        container.albumItemsTotal = searchAndAddContainer(quickSearch, sortCriteria, container.albumItems, "object.container.album", requestCount, objectId);
    }

    private void searchAndAddArtistContainer(String quickSearch, String sortCriteria, SearchResultDto container, long requestCount, String objectId)
    {
    	container.artistItemsTotal = searchAndAddContainer(quickSearch, sortCriteria, container.artistItems, "object.container.person", requestCount, objectId);
    }

    private int searchAndAddContainer(String quickSearch, String sortCriteria, List<ContainerDto> container, String upnpClass, long requestCount, String parentObjectID)
    {
        String searchCriteria;
        if (!StringUtils.isAllBlank(quickSearch)) {
        	searchCriteria = String.format("( upnp:class derivedfrom \"%s\" and dc:title contains \"%s\")", upnpClass, quickSearch);
        } else {
        	searchCriteria = String.format("( upnp:class derivedfrom \"%s\"  )", upnpClass);
        }
        return searchContainer(searchCriteria, sortCriteria, container, requestCount, parentObjectID);
    }

    /** Container search with a ready made criteria string. Returns the server's total match count. */
    private int searchContainer(String searchCriteria, String sortCriteria, List<ContainerDto> container, long requestCount, String parentObjectID)
    {
        SearchInput searchInput = new SearchInput();
        searchInput.ContainerID = getContainerId(parentObjectID);
        searchInput.SearchCriteria = searchCriteria;
        searchInput.StartingIndex = 0L;
        searchInput.Filter = "*";
        searchInput.RequestedCount = requestCount;
        searchInput.SortCriteria = sortCriteria;

        SearchOutput out = contentDirectoryService.search(searchInput);

        DIDLContent didl;
        try
        {
            didl = didlContent.generateDidlContent(out.Result);
            if (didl != null) 
            {
                addContainerObjects(container, didl);
            }
        }
        catch (Exception e)
        {
            log.warn("search error", e);
        }
        return out.TotalMatches.intValue();
    }

    private void searchAndAddMusicItems(String quickSearch, String sortCriteria, SearchResultDto container, Long requestCount, String parentObjectID)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("( upnp:class = \"object.item.audioItem.musicTrack\""); // upnp:class derivedfrom “object.container.person”
        sb.append(String.format(" and dc:title contains \"%s\")", quickSearch));

        SearchInput searchInput = new SearchInput();
        searchInput.ContainerID = getContainerId(parentObjectID);
        searchInput.SearchCriteria = sb.toString();
        searchInput.StartingIndex = 0L;
        searchInput.Filter = "*";
        searchInput.RequestedCount = requestCount;
        searchInput.SortCriteria = sortCriteria;

        DIDLContent didl;
        try
        {
            SearchOutput out = contentDirectoryService.search(searchInput);
            container.musicItemsTotal = out.TotalMatches.intValue();
            didl = didlContent.generateDidlContent(out.Result);
            if (didl != null) 
            {
                addItemObjects(container.musicItems, didl);
            }
        }
        catch (Exception e)
        {
            log.warn("search error", e);
        }
    }

	private String getContainerId(String parentObjectID) {
		if (StringUtils.isAllBlank(parentObjectID)) {
			return "0";
		}
		return parentObjectID;
	}

    private SearchResultDto initEmptySearchResultContainer()
    {
        SearchResultDto dto = new SearchResultDto();
        dto.albumItems = new ArrayList<ContainerDto>();
        dto.artistItems = new ArrayList<ContainerDto>();
        dto.musicItems = new ArrayList<>();
        dto.playlistItems = new ArrayList<>();
        dto.albumItemsTotal = 0;
        dto.artistItemsTotal = 0;
        dto.musicItemsTotal = 0;
        dto.playlistItemsTotal = 0;
        return dto;
    }

    private void addContainerObjects(List<ContainerDto> container, DIDLContent didl)
    {
        for (Container didlObject : didl.getContainers())
        {
            ContainerDto containerDto = mediaServerDevice.getDtoBuilder().buildContainerDto(didlObject);
            containerDto.mediaServerUDN = mediaServerDevice.getUDN().getIdentifierString();
            container.add(containerDto);
        }
    }

    private void addItemObjects(List<MusicItemDto> result, DIDLContent didl)
    {
        for (Item item : didl.getItems())
        {
            MusicItemDto itemDto = mediaServerDevice.getDtoBuilder().buildItemDto(item, mediaServerDevice.getUDN().getIdentifierString());
            result.add(itemDto);
        }
    }

    //
    // search all from one type
    //

    public SearchResultDto searchAllItems(SearchRequestDto searchRequest)
    {
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddMusicItems(searchRequest.searchRequest, searchRequest.sortCriteria, container, adjustRequestCount(searchRequest.requestCount), searchRequest.parentObjectID);

        return container;
    }

    public SearchResultDto searchAllArtists(SearchRequestDto searchRequest)
    {
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddArtistContainer(searchRequest.searchRequest, searchRequest.sortCriteria, container, adjustRequestCount(searchRequest.requestCount), searchRequest.parentObjectID);

        return container;
    }

    public SearchResultDto searchAllAlbum(SearchRequestDto searchRequest)
    {
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddAlbumContainer(searchRequest.searchRequest, searchRequest.sortCriteria, container, adjustRequestCount(searchRequest.requestCount), searchRequest.parentObjectID);

        return container;
    }

    public SearchResultDto searchAllPlaylist(SearchRequestDto searchRequest)
    {
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddPlaylistContainer(searchRequest.searchRequest, searchRequest.sortCriteria, container, adjustRequestCount(searchRequest.requestCount), searchRequest.parentObjectID);

        return container;
    }

	public List<ContainerDto> searchPlaylistItems(SearchRequestDto searchRequest, String parentObjectID) {
		List<ContainerDto> container = new ArrayList<>();

        SearchInput searchInput = new SearchInput();
        searchInput.ContainerID = getContainerId(parentObjectID);
        searchInput.SearchCriteria = String.format("( upnp:class derivedfrom \"%s\" and dc:title contains \"%s\")", "object.container.storageFolder", searchRequest.searchRequest);
        searchInput.StartingIndex = 0L;
        searchInput.Filter = "*";
        searchInput.RequestedCount = searchRequest.requestCount;
        searchInput.SortCriteria = searchRequest.sortCriteria;

        SearchOutput out = contentDirectoryService.search(searchInput);

        DIDLContent didl;
        try
        {
            didl = didlContent.generateDidlContent(out.Result);
            if (didl != null) 
            {
                addContainerObjects(container, didl);
            }
        }
        catch (Exception e)
        {
            log.warn("search error", e);
        }
        return container;
	}

}
