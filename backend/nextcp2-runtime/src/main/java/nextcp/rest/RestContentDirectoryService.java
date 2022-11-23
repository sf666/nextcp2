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
import nextcp.dto.SearchRequestDto;
import nextcp.dto.SearchResultDto;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/ContentDirectoryService")
public class RestContentDirectoryService extends BaseRestService
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

    @PostMapping("/browseChildren")
    public ContainerItemDto browse(@RequestBody BrowseRequestDto browseRequest)
    {
        checkUdn(browseRequest);
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(browseRequest.mediaServerUDN));
        checkDeviceAvailability(browseRequest, device);
        BrowseInput inp = new BrowseInput();
        inp.ObjectID = browseRequest.objectID;
        inp.SortCriteria = browseRequest.sortCriteria;
        inp.StartingIndex = browseRequest.start;
        inp.RequestedCount = browseRequest.count; 
        return device.browseChildren(inp);
    }

    @PostMapping("/quickSearch")
    public SearchResultDto quickSearch(@RequestBody SearchRequestDto searchRequest)
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
        return device.quickSearch(searchRequest);
    }

    @PostMapping("/searchAllItems")
    public SearchResultDto searchAllItems(@RequestBody SearchRequestDto searchRequest)
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
        return device.searchAllItems(searchRequest);
    }

    @PostMapping("/searchAllPlaylist")
    public SearchResultDto searchAllPlaylist(@RequestBody SearchRequestDto searchRequest)
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
        return device.searchAllPlaylist(searchRequest);
    }

    @PostMapping("/searchAllAlbum")
    public SearchResultDto searchAllAlbum(@RequestBody SearchRequestDto searchRequest)
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
        return device.searchAllAlbum(searchRequest);
    }

    @PostMapping("/searchAllArtists")
    public SearchResultDto searchAllArtists(@RequestBody SearchRequestDto searchRequest)
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
        return device.searchAllArtists(searchRequest);
    }
}
