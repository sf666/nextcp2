package nextcp.upnp.device.mediaserver;

import java.io.File;
import java.io.IOException;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public UmsServerDevice(RemoteDevice device)
    {
        super(device);
    }

    @Override
    public void rescan()
    {
        String fileChanged = "C:\\test/Folder with space /test.mp3";
        RequestBody body = RequestBody.create(fileChanged, MediaType.parse("application/text"));
        String requestUrl = String.format("%sapi/rescan", getDevice().getDetails().getBaseURL());
        Request request = new Request.Builder().url(requestUrl).post(body).build();
        Call call = okClient.newCall(request);
        try
        {
            Response response = call.execute();
        }
        catch (IOException e)
        {
            log.warn("unable to retrieve rating data from musicbrainz.org", e);
        }

        //        String requestUrl = String.format("%sconsole/scan", getDevice().getDetails().getBaseURL());
//        Request request = new Request.Builder().url(requestUrl).get().build();
//        Call call = okClient.newCall(request);
//        try
//        {
//            Response response = call.execute();
//        }
//        catch (IOException e)
//        {
//            log.warn("unable to retrieve rating data from musicbrainz.org", e);
//        }
    }
    
    @Override
    public void rescanFile(File f)
    {
        String fileChanged = "test";
        RequestBody body = RequestBody.create(fileChanged, MediaType.parse("application/text"));
        String requestUrl = String.format("%sapi/rescan", getDevice().getDetails().getBaseURL());
        Request request = new Request.Builder().url(requestUrl).post(body).build();
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
