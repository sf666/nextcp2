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
import nextcp.dto.MusicAlbumIds;
import nextcp.service.ToastEventPublisher;
import nextcp.util.FailureReason;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;

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
	
	
	
    public RestMyMusicService()
    {
    }

    @PostMapping("/likeAlbum/{deviceId}")
    public void likeAlbum(@RequestBody MusicAlbumIds albumIds, @PathVariable("deviceId") String deviceId)
    {
        myMusicService.likeAlbum(albumIds, getExtendedMediaServerByUdn(deviceId));
    }

    @PostMapping("/deleteAlbumLike/{deviceId}")
    public void deleteAlbumLike(@RequestBody MusicAlbumIds albumIds, @PathVariable("deviceId") String deviceId)
    {
        myMusicService.dislikeAlbum(albumIds, getExtendedMediaServerByUdn(deviceId));
    }

    @PostMapping("/isAlbumLiked/{deviceId}")
    public boolean isAlbumLiked(@RequestBody MusicAlbumIds albumIds, @PathVariable("deviceId") String deviceId)
    {
        // Read-only status check: degrade gracefully to "not liked" when the server is not
        // (currently) available or does not support the extended API, instead of returning an
        // HTTP error that surfaces in the UI on every album browse.
        ExtendedApiMediaDevice device = findExtendedMediaServerByUdn(deviceId);
        if (device == null)
        {
            return false;
        }
        return myMusicService.isAlbumLiked(albumIds, device);
    }

    @GetMapping("/restoreRatings/{deviceId}")
    public void restoreRatings(@PathVariable("deviceId") String deviceId)
    {
        try
        {
            myMusicService.restoreRatings(getExtendedMediaServerByUdn(deviceId));
            toast.publishSuccessMessage(null, "restore audio ratings", "success");
        }
        catch (Exception e)
        {
            toast.publishErrorMessage(null, "restore audio ratings", FailureReason.of(e));
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
        // One catch, because FailureReason already knows the difference: it prefers a device's own
        // words over anything nextcp wrapped around them, and falls back to getMessage() - which is
        // empty for a UPnP action failure, so the toast used to appear without any text at all.
        catch (Exception e)
        {
            toast.publishErrorMessage(null, "backup liked albums", FailureReason.of(e));
        }
    }
    
}
