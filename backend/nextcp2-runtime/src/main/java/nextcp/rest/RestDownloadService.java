package nextcp.rest;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.dto.MusicItemDto;
import nextcp.dto.ToastrMessage;
import nextcp.indexer.IndexerException;
import nextcp.indexer.service.FilesystemIndexerService;
import nextcp.rating.domain.SongIndexed;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/DownloadService")
public class RestDownloadService
{
    @Autowired
    private FilesystemIndexerService filesystemPlaylistService = null;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    @GetMapping("/downloadFileByMBID/{mbid}")
    public ResponseEntity<Resource> downloadFileByMBID(@PathVariable("mbid") String mbid)
    {
        try
        {
            SongIndexed song = filesystemPlaylistService.getBestSong(mbid);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(song.getFilePath()));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", song.getFilename()));
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            File f = new File(song.getFilePath());
            
            return ResponseEntity.ok().headers(headers).contentLength(f.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        }
        catch (IndexerException e)
        {
            publisher.publishEvent(new ToastrMessage(null, "error", "File download", e.description));
        }
        catch (Exception e)
        {
            publisher.publishEvent(new ToastrMessage(null, "error", "File download", e.getMessage()));
        }
        return null;
    }
}
