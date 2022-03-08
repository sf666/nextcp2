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

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MyMusicService")
public class RestMyMusicService extends BaseRestService
{
    @Autowired
    @Lazy
    private MyMusicService myMusicService = null;

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
    public GenericResult backupLikedAlbum(@PathVariable("deviceId") String deviceId)
    {
        GenericResult gr = new GenericResult();
        gr.success = true;
        gr.headerMessage = "backup liked albums";
        try
        {
            myMusicService.backupMyMusic(getExtendedMediaServerByUdn(deviceId));
        }
        catch (Exception e)
        {
            gr.success = false;
            gr.message = e.getMessage();
        }
        gr.message = "success";
        return gr;
    }

    @GetMapping("/restoreLikedAlbums/{deviceId}")
    public GenericResult restoreLikedAlbum(@PathVariable("deviceId") String deviceId)
    {
        GenericResult gr = new GenericResult();
        gr.success = true;
        gr.headerMessage = "restore liked albums";
        try
        {
            myMusicService.restoreMyMusic(getExtendedMediaServerByUdn(deviceId));
            gr.message = "success";
        }
        catch (Exception e)
        {
            gr.success = false;
            gr.message = e.getMessage();
        }
        return gr;
    }
}
