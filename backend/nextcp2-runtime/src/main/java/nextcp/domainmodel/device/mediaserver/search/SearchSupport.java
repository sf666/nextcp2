package nextcp.domainmodel.device.mediaserver.search;

import java.util.ArrayList;
import java.util.List;

import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.dto.ContainerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.SearchRequestDto;
import nextcp.dto.SearchResultDto;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.ContentDirectoryService;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.SearchInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.SearchOutput;
import nextcp.util.DidlContent;

/**
 * Content Directory Service - Search Support Class
 */
public class SearchSupport
{
    private static final Logger log = LoggerFactory.getLogger(SearchSupport.class.getName());

    private ContentDirectoryService contentDirectoryService = null;
    private DidlContent didlContent = new DidlContent();
    private MediaServerDevice mediaServerDevice = null;

    public SearchSupport(ContentDirectoryService contentDirectoryService, MediaServerDevice mediaServerDevice)
    {
        this.contentDirectoryService = contentDirectoryService;
        this.mediaServerDevice = mediaServerDevice;
    }

    public SearchResultDto quickSearch(SearchRequestDto searchRequest)
    {
        String quickSearch = searchRequest.searchRequest;
        long requestCount = adjustRequestCount(searchRequest.requestCount);
        
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddMusicItems(quickSearch, container, requestCount);
        searchAndAddArtistContainer(quickSearch, container, requestCount);
        searchAndAddAlbumContainer(quickSearch, container, requestCount);
        searchAndAddPlaylistContainer(quickSearch, container, requestCount);
        
        return container;
    }

    private long adjustRequestCount(Long givenRequestCount)
    {
        if (givenRequestCount == null)
        {
            return 3;
        }

        givenRequestCount = Math.min(10, givenRequestCount);
        givenRequestCount = Math.max(2, givenRequestCount);
        return givenRequestCount;

    }

    
    private void searchAndAddPlaylistContainer(String quickSearch, SearchResultDto container, long requestCount)
    {
        searchAndAddArtistContainer(quickSearch, container.playlistItems, "object.container.playlistContainer", requestCount);
    }

    private void searchAndAddAlbumContainer(String quickSearch, SearchResultDto container, long requestCount)
    {
        searchAndAddArtistContainer(quickSearch, container.albumItems, "object.container.album", requestCount);
    }

    private void searchAndAddArtistContainer(String quickSearch, SearchResultDto container, long requestCount)
    {
        searchAndAddArtistContainer(quickSearch, container.artistItems, "object.container.person", requestCount);
    }

    private void searchAndAddArtistContainer(String quickSearch, List<ContainerDto> container, String upnpClass, long requestCount)
    {
        SearchInput searchInput = new SearchInput();
        searchInput.ContainerID = "0";
        searchInput.SearchCriteria = String.format("( upnp:class derivedfrom \"%s\" and dc:title contains \"%s\")", upnpClass, quickSearch);
        searchInput.StartingIndex = 0L;
        searchInput.Filter = "*";
        searchInput.RequestedCount = requestCount;
        searchInput.SortCriteria = "";

        SearchOutput out = contentDirectoryService.search(searchInput);

        DIDLContent didl;
        try
        {
            didl = didlContent.generateDidlContent(out.Result);
            addContainerObjects(container, didl);
        }
        catch (Exception e)
        {
            log.warn("search error", e);
        }
    }

    private void searchAndAddMusicItems(String quickSearch, SearchResultDto container, Long requestCount)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("( upnp:class = \"object.item.audioItem.musicTrack\""); // upnp:class derivedfrom “object.container.person”
        sb.append(String.format(" and dc:title contains \"%s\")", quickSearch));

        SearchInput searchInput = new SearchInput();
        searchInput.ContainerID = "0";
        searchInput.SearchCriteria = sb.toString();
        searchInput.StartingIndex = 0L;
        searchInput.Filter = "*";
        searchInput.RequestedCount = requestCount;
        searchInput.SortCriteria = "";

        DIDLContent didl;
        try
        {
            SearchOutput out = contentDirectoryService.search(searchInput);
            didl = didlContent.generateDidlContent(out.Result);
            addItemObjects(container.musicItems, didl);
        }
        catch (Exception e)
        {
            log.warn("search error", e);
        }
    }

    private SearchResultDto initEmptySearchResultContainer()
    {
        SearchResultDto dto = new SearchResultDto();
        dto.albumItems = new ArrayList<ContainerDto>();
        dto.artistItems = new ArrayList<ContainerDto>();
        dto.musicItems = new ArrayList<>();
        dto.playlistItems = new ArrayList<>();
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

        searchAndAddMusicItems(searchRequest.searchRequest, container, adjustRequestCount(searchRequest.requestCount));

        return container;
    }

    public SearchResultDto searchAllArtists(SearchRequestDto searchRequest)
    {
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddArtistContainer(searchRequest.searchRequest, container, adjustRequestCount(searchRequest.requestCount));

        return container;
    }

    public SearchResultDto searchAllAlbum(SearchRequestDto searchRequest)
    {
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddAlbumContainer(searchRequest.searchRequest, container, adjustRequestCount(searchRequest.requestCount));

        return container;
    }

    public SearchResultDto searchAllPlaylist(SearchRequestDto searchRequest)
    {
        SearchResultDto container = initEmptySearchResultContainer();

        searchAndAddPlaylistContainer(searchRequest.searchRequest, container, adjustRequestCount(searchRequest.requestCount));

        return container;
    }
}

