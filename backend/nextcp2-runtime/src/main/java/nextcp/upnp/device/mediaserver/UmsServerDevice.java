package nextcp.upnp.device.mediaserver;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.support.contentdirectory.DIDLParser;
import org.jupnp.support.model.DIDLContent;
import org.jupnp.support.model.container.Container;
import org.jupnp.support.model.item.Item;
import org.jupnp.support.model.item.PlaylistItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.annotation.PostConstruct;
import nextcp.config.ServerConfig;
import nextcp.dto.Config;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicItemIdDto;
import nextcp.dto.ServerDeviceConfiguration;
import nextcp.dto.ServerPlaylists;
import nextcp.dto.ToastrMessage;
import nextcp.dto.UpdateStarRatingRequest;
import nextcp.service.ToastEventPublisher;
import nextcp.upnp.GenActionException;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.DestroyObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.UpdateObjectInput;
import nextcp.util.BackendException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UmsServerDevice extends MediaServerDevice implements ExtendedApiMediaDevice {

	private static final Logger log = LoggerFactory.getLogger(UmsServerDevice.class.getName());
	private OkHttpClient okClient = new OkHttpClient.Builder().build();
	private final String userAgent = String.format("nextcp/2.0");
	private final String userAgentType = "USER-AGENT";
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder builder = null;
	private XPathFactory xpathfactory = XPathFactory.newInstance();
	private XPath xpath = xpathfactory.newXPath();
	private XPathExpression expr = null;
	@Autowired
	private ServerConfig serverConfig = null;

	@Autowired
	private Config config = null;

	@Autowired
	private ApplicationEventPublisher publisher = null;

	@Autowired
	private ToastEventPublisher toast = null;
	
	

	public UmsServerDevice(RemoteDevice device) {
		super(device);
		factory.setNamespaceAware(false);
		try {
			builder = factory.newDocumentBuilder();
			expr = xpath.compile("//*/errorDescription/text()");
		} catch (ParserConfigurationException | XPathExpressionException e) {
			log.error("cannot build XML reader", e);
		}
	}

	@PostConstruct
	private void init() {
	}

	@Override
	public void rescan() {
		try {
			String strResponse = executeCall(" ", "api/folderscanner/rescan");
			// response can be analyzed
		} catch (Exception e) {
			log.debug("likealbum failed ...", e);
		}
	}

	@Override
	public void rescanFile(File f) {
		try {
			String strResponse = executeCall(f.getAbsolutePath(), "api/folderscanner/rescanFileOrFolder");
			// response can be analyzed
		} catch (Exception e) {
			log.debug("likealbum failed ...", e);
		}
	}

	@Override
	public boolean isAlbumLiked(String musicBrainzReleaseId) {
		try {
			if (musicBrainzReleaseId == null) {
				return false;
			}
			String strResponse = executeCall(musicBrainzReleaseId, "api/like/isalbumliked");
			return Boolean.valueOf(strResponse);
		} catch (Exception e) {
			log.debug("isalbumliked failed ...", e);
			return false;
		}
	}

	@Override
	public void likeAlbum(String musicBrainzReleaseId) {
		try {
			String strResponse = executeCall(musicBrainzReleaseId, "api/like/likealbum");
			// response can be analyzed
		} catch (Exception e) {
			log.debug("likealbum failed ...", e);
		}
	}

	@Override
	public void dislikeAlbum(String musicBrainzReleaseId) {
		try {
			String strResponse = executeCall(musicBrainzReleaseId, "api/like/dislikealbum");
			// response can be analyzed
		} catch (Exception e) {
			log.debug("dislikealbum failed ...", e);
		}
	}

	@Override
	public void likeSong(String musicBrainzTrackId) {
		try {
			String strResponse = executeCall(musicBrainzTrackId, "api/like/likesong");
			// response can be analyzed
		} catch (Exception e) {
			log.debug("likesong failed ...", e);
		}
	}

	@Override
	public void dislikeSong(String musicBrainzTrackId) {
		try {
			String strResponse = executeCall(musicBrainzTrackId, "api/like/dislikesong");
			// response can be analyzed
		} catch (Exception e) {
			log.debug("dislikesong failed ...", e);
		}
	}

	@Override
	public boolean isSongLiked(String musicBrainzTrackId) {
		try {
			String strResponse = executeCall(musicBrainzTrackId, "api/like/issongliked");
			return Boolean.valueOf(strResponse);
		} catch (Exception e) {
			log.debug("issongliked failed ...", e);
			return false;
		}
	}

	private String getApiKey() {
		ServerDeviceConfiguration deviceConfig = serverConfig.getMediaServerConfig(getUdnAsString());
		if (deviceConfig == null) {
			log.warn("no configuration for server device " + getFriendlyName());
		} else {
			String key = serverConfig.getMediaServerConfig(getUdnAsString()).apiKey;
			return key != null ? key : "";
		}
		return "";
	}

	@Override
	public void rateSong(UpdateStarRatingRequest updateRequest) {
		UpdateObjectInput inp = new UpdateObjectInput();
		inp.ObjectID = updateRequest.musicItemIdDto.objectID;
		inp.CurrentTagValue = updateRequest.previousRating != null ? 
			String.format("<upnp:rating>%d</upnp:rating>",updateRequest.previousRating) : null;
		inp.NewTagValue = updateRequest.newRating != null ? 
			String.format("<upnp:rating>%d</upnp:rating>",updateRequest.newRating) : null;
		try {
			getContentDirectoryService().updateObject(inp);
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	private void toastDeviceResponse(String body, int code, boolean successToast) {
		switch (code) {
			case 200:
				if (successToast) {
					publisher.publishEvent(new ToastrMessage(null, "info", "UMS server device " + getFriendlyName(), body));
				}
				break;
			case 401:
				publisher.publishEvent(new ToastrMessage(null, "error", "nextcp/2 configuration error",
					"Wrong API key configured for device " + getFriendlyName() + ". Set correct secret for server : " + getUdnAsString()));
				break;
			case 404:
				publisher
					.publishEvent(new ToastrMessage(null, "warn", "UMS server device " + getFriendlyName(), "Object not found. " + body));
				break;
			case 503:
				publisher.publishEvent(new ToastrMessage(null, "error", "UMS server device " + getFriendlyName(), body));
				break;
			default:
				publisher.publishEvent(new ToastrMessage(null, "warn", "UMS server device '" + getFriendlyName() + "'", body));
		}
	}

	/**
	 * Execute a UMS call. Will send Toast error message to client in case of an
	 * error.
	 * 
	 * @param bodyString
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	private String executeCall(String bodyString, String uri) throws IOException {
		RequestBody body = RequestBody.create(bodyString, MediaType.parse("application/text"));
		String requestUrl = String.format("%s%s", getBaseUrl(), uri);
		Request request = new Request.Builder().url(requestUrl).addHeader("api-key", getApiKey()).addHeader(userAgentType, userAgent)
			.post(body).build();
		Call call = okClient.newCall(request);
		Response response = call.execute();
		String respString = response.body().string();
		toastDeviceResponse(respString, response.code(), false);
		return respString;
	}

	private URL getBaseUrl() {
		if (getDevice().getDetails().getBaseURL() != null) {
			return getDevice().getDetails().getBaseURL();
		} else {
			try {
				return new URL(String.format("%s://%s:%d/", getDevice().getIdentity().getDescriptorURL().getProtocol(),
					getDevice().getIdentity().getDescriptorURL().getHost(), getDevice().getIdentity().getDescriptorURL().getPort()));
			} catch (MalformedURLException e) {
				log.error("cannot acquire base url ", e);
				return null;
			}
		}
	}

	private Response executeCallWithResponse(String bodyString, String uri) throws IOException {
		RequestBody body = RequestBody.create(bodyString, MediaType.parse("application/text"));
		String requestUrl = String.format("%s%s", getBaseUrl(), uri);
		Request request = new Request.Builder().url(requestUrl).addHeader("api-key", getApiKey()).addHeader(userAgentType, userAgent)
			.post(body).build();
		Call call = okClient.newCall(request);
		Response response = call.execute();
		return response;
	}

	private String doGenericCall(String body, String api, boolean showOkMessage) {
		String respBody = "";
		Response res = null;
		try {
			res = executeCallWithResponse(body, api);
			respBody = res.body().string();
		} catch (IOException e) {
			throw new RuntimeException("Execution failed.", e);
		}

		if (res.code() != 200) {
			toastDeviceResponse(respBody, res.code(), false);
			log.warn("API error : " + res.code() + " : " + respBody);
		}
		if (showOkMessage) {
			toastDeviceResponse(respBody, res.code(), true);
		}
		return respBody;
	}

	@Override
	public void backupMyMusic() {
		doGenericCall("", "api/like/backupLikedAlbums", true);
	}

	@Override
	public void restoreMyMusic() {
		doGenericCall("", "api/like/restoreLikedAlbums", true);
		publisher.publishEvent(new ToastrMessage(null, "info", "UMS server " + getFriendlyName(), "My Music albums restored."));
	}

	@Override
	public Container createPlaylist(String parentContainerId, String playlistName) throws Exception {
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
			Container newPL = content.getFirstContainer();
			return newPL;
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public void addSongToPlaylist(String audiotracId, String playlistContainerId) {
		CreateReferenceInput inp = new CreateReferenceInput();
		inp.ContainerID = playlistContainerId;
		inp.ObjectID = audiotracId;
		try {
			CreateReferenceOutput out = getContentDirectoryService().createReference(inp);
			log.debug("created object {} ", out.NewID);
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	private String extractErrorText(String description) {
		InputSource is = new InputSource(new StringReader(description));
		try {
			Document doc = builder.parse(is);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			for (int i = 0; i < nodes.getLength();) {
			  return nodes.item(i).getNodeValue();
			}			
		} catch (SAXException | IOException | XPathExpressionException e) {
			log.warn("cannot extract error message", e);
		}
		return "";
	}

	public MediaServerDto getAsDto() {
		return new MediaServerDto(getUDN().getIdentifierString(), getFriendlyName(), true);
	}

	@Override
	public ServerPlaylists getServerPlaylists() throws JsonMappingException, JsonProcessingException {
		ServerPlaylists spl = searchMyPlaylistsItems(config.applicationConfig.myPlaylistFolderName);
		return spl;
	}

	@Override
	public void deleteObject(String objectId) {
		DestroyObjectInput inp = new DestroyObjectInput();
		inp.ObjectID = objectId;
		try {
			getContentDirectoryService().destroyObject(inp);
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}
}
