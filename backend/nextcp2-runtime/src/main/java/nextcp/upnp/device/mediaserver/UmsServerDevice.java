package nextcp.upnp.device.mediaserver;

import java.io.File;
import java.io.IOException;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nextcp.dto.Config;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UmsServerDevice extends MediaServerDevice
{
    private static final Logger log = LoggerFactory.getLogger(UmsServerDevice.class.getName());
    private OkHttpClient okClient = new OkHttpClient.Builder().build();

    @Autowired
    private Config config = null;
    
    
    public UmsServerDevice(RemoteDevice device)
    {
        super(device);
    }

    @Override
    public void rescan()
    {
        String requestUrl = String.format("%sapi/rescan", getDevice().getDetails().getBaseURL());
        Request request = new Request.Builder().url(requestUrl).get().build();
        Call call = okClient.newCall(request);
        try
        {
            Response response = call.execute();
            // read status status code
        }
        catch (IOException e)
        {
            log.warn("unable to retrieve rating data from musicbrainz.org", e);
        }
    }
    
    @Override
    public void rescanFile(File f)
    {
        RequestBody body = RequestBody.create(config.localIndexerSupport.musicRootPath, MediaType.parse("application/text"));
        String requestUrl = String.format("%sapi/rescanFileOrFolder", getDevice().getDetails().getBaseURL());
        Request request = new Request.Builder().url(requestUrl).addHeader("api-key","123456789012").post(body).build();
        Call call = okClient.newCall(request);
        try
        {
            Response response = call.execute();
        }
        catch (IOException e)
        {
            log.warn("unable to retrieve rating data from musicbrainz.org", e);
        }
    }

}
