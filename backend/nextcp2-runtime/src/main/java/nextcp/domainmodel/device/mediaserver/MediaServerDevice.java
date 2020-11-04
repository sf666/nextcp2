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
import nextcp.domainmodel.device.mediaserver.search.SearchSupport;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.QuickSearchResultDto;
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

    public QuickSearchResultDto quickSearch(String quickSearch)
    {
        return searchSupportDelegate.quickSearch(quickSearch);
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

            //addDirectoryUpContainer(inp, result, curContainer);
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

    private void addItemObjects(List<MusicItemDto> result, DIDLContent didl)
    {
        for (Item item : didl.getItems())
        {
            MusicItemDto itemDto = getDtoBuilder().buildItemDto(item, getUDN().getIdentifierString());
            result.add(itemDto);
        }
    }

}
