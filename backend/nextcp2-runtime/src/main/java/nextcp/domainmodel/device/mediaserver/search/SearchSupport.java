package nextcp.domainmodel.device.mediaserver.search;

import java.util.ArrayList;
import java.util.List;

import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.mediaserver.MediaServerDevice;
import nextcp.dto.ContainerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.QuickSearchResultDto;
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

    public QuickSearchResultDto quickSearch(String quickSearch)
    {
        QuickSearchResultDto container = initEmptySearchResultContainer();

        searchAndAddMusicItems(quickSearch, container);
        searchAndAddArtistContainer(quickSearch, container);
        return container;

        /**
         * if (!StringUtils.isBlank(searchRequest.date_from) && SearchCaps.contains("dc:date")) { sb.append(String.format(" and dc:date >= \"%s\"", searchRequest.date_from)); } if
         * (!StringUtils.isBlank(searchRequest.date_to) && SearchCaps.contains("dc:date")) { sb.append(String.format(" and dc:date <= \"%s\"", searchRequest.date_to)); } if
         * (!StringUtils.isBlank(searchRequest.creator) && SearchCaps.contains("dc:creator")) { sb.append(String.format(" and dc:creator contains \"%s\"", searchRequest.title)); }
         * if (!StringUtils.isBlank(searchRequest.artist) && SearchCaps.contains("upnp:artist")) { sb.append(String.format(" and upnp:artist contains \"%s\"",
         * searchRequest.artist)); } if (!StringUtils.isBlank(searchRequest.album) && SearchCaps.contains("upnp:album")) { sb.append(String.format(" or ( upnp:class =
         * \"object.container.album.musicAlbum\" and upnp:album contains \"%s\" )", searchRequest.album)); } if (!StringUtils.isBlank(searchRequest.genre) &&
         * SearchCaps.contains("upnp:genre")) { sb.append(String.format(" and upnp:genre contains \"%s\"", searchRequest.genre)); }
         * 
         * sb.append(" )");
         * 
         * BrowseResponse response = search(searchRequest.containerID, sb.toString(), "*", 0, 0, "");
         * 
         * if (response != null) { log.info("Search finished with " + response.getTotalMatches() + " entries."); for (DIDLObject item : response.getResult()) { if
         * (item.getObjectClass().contains(DIDLConstants.UPNP_CLASS_MUSIC_TRACK)) { resultList.add(new FileContainerDto((DIDLMusicTrack) item, getDeviceUdn(), 0, true)); } } } else
         * { log.info("Search finished with no result."); }
         */

    }

    private void searchAndAddMusicItems(String quickSearch, QuickSearchResultDto container)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("( upnp:class = \"object.item.audioItem.musicTrack\""); // upnp:class derivedfrom “object.container.person”
        sb.append(String.format(" and dc:title contains \"%s\")", quickSearch));

        SearchInput searchInput = new SearchInput();
        searchInput.ContainerID = "0";
        searchInput.SearchCriteria = sb.toString();
        searchInput.StartingIndex = 0L;
        searchInput.Filter = "*";
        searchInput.RequestedCount = 3L;
        searchInput.SortCriteria = "";

        SearchOutput out = contentDirectoryService.search(searchInput);

        DIDLContent didl;
        try
        {
            didl = didlContent.generateDidlContent(out.Result);
            addItemObjects(container.musicItems, didl);
        }
        catch (Exception e)
        {
            log.warn("search error", e);
        }
    }

    private QuickSearchResultDto initEmptySearchResultContainer()
    {
        QuickSearchResultDto dto = new QuickSearchResultDto();
        dto.albumItems = new ArrayList<ContainerDto>();
        dto.artistItems = new ArrayList<ContainerDto>();
        dto.musicItems = new ArrayList();
        dto.playlistItems = new ArrayList();
        return dto;
    }

    private void searchAndAddArtistContainer(String quickSearch, QuickSearchResultDto container)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("( upnp:class derivedfrom \"object.container.person\""); // upnp:class derivedfrom “object.container.person”
        sb.append(String.format(" and dc:title contains \"%s\")", quickSearch));

        SearchInput searchInput = new SearchInput();
        searchInput.ContainerID = "0";
        searchInput.SearchCriteria = sb.toString();
        searchInput.StartingIndex = 0L;
        searchInput.Filter = "*";
        searchInput.RequestedCount = 3L;
        searchInput.SortCriteria = "";

        SearchOutput out = contentDirectoryService.search(searchInput);

        DIDLContent didl;
        try
        {
            didl = didlContent.generateDidlContent(out.Result);
            addContainerObjects(container.artistItems, didl);
        }
        catch (Exception e)
        {
            log.warn("search error", e);
        }
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
}
