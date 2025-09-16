package nextcp.upnp.device.mediaserver;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.StringUtils;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.support.contentdirectory.DIDLParser;
import org.jupnp.support.model.DIDLContent;
import org.jupnp.support.model.DIDLObject.Class;
import org.jupnp.support.model.Res;
import org.jupnp.support.model.container.Container;
import org.jupnp.support.model.container.StorageFolder;
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
import jakarta.annotation.PostConstruct;
import nextcp.config.ServerConfig;
import nextcp.dto.Config;
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicItemDto;
import nextcp.dto.ServerDeviceConfiguration;
import nextcp.dto.UpdateAlbumArtUriRequest;
import nextcp.dto.UpdateStarRatingRequest;
import nextcp.service.ToastEventPublisher;
import nextcp.upnp.GenActionException;
import nextcp.upnp.device.mediaserver.ums.UmsExtendedServicesServiceEventListener;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.DestroyObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.UpdateObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.UmsExtendedServicesService;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.UmsExtendedServicesServiceEventListenerImpl;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbumInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbumInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStoreFolderInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAnonymousDevicesWriteInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictEurope;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictEuropeInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictPassInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictUser;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictUserInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioUpdateRatingTagInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetUpnpCdsWriteInput;
import nextcp.util.BackendException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UmsServerDevice extends MediaServerDevice implements ExtendedApiMediaDevice {

	private static final Logger log = LoggerFactory.getLogger(UmsServerDevice.class.getName());
	private OkHttpClient okClient = new OkHttpClient.Builder().build();
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder builder = null;
	private XPathFactory xpathfactory = XPathFactory.newInstance();
	private XPath xpath = xpathfactory.newXPath();
	private XPathExpression expr = null;

	private UmsExtendedServicesService umsServices = null;
	private UmsExtendedServicesServiceEventListenerImpl umsServiceEventListener = null;

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
		try {
			umsServices = new UmsExtendedServicesService(getUpnpService(), getDevice());
			if (umsServices.getUmsExtendedServicesService() != null) {
				umsServiceEventListener = new UmsExtendedServicesServiceEventListener(getDevice(), this);
				umsServices.addSubscriptionEventListener(umsServiceEventListener);
			} else {
				log.warn(
					"This UMS version has no UPnP extended UMS services. Please use the fork from https://github.com/ik666/UniversalMediaServer or you can try a current version of UMS.");
			}
		} catch (Exception e) {
			log.info("This UMS version has no UPnP extended UMS services.");
		}
	}

	private void configureServer() {
		log.info("anonwrite : {} ", umsServiceEventListener.getStateVariable().AnonymousDevicesWrite);
		log.info("like in root  : {} ", umsServiceEventListener.getStateVariable().AudioLikesVisibleRoot);
		log.info("update rating : {} ", umsServiceEventListener.getStateVariable().AudioUpdateRating);
		log.info("upnp cds write : {} ", umsServiceEventListener.getStateVariable().UpnpCdsWrite);

		if (umsServiceEventListener.getStateVariable().UpnpCdsWrite != null && !umsServiceEventListener.getStateVariable().UpnpCdsWrite) {
			log.info("UMS server -> activating UPnP create object ability ...");
			SetUpnpCdsWriteInput inp = new SetUpnpCdsWriteInput();
			inp.UpnpCdsWrite = Boolean.TRUE;
			umsServices.setUpnpCdsWrite(inp);
		}

		if (umsServiceEventListener.getStateVariable().AnonymousDevicesWrite != null &&
			!umsServiceEventListener.getStateVariable().AnonymousDevicesWrite) {
			log.info("UMS server -> activating UPnP create object ability for all UPnP devices ...");
			SetAnonymousDevicesWriteInput inp = new SetAnonymousDevicesWriteInput();
			inp.AnonymousDevicesWrite = Boolean.TRUE;
			umsServices.setAnonymousDevicesWrite(inp);
		}

		if (!StringUtils.isAllBlank(config.audioAddictConfig.user) && !StringUtils.isAllBlank(config.audioAddictConfig.pass)) {
			log.debug("[AudioAddict Config] updating UMS.conf with user & password ...");
			try {
				SetAudioAddictEuropeInput inp = new SetAudioAddictEuropeInput();
				inp.AudioAddictEurope = config.audioAddictConfig.preferEuropeanServer; 
				umsServices.setAudioAddictEurope(inp);
				
				SetAudioAddictUserInput inp_user = new SetAudioAddictUserInput();
				inp_user.AudioAddictUser = config.audioAddictConfig.user; 
				umsServices.setAudioAddictUser(inp_user);

				SetAudioAddictPassInput inp_pass = new SetAudioAddictPassInput();
				inp_pass.AudioAddictPass = new String(Base64.getDecoder().decode(config.audioAddictConfig.pass));
				umsServices.setAudioAddictPass(inp_pass);
			} catch (Exception e) {
				log.warn("couldn't update AudioAddict Network.", e);
			}			
		} else {
			log.debug("[AudioAddict Config] disabled. Please set username & password to activate AudioAddict network support.");
		}

		ServerDeviceConfiguration sd = getNewServerConfig();
		serverConfig.updateServerDevice(sd);
	}

	@Override
	public void rescan() {
		umsServices.rescanMediaStore();
	}

	@Override
	public void rescanFile(String objectId) {
		RescanMediaStoreFolderInput inp = new RescanMediaStoreFolderInput();
		inp.ObjectID = objectId;
		umsServices.rescanMediaStoreFolder(inp);
	}

	@Override
	public boolean isAlbumLiked(String musicBrainzReleaseId) {
		IsAlbumLikedInput inp = new IsAlbumLikedInput();
		inp.MusicBraizReleaseID = musicBrainzReleaseId;
		IsAlbumLikedOutput out = umsServices.isAlbumLiked(inp);
		if (out.AlbumLikedValue != null) {
			return out.AlbumLikedValue;
		}
		return false;
	}

	@Override
	public void likeAlbum(String musicBrainzReleaseId) {
		LikeAlbumInput inp = new LikeAlbumInput();
		inp.MusicBraizReleaseID = musicBrainzReleaseId;
		umsServices.likeAlbum(inp);
	}

	@Override
	public void dislikeAlbum(String musicBrainzReleaseId) {
		DislikeAlbumInput inp = new DislikeAlbumInput();
		inp.MusicBraizReleaseID = musicBrainzReleaseId;
		umsServices.dislikeAlbum(inp);
	}

	@Override
	public void likeSong(String musicBrainzTrackId) {
		// NOT IMPLEMENETD
	}

	@Override
	public void dislikeSong(String musicBrainzTrackId) {
		// NOT IMPLEMENETD
	}

	@Override
	public boolean isSongLiked(String musicBrainzTrackId) {
		// NOT IMPLEMENETD
		return false;
	}

	@Override
	public void updateAlbumArtURI(UpdateAlbumArtUriRequest updateRequest) {
		UpdateObjectInput inp = new UpdateObjectInput();
		inp.ObjectID = updateRequest.musicItemIdDto.objectID;
		inp.CurrentTagValue = updateRequest.previousAlbumArtUri != null ?
			String.format("<upnp:albumArtURI>%s</upnp:albumArtURI>", updateRequest.previousAlbumArtUri) :
			null;
		inp.NewTagValue = updateRequest.previousAlbumArtUri != null ?
			String.format("<upnp:albumArtURI>%s</upnp:albumArtURI>", updateRequest.newAlbumArtUri) :
			null;
		try {
			getContentDirectoryService().updateObject(inp);
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public void rateSong(UpdateStarRatingRequest updateRequest) {
		UpdateObjectInput inp = new UpdateObjectInput();
		inp.ObjectID = updateRequest.musicItemIdDto.objectID;
		inp.CurrentTagValue = updateRequest.previousRating != null ?
			String.format("<upnp:rating>%d</upnp:rating>", updateRequest.previousRating) :
			null;
		inp.NewTagValue = updateRequest.newRating != null ? String.format("<upnp:rating>%d</upnp:rating>", updateRequest.newRating) : null;
		try {
			getContentDirectoryService().updateObject(inp);
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public void backupMyMusic() {
		umsServices.backupAudioLikes();
	}

	@Override
	public void restoreMyMusic() {
		umsServices.restoreAudioLikes();
	}

	private String browseChildrenSearchFolder(long start, long end, String objectId, String foldername) {
		log.debug("search folder having id {} and title {} ...", objectId, foldername);
		BrowseInput inp = new BrowseInput();
		inp.ObjectID = objectId;
		inp.SortCriteria = "";
		inp.StartingIndex = start;
		inp.RequestedCount = end;
		inp.Filter = "*";
		ContainerItemDto resultContainer = browseChildren(inp);
		log.info("Reported total matches : {} ", resultContainer.totalMatches);
		log.info("result container size : {} ", resultContainer.containerDto.size());
		for (ContainerDto folder : resultContainer.containerDto) {
			log.debug("Found folder named : {}", folder.title);
			if (folder.title.equalsIgnoreCase(foldername)) {
				log.debug("Found folder id : {}", folder.id);
				return folder.id;
			}
		}
		if (resultContainer.totalMatches != null && resultContainer.totalMatches > end) {
			long diff = end - start;
			log.debug("extending search to items from {} to {}", end + 1, end + diff + 1);
			return browseChildrenSearchFolder(end + 1, end + diff + 1, objectId, foldername);
		} else {
			log.warn("CDS didn't fill totalMatches attribute.");
		}
		log.debug("folder not found : {}", foldername);
		return null;
	}

	@Override
	public String getOrCreateChildFolderId(String parentContainerId, String folderName) throws Exception {
		log.debug("getting or creating folder with name {} in parentfolder having id {} ...", folderName, parentContainerId);
		String childId = this.browseChildrenSearchFolder(0, 200, parentContainerId, folderName);
		if (childId == null) {
			Container c = createFolder(parentContainerId, folderName);
			if (c != null) {
				return c.getId();
			}
			throw new RuntimeException("cannot create directory");
		} else {
			log.info("folder already exists with id : " + childId);
			return childId;
		}
	}

	@Override
	public Container createFolder(String parentContainerId, String folderName) throws Exception {
		log.debug("creating folder parentId : {} / folder name : {} ... ", parentContainerId, folderName);
		CreateObjectInput inp = new CreateObjectInput();
		inp.ContainerID = parentContainerId;
		StorageFolder storageFolder = new StorageFolder();
		storageFolder.setTitle(folderName);
		storageFolder.setParentID(parentContainerId);
		storageFolder.setId("");
		DIDLParser parser = new DIDLParser();
		DIDLContent content = new DIDLContent();
		content.addContainer(storageFolder);
		String xml = parser.generate(content);
		inp.Elements = xml;

		try {
			CreateObjectOutput out = getContentDirectoryService().createObject(inp);
			log.debug("created object {} ", out.Result);
			content = parser.parse(out.Result);
			Container newPL = content.getFirstContainer();
			log.info("new folder created with object id : " + newPL.getId());
			return newPL;
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public Container createPlaylist(String parentContainerId, String playlistName) throws Exception {
		log.info("adding playlist with name {} to folder with id {} ...", parentContainerId, playlistName);
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
			log.debug("playlist created with object id {} ", out.Result);
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
	public String addSongToPlaylist(String songObjectId, String playlistContainerId) {
		log.info("adding song {} to playlist {} ...", songObjectId, playlistContainerId);
		CreateReferenceInput inp = new CreateReferenceInput();
		inp.ContainerID = playlistContainerId;
		inp.ObjectID = songObjectId;
		try {
			CreateReferenceOutput out = getContentDirectoryService().createReference(inp);
			log.debug("song added. returned object id : {} ", out.NewID);
			return out.NewID;
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
		return new MediaServerDto(getBiggestIconUrl(), getUDN().getIdentifierString(), getFriendlyName(), true);
	}

	@Override
	public void deleteObject(String objectId) {
		log.debug("deleting object with id {} ...", objectId);
		DestroyObjectInput inp = new DestroyObjectInput();
		inp.ObjectID = objectId;
		try {
			getContentDirectoryService().destroyObject(inp);
			log.error("deleteObject : failed for id {} ", objectId);
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	private String browseChildrenSearchItem(String objectId, String title) {
		log.debug("searching for items in folder id {} with title {} ...", objectId, title);
		BrowseInput inp = new BrowseInput();
		inp.ObjectID = objectId;
		inp.SortCriteria = "";
		inp.StartingIndex = 0L;
		inp.RequestedCount = 999L;
		inp.Filter = "*";
		ContainerItemDto resultContainer = browseChildren(inp);
		log.debug("container music-items count : " + resultContainer.musicItemDto.size());
		log.debug("container countainer count : " + resultContainer.containerDto.size());
		for (MusicItemDto item : resultContainer.musicItemDto) {
			log.debug("music item found named : {} ", item.title);
			if (item.title.equalsIgnoreCase(title)) {
				log.info("browseChildrenSearchItem : returning folderID {}", item.objectID);
				return item.objectID;
			}
		}
		log.info("no objects found in container {} for title {}", objectId, title);
		return null;
	}

	@Override
	public String getOrCreateItem(String parentContainerId, File file) throws Exception {
		String itemId = browseChildrenSearchItem(parentContainerId, file.getName().toString());
		if (itemId == null) {
			Item item = createItem(parentContainerId, file);
			if (item == null) {
				log.error("could not create item in parent container id {} with filename {}", parentContainerId, file.getName().toString());
				throw new RuntimeException("item could no be created");
			}
			return item.getId();
		} else {
			log.info("getOrCreateItem : returning itemID {}", itemId);
			return itemId;
		}
	}

	@Override
	public Item createItem(String parentContainerId, File file) throws Exception {
		CreateObjectInput inp = new CreateObjectInput();
		inp.ContainerID = parentContainerId;
		Item item = new Item();
		item.setClazz(new Class("object.item"));
		item.setTitle(file.getName());
		item.setParentID(parentContainerId);
		item.setId("");
		DIDLParser parser = new DIDLParser();
		DIDLContent content = new DIDLContent();
		content.addItem(item);
		String xml = parser.generate(content);
		inp.Elements = xml;

		try {
			log.debug("creating object.item with title {} in container {}", file.getName(), parentContainerId);
			CreateObjectOutput out = getContentDirectoryService().createObject(inp);
			log.debug("created object {} ", out.Result);
			content = parser.parse(out.Result);
			log.debug("content count : " + content.getCount());
			if (content.getItems().size() > 0) {
				importFile(content, file);
				return content.getItems().get(0);
			} else {
				log.error("couldn't create item");
				return null;
			}
		} catch (GenActionException e) {
			String errorText = extractErrorText(e.description);
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, errorText, e);
		} catch (Exception e) {
			throw new BackendException(BackendException.DIDL_PARSE_ERROR, e.getMessage(), e);
		}
	}

	// Upload file ...
	private void importFile(DIDLContent content, File file) {
		log.debug("importing resource from {}", file.getName());
		for (Res resource : content.getItems().get(0).getResources()) {
			if (resource.getProtocolInfo().getContentFormat().toLowerCase().startsWith("audio")) {
				if (resource.getImportUri().toString().endsWith("null")) {
					log.error("import uri ends with NULL");
				} else {
					log.info("import uri : " + resource.getImportUri());
				}
				RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("filename", file.getName(), RequestBody.create(file, MediaType.parse("application/octet-stream")))
					.build();

				Request request = new Request.Builder().url(resource.getImportUri().toString()).post(requestBody).build();

				Call call = okClient.newCall(request);
				try {
					Response response = call.execute();
					log.info("upload response code : " + response.code());
				} catch (IOException e) {
					log.error("importFile", e);
				}
				break;
			}
		}
	}

	public boolean isUpdateRatingInFile() {
		if (umsServiceEventListener.getStateVariable().AudioUpdateRating != null) {
			return umsServiceEventListener.getStateVariable().AudioUpdateRating;
		} else {
			return false;
		}
	}

	public void newUmsConfig() {
		configureServer();
	}

	public ServerDeviceConfiguration getNewServerConfig() {
		ServerDeviceConfiguration c = super.getNewServerConfig();
		c.updateRatingInFile = isUpdateRatingInFile();
		return c;
	}

	public void updateCurrentConfigState(ServerDeviceConfiguration c) {
		super.updateCurrentConfigState(c);
		c.updateRatingInFile = isUpdateRatingInFile();
		serverConfig.updateServerDevice(c);
	}

	@Override
	public void updateExtApiConfig(ServerDeviceConfiguration serverDeviceConfig) {
		if (serverDeviceConfig == null) {
			log.debug("[UmsServerDevice-{}] do not change config. No configuration.", getFriendlyName());
			return;
		}
		if (umsServiceEventListener.getStateVariable().AudioUpdateRating != null) {
			log.info("[UmsServerDevice-{}] setting AudioUpdateRatingTag to {} ", getFriendlyName(), serverDeviceConfig.updateRatingInFile);
			SetAudioUpdateRatingTagInput inp = new SetAudioUpdateRatingTagInput();
			inp.AudioUpdateRating = serverDeviceConfig.updateRatingInFile;
			umsServices.setAudioUpdateRatingTag(inp);
		}
	}
}
