package nextcp.lastfm.request;

import java.io.IOException;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextcp.lastfm.LastFmException;
import nextcp.lastfm.dto.ResponseError;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class LastFmRequestService
{

    private OkHttpClient okClient = new OkHttpClient.Builder().build();

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private Signer signer = null;

    public LastFmRequestService()
    {
    }

    /**
     * This method uses the parameters set to construct the request url for convinience.
     * 
     * @param <T>   return type
     * @param params    the parameter
     * @param cls   return type class   
     * @param signCall should request be signed
     * @return unmarshaled class <T>
     */
    public <T> T doApiRequest(TreeSet<Parameter> params, Class<T> cls, boolean signCall)
    {
        StringBuilder sb = constructParameterUri(params);
        if (signCall)
        {
            String sig = signer.signCall(params);
            sb.append("&api_sig=").append(sig);
        }
        return doApiRequest(sb.toString(), cls);
    }

    private StringBuilder constructParameterUri(TreeSet<Parameter> params)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("http://ws.audioscrobbler.com/2.0/?");

        for (Parameter parameter : params)
        {
            sb.append(parameter.name).append("=").append(parameter.value).append("&");
        }
        sb.append("format=json");
        return sb;
    }

    /**
     * This method can be used to call self generated requestUrl's.
     * @param <T>
     * @param requestUrl
     * @param cls
     * @return
     */
    public <T> T doApiRequest(String requestUrl, Class<T> cls)
    {
        Request request = new Request.Builder().url(requestUrl).get().build();
        Call call = okClient.newCall(request);
        try
        {
            Response response = call.execute();
            if (response.isSuccessful())
            {
                String body = response.body().string();
                return om.readValue(body, cls);
            }
            else
            {
                ResponseError err = om.readValue(response.body().string(), ResponseError.class);
                throw new LastFmException(err.getError(), response);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("LastFM request error", e);
        }
    }
}
