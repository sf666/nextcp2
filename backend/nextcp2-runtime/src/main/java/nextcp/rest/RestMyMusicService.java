package nextcp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.domainmodel.services.MyMusicService;
import nextcp.dto.GenericResult;
import nextcp.service.ToastEventPublisher;
import nextcp.upnp.GenActionException;
import nextcp.util.UpnpErrorDescriptionHandler;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MyMusicService")
public class RestMyMusicService extends BaseRestService
{
    @Autowired
    @Lazy
    private MyMusicService myMusicService = null;

	@Autowired
	private ToastEventPublisher toast = null;
	
	private UpnpErrorDescriptionHandler errorHandler = new UpnpErrorDescriptionHandler();
	
	
    public RestMyMusicService()
    {
    }

    @PostMapping("/likeAlbum/{deviceId}")
    public void likeAlbum(@RequestBody String uuid, @PathVariable("deviceId") String deviceId)
    {
        myMusicService.likeAlbum(uuid, getExtendedMediaServerByUdn(deviceId));
    }

    @PostMapping("/deleteAlbumLike/{deviceId}")
    public void deleteAlbumLike(@RequestBody String uuid, @PathVariable("deviceId") String deviceId)
    {
        myMusicService.dislikeAlbum(uuid, getExtendedMediaServerByUdn(deviceId));
    }

    @PostMapping("/isAlbumLiked/{deviceId}")
    public boolean isAlbumLiked(@RequestBody String uuid, @PathVariable("deviceId") String deviceId)
    {
        boolean status = myMusicService.isAlbumLiked(uuid, getExtendedMediaServerByUdn(deviceId));
        return status;
    }

    @GetMapping("/backupLikedAlbums/{deviceId}")
    public void backupLikedAlbum(@PathVariable("deviceId") String deviceId)
    {
        try
        {
            myMusicService.backupMyMusic(getExtendedMediaServerByUdn(deviceId));
            toast.publishSuccessMessage(null, "backup liked albums", "success");
        }
        catch (Exception e)
        {
            toast.publishErrorMessage(null, "backup liked albums", e.getMessage());
        }
    }
    
    @GetMapping("/restoreLikedAlbums/{deviceId}")
    public void restoreLikedAlbum(@PathVariable("deviceId") String deviceId)
    {
        try
        {
            myMusicService.restoreMyMusic(getExtendedMediaServerByUdn(deviceId));
            toast.publishSuccessMessage(null, "restore liked albums", "success");
        }
        catch (Exception e)
        {
            toast.publishErrorMessage(null, "restore liked albums", e.getMessage());
        }
    }
    
    @GetMapping("/restoreRatings/{deviceId}")
    public void restoreRatings(@PathVariable("deviceId") String deviceId)
    {
        try
        {
            myMusicService.restoreRatings(getExtendedMediaServerByUdn(deviceId));
            toast.publishSuccessMessage(null, "restore audio ratings", "success");
        }
        catch (GenActionException e)
        {
            toast.publishErrorMessage(null, "restore audio ratings", errorHandler.extractErrorText(e.description));
        }
        catch (Exception e)
        {
            toast.publishErrorMessage(null, "restore audio ratings", e.getMessage());
        }
    }
    
    @GetMapping("/backupRatings/{deviceId}")
    public void backupRatings(@PathVariable("deviceId") String deviceId)
    {
        try
        {
            myMusicService.backupRatings(getExtendedMediaServerByUdn(deviceId));
            toast.publishSuccessMessage(null, "backup liked albums", "success");
        }
        catch (Exception e)
        {
            toast.publishErrorMessage(null, "backup liked albums", e.getMessage());
        }
    }
    
}
