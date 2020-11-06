package nextcp.rest;

import java.util.List;

import org.fourthline.cling.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.domainmodel.device.DeviceRegistry;
import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.domainmodel.device.mediaserver.MediaServerDevice;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.GenericBooleanRequest;
import nextcp.dto.GenericNumberRequest;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlayRequestDto;
import nextcp.dto.PlaylistAddContainerRequest;
import nextcp.dto.PlaylistState;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.InsertInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseInput;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/PlaylistService")
public class RestPlaylistService extends BaseRestService
{
    private static final Logger log = LoggerFactory.getLogger(RestPlaylistService.class.getName());

    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @PostMapping("/getState")
    public PlaylistState getState(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        checkDevice(device);
        return device.getPlaylistServiceBridge().getState();
    }

    @PostMapping("/getPlaylistItems")
    public List<MusicItemDto> getPlaylistItems(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        checkDevice(device);
        return device.getPlaylistServiceBridge().getPlaylistItems();
    }

    @PostMapping("/setShuffle")
    public void setShuffle(@RequestBody GenericBooleanRequest req)
    {
        MediaRendererDevice device = getMediaRendererByUdn(req.deviceUDN);
        checkDevice(device);
        device.getPlaylistServiceBridge().setShuffle(req.value);
    }

    @PostMapping("/seekId")
    public void seekId(@RequestBody GenericNumberRequest req)
    {
        MediaRendererDevice device = getMediaRendererByUdn(req.deviceUDN);
        checkDevice(device);
        device.getPlaylistServiceBridge().seekId(req.value);
    }

    @PostMapping("/insert")
    public void insert(@RequestBody PlayRequestDto req)
    {
        MediaRendererDevice device = getMediaRendererByUdn(req.mediaRendererDto.udn);
        checkDevice(device);
        InsertInput inp = new InsertInput();
        inp.AfterId = 0L;
        inp.Metadata = req.streamMetadata;
        inp.Uri = req.streamUrl;
        device.getPlaylistServiceBridge().insert(inp);
    }

    @PostMapping("/insertContainer")
    public void insert(@RequestBody PlaylistAddContainerRequest req)
    {
        try
        {
            MediaRendererDevice rendererDevice = getMediaRendererByUdn(req.mediaRendererUdn);
            checkDevice(rendererDevice);
            addSongsToRenderDevice(req, rendererDevice);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Adding Folder : " + e.getMessage());
        }
    }

    private MediaRendererDevice addSongsToRenderDevice(PlaylistAddContainerRequest req, MediaRendererDevice rendererDevice)
    {
        ContainerItemDto itemsToAdd = getChildElements(req.containerDto.mediaServerUDN, req.containerDto.id);
        rendererDevice.getPlaylistServiceBridge().insertContainer(itemsToAdd);
        return rendererDevice;
    }

    private ContainerItemDto getChildElements(String udn, String containerID)
    {
        MediaServerDevice serverDevice = deviceRegistry.getMediaServerByUDN(new UDN(udn));
        BrowseInput browseInp = new BrowseInput();
        browseInp.ObjectID = containerID;
        ContainerItemDto itemsToAdd = serverDevice.browseChildren(browseInp);
        return itemsToAdd;
    }

    @PostMapping("/insertAndPlayContainer")
    public void insertAndPlay(@RequestBody PlaylistAddContainerRequest req)
    {
        try
        {
            MediaRendererDevice rendererDevice = getMediaRendererByUdn(req.mediaRendererUdn);
            checkDevice(rendererDevice);
            if (req.shuffle != null) 
            {
                rendererDevice.getPlaylistServiceBridge().setShuffle(req.shuffle);
            }
            ContainerItemDto itemsToAdd = getChildElements(req.containerDto.mediaServerUDN, req.containerDto.id);
            rendererDevice.getPlaylistServiceBridge().insertAndPlayContainer(itemsToAdd);
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Adding folder & play" + e.getMessage());
        }
    }

    @PostMapping("/pause")
    public void pause(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        checkDevice(device);
        device.getPlaylistServiceBridge().pause();
    }

    @PostMapping("/deleteAll")
    public void deleteAll(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        checkDevice(device);
        device.getPlaylistServiceBridge().deleteAll();
    }

    private void checkDevice(MediaRendererDevice device)
    {
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Please select an output device (media renderer).");
        }
    }

    @PostMapping("/setRepeat")
    public void repeat(@RequestBody GenericBooleanRequest req)
    {
        MediaRendererDevice device = getMediaRendererByUdn(req.deviceUDN);
        checkDevice(device);
        device.getPlaylistServiceBridge().setRepeat(req.value);
    }

    @PostMapping("/delete")
    public void deleteId(@RequestBody GenericNumberRequest req)
    {
        MediaRendererDevice device = getMediaRendererByUdn(req.deviceUDN);
        checkDevice(device);
        device.getPlaylistServiceBridge().deleteId(req.value);
    }

    @PostMapping("/play")
    public void play(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        checkDevice(device);
        device.getPlaylistServiceBridge().play();
    }

    @PostMapping("/next")
    public void next(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        checkDevice(device);
        device.getPlaylistServiceBridge().next();
    }

    @PostMapping("/previous")
    public void previous(@RequestBody String rendererUdn)
    {
        MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
        checkDevice(device);
        device.getPlaylistServiceBridge().previous();
    }
}
