package nextcp.upnp.device.mediaserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nextcp.config.ConfigPersistence;
import nextcp.dto.Config;
import nextcp.dto.UmsServerApiKey;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UmsServerDevice extends MediaServerDevice implements ExtendedApiMediaDevice
{
    private static final Logger log = LoggerFactory.getLogger(UmsServerDevice.class.getName());
    private OkHttpClient okClient = new OkHttpClient.Builder().build();

    @Autowired
    private Config config = null;

    @Autowired
    private ConfigPersistence cp = null;

    private static HashMap<String, String> ums_keys = new HashMap<String, String>();

    public UmsServerDevice(RemoteDevice device)
    {
        super(device);
    }

    @PostConstruct
    private void init()
    {
        synchronized (ums_keys)
        {
            if (ums_keys.isEmpty())
            {
                for (UmsServerApiKey ums : config.umsApiKeys)
                {
                    ums_keys.put(ums.serverUuid, ums.serverApiKey);
                }
            }
            if (!ums_keys.containsKey(getUdnAsString()))
            {
                ums_keys.put(getUdnAsString(), "secret");
                config.umsApiKeys.add(new UmsServerApiKey(getUdnAsString(), "secret"));
                cp.writeConfig();
            }
        }
    }

    @Override
    public void rescan()
    {
        try
        {
            String strResponse = executeCall(" ", "api/folderscanner/rescan");
            // response can be analyzed
        }
        catch (Exception e)
        {
            log.debug("likealbum failed ...", e);
        }          
    }

    @Override
    public void rescanFile(File f)
    {
        try
        {
            String strResponse = executeCall(f.getAbsolutePath(), "api/folderscanner/rescanFileOrFolder");
            // response can be analyzed
        }
        catch (Exception e)
        {
            log.debug("likealbum failed ...", e);
        }          
    }

    @Override
    public boolean isAlbumLiked(String musicBrainzReleaseId)
    {
        try
        {
            String strResponse = executeCall(musicBrainzReleaseId, "api/like/isalbumliked");
            return Boolean.valueOf(strResponse);
        }
        catch (Exception e)
        {
            log.debug("isalbumliked failed ...", e);
            return false;
        }        
    }

    @Override
    public void likeAlbum(String musicBrainzReleaseId)
    {
        try
        {
            String strResponse = executeCall(musicBrainzReleaseId, "api/like/likealbum");
            // response can be analyzed
        }
        catch (Exception e)
        {
            log.debug("likealbum failed ...", e);
        }          
    }

    @Override
    public void dislikeAlbum(String musicBrainzReleaseId)
    {
        try
        {
            String strResponse = executeCall(musicBrainzReleaseId, "api/like/dislikealbum");
            // response can be analyzed
        }
        catch (Exception e)
        {
            log.debug("dislikealbum failed ...", e);
        }        
    }

    @Override
    public void likeSong(String musicBrainzTrackId)
    {
        try
        {
            String strResponse = executeCall(musicBrainzTrackId, "api/like/likesong");
            // response can be analyzed
        }
        catch (Exception e)
        {
            log.debug("likesong failed ...", e);
        }        
    }

    @Override
    public void dislikeSong(String musicBrainzTrackId)
    {
        try
        {
            String strResponse = executeCall(musicBrainzTrackId, "api/like/dislikesong");
            // response can be analyzed
        }
        catch (Exception e)
        {
            log.debug("dislikesong failed ...", e);
        }        
    }

    @Override
    public boolean isSongLiked(String musicBrainzTrackId)
    {
        try
        {
            String strResponse = executeCall(musicBrainzTrackId, "api/like/issongliked");
            return Boolean.valueOf(strResponse);
        }
        catch (Exception e)
        {
            log.debug("issongliked failed ...", e);
            return false;
        }
    }

    private String getApiKey()
    {
        String key = ums_keys.get(getUdnAsString());
        return key != null ? key : "";
    }

    @Override
    public void rateSong(String musicBrainzTrackId, int stars)
    {
        try
        {
            String strResponse = executeCall(String.format("%s/%s", musicBrainzTrackId, stars ), "api/rating/setrating");
        }
        catch (Exception e)
        {
            log.debug("rateSong failed ...", e);
        }
    }

    @Override
    public int getSongRating(String musicBrainzTrackId)
    {
        try
        {
            String strResponse = executeCall(musicBrainzTrackId, "api/rating/getrating");
            return Integer.valueOf(strResponse);
        }
        catch (Exception e)
        {
            log.debug("getSongRating failed ...", e);
            return 0;
        }
    }

    private String executeCall(String bodyString, String uri) throws IOException
    {
        RequestBody body = RequestBody.create(bodyString, MediaType.parse("application/text"));
        String requestUrl = String.format("%s%s", getDevice().getDetails().getBaseURL(), uri);
        Request request = new Request.Builder().url(requestUrl).addHeader("api-key", getApiKey()).post(body).build();
        Call call = okClient.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }
}
