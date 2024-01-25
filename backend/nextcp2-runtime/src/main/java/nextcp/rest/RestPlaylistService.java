package nextcp.rest;

import java.util.ArrayList;
import java.util.List;

import org.jupnp.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
import nextcp.dto.ServerPlaylistDto;
import nextcp.dto.ServerPlaylistEntry;
import nextcp.dto.ToastrMessage;
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

    // @Autowired
    // private FilesystemIndexerService filesystemPlaylistService = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    //
    // Media Server based playlists
    // =======================================================================

    /**
     * Creates a server based playlist.
     * 
     * @param serverUdn server must support extended API
     * @param playlistName should be unique
     */
    @PostMapping("/createPlaylist/{serverUdn}")
    public void createPlaylist(@PathVariable("serverUdn") String serverUdn, @RequestBody String playlistName)
    {
        try
        {
            getExtendedMediaServerByUdn(serverUdn).createPlaylist(playlistName);
        }
        catch (Exception e)
        {
            log.warn("touchPlaylist", e);
        }
    }

    @PostMapping("/touchPlaylist/{serverUdn}")
    public void touchPlaylist(@PathVariable("serverUdn") String serverUdn, @RequestBody String playlistName)
    {
        try
        {
            getExtendedMediaServerByUdn(serverUdn).touchPlaylist(playlistName);
        }
        catch (Exception e)
        {
            log.warn("touchPlaylist", e);
        }
    }

    @PostMapping("/getDefaultPlaylists")
    public List<String> getDefaultPlaylists(@RequestBody String serverUdn)
    {
        try
        {
            return getExtendedMediaServerByUdn(serverUdn).getAllPlaylists();
        }
        catch (Exception e)
        {
            log.warn("getDefaultPlaylists", e);
            return new ArrayList<String>();
        }
    }

    @PostMapping("/getServerPlaylists")
    public List<ServerPlaylistDto> getServerPlaylists(@RequestBody String serverUdn)
    {
        try
        {
            return getExtendedMediaServerByUdn(serverUdn).getServerPlaylists();
        }
        catch (Exception e)
        {
            log.warn("getServerPlaylists", e);
            return new ArrayList<ServerPlaylistDto>();
        }
    }

    @PostMapping("/addToServerPlaylist")
    public void addToServerPlaylist(@RequestBody ServerPlaylistEntry addRequest)
    {
        try
        {
            getExtendedMediaServerByUdn(addRequest.serverUdn).addSongToPlaylist(addRequest.songid, addRequest.playlistName);
        }
        catch (Exception e)
        {
            publisher.publishEvent(new ToastrMessage(null, "error", "edit playlist", "Adding song failed. Message : " + e.getMessage()));
            log.warn("adding song to server playlist", e);
        }
    }

    @PostMapping("/removeFromServerPlaylist")
    public void removeFromServerPlaylist(@RequestBody ServerPlaylistEntry addRequest)
    {
        try
        {
            getExtendedMediaServerByUdn(addRequest.serverUdn).removeSongFromPlaylist(addRequest.songid, addRequest.playlistName);
        }
        catch (Exception e)
        {
            log.warn("removing song from server playlist", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "edit playlist", "Removing song failed. Message : " + e.getMessage()));
        }
    }

    //
    // UPnP playlist service
    // =======================================================================

    @PostMapping("/getState")
    public PlaylistState getState(@RequestBody String rendererUdn)
    {
        log.debug("/getState called");
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
        if (req.value == null)
        {
            publisher.publishEvent(new ToastrMessage(null, "error", "seekId", "shall not be null"));
            return;
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
            publisher.publishEvent(new ToastrMessage(null, "error", "seekId #" + req.value, e.getMessage()));
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
            inp.AfterId = 0L;
            inp.Metadata = req.streamMetadata;
            inp.Uri = req.streamUrl;
            device.getPlaylistServiceBridge().insert(inp);
        }
        catch (Exception e)
        {
            log.warn("seekId", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "insert ", req.streamUrl + "failed. Message" + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "adding songs ", "Adding folder to current renderer playlist failed. Message : " + e.getMessage()));
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
                publisher.publishEvent(new ToastrMessage(null, "error", "adding songs ", 
                	"Device " + rendererDevice.getFriendlyName() + " has no playlist implementation set."));
                return;
            }
            	
            rendererDevice.getPlaylistServiceBridge().deleteAll();
            if (req.shuffle != null)
            {
                rendererDevice.getPlaylistServiceBridge().setShuffle(req.shuffle);
            }
            ContainerItemDto itemsToAdd = getChildElements(req.containerDto.mediaServerUDN, req.containerDto.id);
            rendererDevice.getPlaylistServiceBridge().insertAndPlayContainer(itemsToAdd);
        }
        catch (Exception e)
        {
            log.warn("insertAndPlayContainer", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "adding songs ", 
            	getMediaRendererByUdn(req.mediaRendererUdn).getFriendlyName() + ": Cannot play folder. Message : " + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "pause ", "failed. Message : " + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "deleteAll ", "Failed. Message : " + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "setRepeat", "Failed. Message : " + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "delete ", "Failed. Message : " + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "/play ", "Failed. Message : " + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "next ", "Failed. Message : " + e.getMessage()));
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
            publisher.publishEvent(new ToastrMessage(null, "error", "previous ", "Failed. Message : " + e.getMessage()));
        }
    }
}
