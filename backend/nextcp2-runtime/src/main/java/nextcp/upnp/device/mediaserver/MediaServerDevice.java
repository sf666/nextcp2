package nextcp.upnp.device.mediaserver;

import java.io.File;
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
import nextcp.dto.SearchRequestDto;
import nextcp.dto.SearchResultDto;
import nextcp.upnp.GenActionException;
import nextcp.upnp.device.BaseDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.ContentDirectoryService;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseOutput;
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
        try
        {
            searchSupportDelegate = new SearchSupport(contentDirectoryService, this);
        }
        catch (Exception e)
        {
            log.info("search support ...", e);
        }
    }

    public ContentDirectoryService getContentDirectoryService()
    {
        return contentDirectoryService;
    }

    public SearchResultDto quickSearch(SearchRequestDto searchRequest)
    {
        return searchSupportDelegate.quickSearch(searchRequest);
    }

    public SearchResultDto searchAllItems(SearchRequestDto searchRequest)
    {
        return searchSupportDelegate.searchAllItems(searchRequest);
    }

    public SearchResultDto searchAllArtists(SearchRequestDto searchRequest)
    {
        return searchSupportDelegate.searchAllArtists(searchRequest);
    }

    public SearchResultDto searchAllAlbum(SearchRequestDto searchRequest)
    {
        return searchSupportDelegate.searchAllAlbum(searchRequest);
    }

    public SearchResultDto searchAllPlaylist(SearchRequestDto searchReques)
    {
        return searchSupportDelegate.searchAllPlaylist(searchReques);
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
            result.totalMatches = out.TotalMatches;

            result.currentContainer = curContainer;
//            result.parentFolderTitle = removeMinimTagChars(getParentName(curContainer));
            addContainerObjects(result, didl);
            addItemObjects(result.musicItemDto, didl);

            return result;
        }
        catch (Exception e)
        {
            throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
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
//                result.title = removeMinimTagChars(result.title);
            }
            result.mediaServerUDN = getUDN().getIdentifierString();
            return result;
        }
        catch (GenActionException e)
        {
            e.printStackTrace();
            throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.description);
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
        return new MediaServerDto(getUDN().getIdentifierString(), getFriendlyName(), false);
    }

    private void addItemObjects(List<MusicItemDto> result, DIDLContent didl)
    {
        for (Item item : didl.getItems())
        {
            MusicItemDto itemDto = getDtoBuilder().buildItemDto(item, getUDN().getIdentifierString());
            result.add(itemDto);
        }
    }

    public void rescan()
    {
        log.warn("rescan not implemented for this device : " + getFriendlyName());
    }

    public void rescanFile(File f)
    {
        log.warn("scan file not implemented for this device : " + getFriendlyName());
    }

}
