package nextcp.rest;

import java.util.List;
import org.jupnp.model.types.UDN;
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
import nextcp.dto.ContainerItemDto;
import nextcp.dto.GenericBooleanRequest;
import nextcp.dto.GenericNumberRequest;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlayRequestDto;
import nextcp.dto.PlaylistAddContainerRequest;
import nextcp.dto.PlaylistState;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.InsertInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/PlaylistService")
public class RestPlaylistService extends BaseRestService
{

    private static final Logger log = LoggerFactory.getLogger(RestPlaylistService.class.getName());

    @Autowired
    private DeviceRegistry deviceRegistry = null;

    //
    // UPnP playlist service
    // =======================================================================

    /**
     * A polled status read, so it degrades to null instead of failing the request the way the action
     * endpoints below do : an absent renderer is a normal state here, not something to report on
     * every poll. The client treats null as "no state" already.
     */
    @PostMapping("/getState")
    public PlaylistState getState(@RequestBody String rendererUdn)
    {
    	try {
            log.debug("/getState called");
            MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
            checkDevice(device);
            return device.getPlaylistServiceBridge().getState();
    	} catch (Exception e) {
    		log.error("getState", e);
    		return null;
    	}
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
        if (req.value == null)
        {
            throw failed("No playlist entry given to jump to.", null);
        }
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(req.deviceUDN);
            checkDevice(device);
            device.getPlaylistServiceBridge().seekId(req.value);
        }
        catch (Exception e)
        {
            log.warn("seekId", e);
            throw failed("Cannot jump to playlist entry #" + req.value + " : " + e.getMessage(), e);
        }
    }

    @PostMapping("/insert")
    public void insert(@RequestBody PlayRequestDto req)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(req.mediaRendererDto.udn);
            checkDevice(device);
            InsertInput inp = new InsertInput();
            inp.Metadata = req.streamMetadata;
            inp.Uri = req.streamUrl;
            device.getPlaylistServiceBridge().insertLast(inp);
        }
        catch (Exception e)
        {
            log.warn("insert", e);
            throw failed("Cannot add " + req.streamUrl + " : " + e.getMessage(), e);
        }
    }
    
    @PostMapping("/insertNext")
    public void insertNext(@RequestBody PlayRequestDto req)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(req.mediaRendererDto.udn);
            checkDevice(device);
            InsertInput inp = new InsertInput();
            inp.AfterId = 0L;
            inp.Metadata = req.streamMetadata;
            inp.Uri = req.streamUrl;
            device.getPlaylistServiceBridge().insertNext(inp);
        }
        catch (Exception e)
        {
            log.warn("insertNext", e);
            throw failed("Cannot add " + req.streamUrl + " as the next entry : " + e.getMessage(), e);
        }
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
            log.warn("insertContainer", e);
            throw failed("Adding the folder to the renderer playlist failed : " + e.getMessage(), e);
        }
    }

    private MediaRendererDevice addSongsToRenderDevice(PlaylistAddContainerRequest req, MediaRendererDevice rendererDevice)
    {
        rendererDevice.getPlaylistServiceBridge().insertContainer(getSongsToAdd(req));
        return rendererDevice;
    }

    /**
     * Songs the request asks for.
     *
     * The UI may send an explicit track list — that is how an active browse filter reaches the
     * renderer, since the filter only exists in the browser and this service would otherwise
     * re-browse the full container. Without such a list, everything the container holds is used.
     */
    private ContainerItemDto getSongsToAdd(PlaylistAddContainerRequest req)
    {
        if (req.musicItemDto != null && !req.musicItemDto.isEmpty())
        {
            ContainerItemDto items = new ContainerItemDto();
            items.currentContainer = req.containerDto;
            items.musicItemDto = req.musicItemDto;
            return items;
        }
        return getChildElements(req.containerDto.mediaServerUDN, req.containerDto.id);
    }

    private ContainerItemDto getChildElements(String udn, String containerID)
    {
        MediaServerDevice serverDevice = deviceRegistry.getMediaServerByUDN(new UDN(udn));
        BrowseInput browseInp = new BrowseInput();
        browseInp.ObjectID = containerID;
        browseInp.StartingIndex = 0L;
        browseInp.RequestedCount = 2000L;
        ContainerItemDto containerWithChildren = serverDevice.browseChildren(browseInp);
        return containerWithChildren;
    }

    @PostMapping("/insertAndPlayContainer")
    public void insertAndPlay(@RequestBody PlaylistAddContainerRequest req)
    {
        try
        {
            MediaRendererDevice rendererDevice = getMediaRendererByUdn(req.mediaRendererUdn);
            checkDevice(rendererDevice);
            if (rendererDevice.getPlaylistServiceBridge() == null) {
                log.warn("{} no playlist service bridge available", rendererDevice.getFriendlyName());
                throw failed("Device " + rendererDevice.getFriendlyName() + " has no playlist implementation set.", null);
            }
            	
            rendererDevice.getPlaylistServiceBridge().deleteAll();
            if (req.shuffle != null)
            {
                rendererDevice.getPlaylistServiceBridge().setShuffle(req.shuffle);
            }
            rendererDevice.getPlaylistServiceBridge().insertAndPlayContainer(getSongsToAdd(req));
        }
        catch (Exception e)
        {
            log.warn("insertAndPlayContainer", e);
            throw failed("Cannot play the folder : " + e.getMessage(), e);
        }
    }

    @PostMapping("/pause")
    public void pause(@RequestBody String rendererUdn)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
            checkDevice(device);
            device.getPlaylistServiceBridge().pause();
        }
        catch (Exception e)
        {
            log.warn("pause", e);
            throw failed("Cannot pause : " + e.getMessage(), e);
        }
    }

    @PostMapping("/deleteAll")
    public void deleteAll(@RequestBody String rendererUdn)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
            checkDevice(device);
            device.getPlaylistServiceBridge().deleteAll();
        }
        catch (Exception e)
        {
            log.warn("deleteAll", e);
            throw failed("Cannot clear the playlist : " + e.getMessage(), e);
        }
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
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(req.deviceUDN);
            checkDevice(device);
            device.getPlaylistServiceBridge().setRepeat(req.value);
        }
        catch (Exception e)
        {
            log.warn("setRepeat", e);
            throw failed("Cannot change repeat : " + e.getMessage(), e);
        }
    }

    @PostMapping("/delete")
    public void deleteId(@RequestBody GenericNumberRequest req)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(req.deviceUDN);
            checkDevice(device);
            device.getPlaylistServiceBridge().deleteId(req.value);
        }
        catch (Exception e)
        {
            log.warn("delete", e);
            throw failed("Cannot remove the playlist entry : " + e.getMessage(), e);
        }
    }

    @PostMapping("/play")
    public void play(@RequestBody String rendererUdn)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
            checkDevice(device);
            device.getPlaylistServiceBridge().play();
        }
        catch (Exception e)
        {
            log.warn("/play", e);
            throw failed("Cannot start playback : " + e.getMessage(), e);
        }
    }

    @PostMapping("/next")
    public void next(@RequestBody String rendererUdn)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
            checkDevice(device);
            device.getPlaylistServiceBridge().next();
        }
        catch (Exception e)
        {
            log.warn("next", e);
            throw failed("Cannot skip to the next entry : " + e.getMessage(), e);
        }
    }

    @PostMapping("/previous")
    public void previous(@RequestBody String rendererUdn)
    {
        try
        {
            MediaRendererDevice device = getMediaRendererByUdn(rendererUdn);
            checkDevice(device);
            device.getPlaylistServiceBridge().previous();
        }
        catch (Exception e)
        {
            log.warn("previous", e);
            throw failed("Cannot skip to the previous entry : " + e.getMessage(), e);
        }
    }
}
