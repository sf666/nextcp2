package nextcp.upnp.device.mediaserver;

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

import nextcp.domainmodel.device.mediaserver.search.SearchSupport;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.QuickSearchResultDto;
import nextcp.upnp.device.BaseDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.ContentDirectoryService;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseOutput;
import nextcp.util.BackendException;

/**
 * This class controls an av media server device
 */
public class MediaServerDevice extends BaseDevice
{
    private static final Logger log = LoggerFactory.getLogger(MediaServerDevice.class.getName());

    private ContentDirectoryService contentDirectoryService;

    private SearchSupport searchSupportDelegate = null;

    public MediaServerDevice(RemoteDevice device)
    {
        super(device);
    }

    @PostConstruct
    private void init()
    {
        this.contentDirectoryService = new ContentDirectoryService(getUpnpService(), getDevice());
        searchSupportDelegate = new SearchSupport(contentDirectoryService, this);
    }

    public ContentDirectoryService getContentDirectoryService()
    {
        return contentDirectoryService;
    }

    public QuickSearchResultDto quickSearch(String quickSearch, long requestCount)
    {
        return searchSupportDelegate.quickSearch(quickSearch, requestCount);
    }

    public QuickSearchResultDto searchAllItems(String quickSearch, long requestCount)
    {
        return searchSupportDelegate.searchAllItems(quickSearch, requestCount);
    }

    public QuickSearchResultDto searchAllArtists(String quickSearch, long requestCount)
    {
        return searchSupportDelegate.searchAllArtists(quickSearch, requestCount);
    }

    public QuickSearchResultDto searchAllAlbum(String quickSearch, long requestCount)
    {
        return searchSupportDelegate.searchAllAlbum(quickSearch, requestCount);
    }

    public QuickSearchResultDto searchAllPlaylist(String quickSearch, long requestCount)
    {
        return searchSupportDelegate.searchAllPlaylist(quickSearch, requestCount);
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
                if (log.isDebugEnabled())
                {
                    log.debug("DIDL Object : " + out.Result);
                }
            }

            DIDLContent didl = generateDidlContent(out.Result);
            ContainerItemDto result = initEmptyContainerItemDto();

            result.currentContainer = curContainer;
            result.parentFolderTitle = removeMinimTagChars(getParentName(curContainer));
            addContainerObjects(result, didl);
            addItemObjects(result.musicItemDto, didl);

            // addDirectoryUpContainer(inp, result, curContainer);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
        }
    }

    /**
     * Removes '// ' in front of the title
     * 
     * @param title
     * @return
     */
    private String removeMinimTagChars(String title)
    {
        if (title.startsWith(">> "))
        {
            return title.substring(3);
        }
        return title;
    }

    private String getParentName(ContainerDto current)
    {
        if (current == null || current.id == null || current.id.equals("0"))
        {
            return "";
        }
        BrowseInput bi = new BrowseInput();
        bi.ObjectID = current.parentID;
        bi.SortCriteria = "";
        bi.BrowseFlag = "BrowseMetadata";
        checkInp(bi);
        try
        {
            BrowseOutput out = contentDirectoryService.browse(bi);
            if (out.NumberReturned == 1)
            {
                DIDLContent didl = generateDidlContent(out.Result);
                return getDtoBuilder().buildContainerDto(didl.getFirstContainer()).title;
            }
            return "";
        }
        catch (Exception e)
        {
            log.warn("cannot get parent name", e);
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
                // minimserver support
                result.title = removeMinimTagChars(result.title);
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
        result.parentFolderTitle = "";
        result.minimServerSupportTags = new ArrayList<ContainerDto>();
        return result;
    }

    private void addContainerObjects(ContainerItemDto result, DIDLContent didl)
    {
        for (Container didlObject : didl.getContainers())
        {
            ContainerDto containerDto = getDtoBuilder().buildContainerDto(didlObject);
            containerDto.mediaServerUDN = getUDN().getIdentifierString();
            if (didlObject.getTitle().startsWith(">>"))
            {
                // minim server support for tags ...
                if (!didlObject.getId().endsWith("$hchide"))
                {
                    containerDto.title = containerDto.title.substring(3);
                    result.minimServerSupportTags.add(containerDto);
                }
            }
            else if (didlObject.getTitle().startsWith("#--"))
            {
                // Maybe the renderer in misconfigred in UMS.
                // ignore Transcode folder. 
                log.debug("Ignore #--Transcode--# folder ... maybe UMS renderer is misconfigured");
            }
            else
            {
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

    private void addItemObjects(List<MusicItemDto> result, DIDLContent didl)
    {
        for (Item item : didl.getItems())
        {
            MusicItemDto itemDto = getDtoBuilder().buildItemDto(item, getUDN().getIdentifierString());
            result.add(itemDto);
        }
    }

}
