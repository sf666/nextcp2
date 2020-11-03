package nextcp.domainmodel.device.mediaserver;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject.Property.UPNP;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.container.MusicAlbum;
import org.fourthline.cling.support.model.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.BaseDevice;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.QuickSearchResultDto;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.ContentDirectoryService;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.SearchInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.SearchOutput;
import nextcp.util.BackendException;

/**
 * This class controls an av media server device
 */
public class MediaServerDevice extends BaseDevice
{
    private static final Logger log = LoggerFactory.getLogger(MediaServerDevice.class.getName());

    private ContentDirectoryService contentDirectoryService;

    public MediaServerDevice(RemoteDevice device)
    {
        super(device);
    }

    @PostConstruct
    private void init()
    {
        this.contentDirectoryService = new ContentDirectoryService(getUpnpService(), getDevice());
    }

    public ContentDirectoryService getContentDirectoryService()
    {
        return contentDirectoryService;
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
            didl = generateDidlContent(out.Result);
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
            ContainerDto containerDto = getDtoBuilder().buildContainerDto(didlObject);
            containerDto.mediaServerUDN = getUDN().getIdentifierString();
            container.add(containerDto);
        }
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
            didl = generateDidlContent(out.Result);
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

    public ContainerItemDto browseChildren(BrowseInput inp)
    {
        ContainerDto curContainer = getCurrentMeta(inp);

        inp.BrowseFlag = "BrowseDirectChildren";
        checkInp(inp);
        try
        {
            BrowseOutput out = contentDirectoryService.browse(inp);
            if (out != null && out.NumberReturned != null)
            {
                log.info("Response Objects: " + out.NumberReturned);
            }

            DIDLContent didl = generateDidlContent(out.Result);
            ContainerItemDto result = initEmptyContainerItemDto();

            result.currentContainer = curContainer;
            addContainerObjects(result, didl);
            addItemObjects(result.musicItemDto, didl);

            addDirectoryUpContainer(inp, result, curContainer);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
        }
    }

    private ContainerDto getCurrentMeta(BrowseInput inp)
    {
        ContainerDto result = new ContainerDto();
        inp.BrowseFlag = "BrowseMetadata";
        checkInp(inp);
        try
        {
            BrowseOutput out = contentDirectoryService.browse(inp);
            log.info("Response Objects: " + out.NumberReturned);
            if (out.NumberReturned == 1)
            {
                DIDLContent didl = generateDidlContent(out.Result);
                result = getDtoBuilder().buildContainerDto(didl.getFirstContainer());
            }
            result.mediaServerUDN = getUDN().getIdentifierString();
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
        }
    }

    public URI[] getAlbumArtURIs(DIDLContent didl)
    {
        List<URI> list = didl.getFirstContainer().getPropertyValues(UPNP.ALBUM_ART_URI.class);
        return list.toArray(new URI[list.size()]);
    }

    private ContainerItemDto initEmptyContainerItemDto()
    {
        ContainerItemDto result = new ContainerItemDto();
        result.containerDto = new ArrayList<>();
        result.musicItemDto = new ArrayList<>();
        result.albumDto = new ArrayList<>();
        return result;
    }

    private void addItemObjects(List<MusicItemDto> result, DIDLContent didl)
    {
        for (Item item : didl.getItems())
        {
            MusicItemDto itemDto = getDtoBuilder().buildItemDto(item, getUDN().getIdentifierString());
            result.add(itemDto);
        }
    }

    private void addContainerObjects(ContainerItemDto result, DIDLContent didl)
    {
        for (Container didlObject : didl.getContainers())
        {
            ContainerDto containerDto = getDtoBuilder().buildContainerDto(didlObject);
            containerDto.mediaServerUDN = getUDN().getIdentifierString();
            if (didlObject instanceof MusicAlbum)
            {
                result.albumDto.add(containerDto);
            }
            else
            {
                result.containerDto.add(containerDto);
            }
        }
    }

    private void addDirectoryUpContainer(BrowseInput inp, ContainerItemDto result, ContainerDto curContainer)
    {
        if (!inp.ObjectID.equals("0"))
        {
            result.containerDto.add(0, new ContainerDto(curContainer.parentID, "[PARENT]", "\u21EA [parent directory]", "", 0, "", "", false, getUDN().getIdentifierString(),
                    "assets/directory-up.png", "", 0, ""));
        }
    }

    public void browseMetadata(BrowseInput inp)
    {
        inp.BrowseFlag = getDefault(inp.BrowseFlag, "BrowseMetadata");
        checkInp(inp);
        BrowseOutput out = contentDirectoryService.browse(inp);

        try
        {
            DIDLContent didl = generateDidlContent(out.Result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
        }
    }

    private void checkInp(BrowseInput inp)
    {
        inp.Filter = getDefault(inp.Filter, "*");
        inp.ObjectID = getDefault(inp.ObjectID, "0");
        inp.StartingIndex = getDefault(inp.StartingIndex, 0L);
        inp.RequestedCount = getDefault(inp.RequestedCount, 999L);
        inp.SortCriteria = getDefault(inp.SortCriteria, "");
    }

    public MediaServerDto getAsDto()
    {
        return new MediaServerDto(getUDN().getIdentifierString(), getFriendlyName());
    }

}
