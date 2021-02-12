package nextcp.rest;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.model.types.UDN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.dto.BrowseRequestDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.QuickSearchRequestDto;
import nextcp.dto.QuickSearchResultDto;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseInput;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/ContentDirectoryService")
public class RestContentDirectoryService
{
    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @PostMapping("/rescanContent")
    public void rescanContent(@RequestBody String mediaServerUDN)
    {
        if (StringUtils.isBlank(mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(mediaServerUDN));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + mediaServerUDN);
        }
        device.rescan();
    }

    private void checkDeviceAvailability(BrowseRequestDto browseRequest, MediaServerDevice device)
    {
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + browseRequest.mediaServerUDN);
        }
    }

    private void checkUdn(BrowseRequestDto browseRequest)
    {
        if (StringUtils.isBlank(browseRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
    }

    @PostMapping("/browseChildren")
    public ContainerItemDto browse(@RequestBody BrowseRequestDto browseRequest)
    {
        checkUdn(browseRequest);
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(browseRequest.mediaServerUDN));
        checkDeviceAvailability(browseRequest, device);
        BrowseInput inp = new BrowseInput();
        inp.ObjectID = browseRequest.objectID;
        inp.SortCriteria = browseRequest.sortCriteria;
        return device.browseChildren(inp);
    }

    @PostMapping("/quickSearch")
    public QuickSearchResultDto quickSearch(@RequestBody QuickSearchRequestDto searchRequest)
    {
        if (StringUtils.isBlank(searchRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }

        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
        }
        return device.quickSearch(searchRequest.searchRequest, adjustRequestCount(searchRequest.requestCount));
    }

    @PostMapping("/searchAllItems")
    public QuickSearchResultDto searchAllItems(@RequestBody QuickSearchRequestDto searchRequest)
    {
        if (StringUtils.isBlank(searchRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
        }
        return device.searchAllItems(searchRequest.searchRequest, adjustRequestCount(searchRequest.requestCount));
    }

    @PostMapping("/searchAllPlaylist")
    public QuickSearchResultDto searchAllPlaylist(@RequestBody QuickSearchRequestDto searchRequest)
    {
        if (StringUtils.isBlank(searchRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
        }
        return device.searchAllPlaylist(searchRequest.searchRequest, adjustRequestCount(searchRequest.requestCount));
    }

    @PostMapping("/searchAllAlbum")
    public QuickSearchResultDto searchAllAlbum(@RequestBody QuickSearchRequestDto searchRequest)
    {
        if (StringUtils.isBlank(searchRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
        }
        return device.searchAllAlbum(searchRequest.searchRequest, adjustRequestCount(searchRequest.requestCount));
    }

    @PostMapping("/searchAllArtists")
    public QuickSearchResultDto searchAllArtists(@RequestBody QuickSearchRequestDto searchRequest)
    {
        if (StringUtils.isBlank(searchRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(searchRequest.mediaServerUDN));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + searchRequest.mediaServerUDN);
        }
        return device.searchAllArtists(searchRequest.searchRequest, adjustRequestCount(searchRequest.requestCount));
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
}
