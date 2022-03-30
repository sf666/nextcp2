package nextcp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.dto.ToastrMessage;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/DownloadService")
public class RestDownloadService
{

    @Autowired
    private ApplicationEventPublisher publisher = null;

    @GetMapping("/downloadFileByMBID/{mbid}")
    public ResponseEntity<Resource> downloadFileByMBID(@PathVariable("mbid") String mbid)
    {
        try
        {
            return null;
        }
        catch (Exception e)
        {
            publisher.publishEvent(new ToastrMessage(null, "error", "File download", e.getMessage()));
        }
        return null;
    }
}
