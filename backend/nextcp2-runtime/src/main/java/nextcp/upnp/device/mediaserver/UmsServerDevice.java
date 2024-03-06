package nextcp.upnp.device.mediaserver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.support.contentdirectory.DIDLParser;
import org.jupnp.support.model.DIDLContent;
import org.jupnp.support.model.item.Item;
import org.jupnp.support.model.item.PlaylistItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.annotation.PostConstruct;
import nextcp.config.ServerConfig;
import nextcp.dto.Config;
import nextcp.dto.MediaServerDto;
import nextcp.dto.ServerDeviceConfiguration;
import nextcp.dto.ServerPlaylists;
import nextcp.dto.ToastrMessage;
import nextcp.upnp.GenActionException;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.DestroyObjectInput;
import nextcp.util.BackendException;
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
    private final String userAgent = String.format("nextcp/2.0");
    private final String userAgentType = "USER-AGENT";

    @Autowired
    private ServerConfig serverConfig = null;

    @Autowired
    private Config config = null;    
    
    @Autowired
    private ApplicationEventPublisher publisher = null;

    public UmsServerDevice(RemoteDevice device)
    {
        super(device);
    }

    @PostConstruct
    private void init()
    {
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
            if (musicBrainzReleaseId == null)
            {
                return false;
            }
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
        ServerDeviceConfiguration deviceConfig = serverConfig.getMediaServerConfig(getUdnAsString());
        if (deviceConfig == null)
        {
            log.warn("no configuration for server device " + getFriendlyName());
        }
        else
        {
            String key = serverConfig.getMediaServerConfig(getUdnAsString()).apiKey;
            return key != null ? key : "";
        }
        return "";
    }

    @Override
    public void rateSong(Long audiotrackId, String oid, int stars)
    {
        try
        {
            Response response = executeCallWithResponse(String.format("%s/%s/%s", audiotrackId, stars, oid), "api/rating/setratingbyaudiotrackid");
            String body = response.body().string();
            int code = response.code();
            toastDeviceResponse(body, code, true);
        }
        catch (Exception e)
        {
            log.error("rateSong failed ...", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "UMS server device" + getFriendlyName(), "File rating failed : " + e.getMessage()));
        }
    }

    @Override
    public void rateSongByMusicBrainzID(String musicbrainzId, int stars)
    {
        try
        {
            Response response = executeCallWithResponse(String.format("%s/%s", musicbrainzId, stars), "api/rating/setrating");
            String body = response.body().string();
            int code = response.code();
            toastDeviceResponse(body, code, true);
        }
        catch (Exception e)
        {
            log.debug("rateSong failed ...", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "UMS server device" + getFriendlyName(), "File rating failed : " + e.getMessage()));
        }
    }

    private void toastDeviceResponse(String body, int code, boolean successToast)
    {
        switch (code)
        {
            case 200:
                if (successToast)
                {
                    publisher.publishEvent(new ToastrMessage(null, "info", "UMS server device " + getFriendlyName(), body));
                }
                break;
            case 401:
                publisher.publishEvent(new ToastrMessage(null, "error", "nextcp/2 configuration error",
                        "Wrong API key configured for device " + getFriendlyName() + ". Set correct secret for server : " + getUdnAsString()));
                break;
            case 404:
                publisher.publishEvent(new ToastrMessage(null, "warn", "UMS server device " + getFriendlyName(), "Object not found. " + body));
                break;
            case 503:
                publisher.publishEvent(new ToastrMessage(null, "error", "UMS server device " + getFriendlyName(), body));
                break;
            default:
                publisher.publishEvent(new ToastrMessage(null, "warn", "UMS server device '" + getFriendlyName() + "'", body));
        }
    }

    @Override
    public int getSongRating(Integer audiotrackId)
    {
        try
        {
            String strResponse = executeCall(Integer.toString(audiotrackId), "api/rating/getratingbyaudiotrackid");
            return Integer.valueOf(strResponse);
        }
        catch (Exception e)
        {
            log.debug("getSongRating by audiotrackid failed ...", e);
            return 0;
        }
    }

    @Override
    public int getSongRatingByMusicBrainzID(String musicBrainzTrackId)
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

    /**
     * Execute a UMS call. Will send Toast error message to client in case of an error.
     * 
     * @param bodyString
     * @param uri
     * @return
     * @throws IOException
     */
    private String executeCall(String bodyString, String uri) throws IOException
    {
        RequestBody body = RequestBody.create(bodyString, MediaType.parse("application/text"));
        String requestUrl = String.format("%s%s", getBaseUrl(), uri);
        Request request = new Request.Builder().url(requestUrl).addHeader("api-key", getApiKey()).addHeader(userAgentType, userAgent).post(body).build();
        Call call = okClient.newCall(request);
        Response response = call.execute();
        String respString = response.body().string();
        toastDeviceResponse(respString, response.code(), false);
        return respString;
    }

    private URL getBaseUrl()
    {
        if (getDevice().getDetails().getBaseURL() != null)
        {
            return getDevice().getDetails().getBaseURL();
        }
        else
        {
            try
            {
                return new URL(String.format("%s://%s:%d/", getDevice().getIdentity().getDescriptorURL().getProtocol(), getDevice().getIdentity().getDescriptorURL().getHost(),
                        getDevice().getIdentity().getDescriptorURL().getPort()));
            }
            catch (MalformedURLException e)
            {
                log.error("cannot acquire base url ", e);
                return null;
            }
        }
    }

    private Response executeCallWithResponse(String bodyString, String uri) throws IOException
    {
        RequestBody body = RequestBody.create(bodyString, MediaType.parse("application/text"));
        String requestUrl = String.format("%s%s", getBaseUrl(), uri);
        Request request = new Request.Builder().url(requestUrl).addHeader("api-key", getApiKey()).addHeader(userAgentType, userAgent).post(body).build();
        Call call = okClient.newCall(request);
        Response response = call.execute();
        return response;
    }

    private String doGenericCall(String body, String api, boolean showOkMessage)
    {
        String respBody = "";
        Response res = null;
        try
        {
            res = executeCallWithResponse(body, api);
            respBody = res.body().string();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Execution failed.", e);
        }

        if (res.code() != 200)
        {
            toastDeviceResponse(respBody, res.code(), false);
            log.warn("API error : " + res.code() + " : " + respBody);
        }
        if (showOkMessage)
        {
            toastDeviceResponse(respBody, res.code(), true);
        }
        return respBody;
    }

    @Override
    public void backupMyMusic()
    {
        doGenericCall("", "api/like/backupLikedAlbums", true);
    }

    @Override
    public void restoreMyMusic()
    {
        doGenericCall("", "api/like/restoreLikedAlbums", true);
        publisher.publishEvent(new ToastrMessage(null, "info", "UMS server " + getFriendlyName(), "My Music albums restored."));
    }

    @Override
    public Item createPlaylist(String parentContainerId, String playlistName) throws Exception
    {
    	CreateObjectInput inp = new CreateObjectInput();
    	inp.ContainerID = parentContainerId;
    	PlaylistItem pi = new PlaylistItem();
    	pi.setTitle(playlistName);
    	pi.setParentID(parentContainerId);
    	pi.setId("");
    	DIDLParser parser = new DIDLParser();
    	DIDLContent content = new DIDLContent();
    	content.addItem(pi);
    	String xml = parser.generate(content);
		inp.Elements = xml;
		
		try {
			CreateObjectOutput out = getContentDirectoryService().createObject(inp);
			log.debug("created object {} ", out.Result);
			content = parser.parse(out.Result);
			org.jupnp.support.model.item.Item newPL = content.getItems().get(0);
			return newPL;
			
		} catch (GenActionException e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.description);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
		}
    }

    @Override
    public void addSongToPlaylist(String audiotracId, String playlistContainerId)
    {
    	CreateReferenceInput inp = new CreateReferenceInput();
		inp.ContainerID = playlistContainerId;
		inp.ObjectID = audiotracId;
		try {
			CreateReferenceOutput out = getContentDirectoryService().createReference(inp);
			log.debug("created object {} ", out.NewID);			
		} catch (GenActionException e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.description);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
		}
    }


    public MediaServerDto getAsDto()
    {
        return new MediaServerDto(getUDN().getIdentifierString(), getFriendlyName(), true);
    }

    @Override
    public ServerPlaylists getServerPlaylists() throws JsonMappingException, JsonProcessingException
    {
    	ServerPlaylists spl = searchMyPlaylistsItems(config.applicationConfig.myPlaylistFolderName);
        return spl;
    }

	@Override
	public void deleteObject(String objectId) {
    	DestroyObjectInput inp = new DestroyObjectInput();
		inp.ObjectID = objectId;
		try {
			getContentDirectoryService().destroyReference(inp);
		} catch (GenActionException e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.description);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage());
		}
	}
}
