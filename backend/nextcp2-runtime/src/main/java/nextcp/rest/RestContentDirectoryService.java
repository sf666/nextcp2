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

import nextcp.domainmodel.device.DeviceRegistry;
import nextcp.domainmodel.device.mediaserver.MediaServerDevice;
import nextcp.dto.BrowseRequestDto;
import nextcp.dto.ContainerItemDto;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseInput;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/ContentDirectoryService")
public class RestContentDirectoryService
{
    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @PostMapping("/browseChildren")
    public ContainerItemDto browse(@RequestBody BrowseRequestDto browseRequest)
    {
        if (StringUtils.isBlank(browseRequest.mediaServerUDN))
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "media server ID shall not be empty");
        }
        MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(browseRequest.mediaServerUDN));
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "unknown media server : " + browseRequest.mediaServerUDN);
        }
        BrowseInput inp = new BrowseInput();
        inp.ObjectID = browseRequest.objectID;
        inp.SortCriteria = browseRequest.sortCriteria;
        return device.browseChildren(inp);
    }

}
