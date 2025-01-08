package nextcp.rest;

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
import org.jupnp.support.model.item.Item;
import org.jupnp.transport.RouterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.annotation.PostConstruct;
import nextcp.dto.MediaPlayerConfigDto;
import nextcp.dto.ToastrMessage;
import nextcp.mediaplayer.MediaPlayerDiscoveryService;
import nextcp.service.upnp.UpnpServiceFactory;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.util.BackendException;
import nextcp2.upnp.localdevice.IMediaPlayerFactory;
import nextcp2.upnp.localdevice.ISongPlayedCallback;
import nextcp2.upnp.localdevice.MediaPlayerConfigService;
import nextcp2.upnp.localdevice.Nextcp2Renderer;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MediaRendererService")
public class RestMediaRendererService implements ISongPlayedCallback {

	private static final Logger log = LoggerFactory.getLogger(RestMediaRendererService.class.getName());

	private Nextcp2Renderer renderer = null;;

	@Autowired
	private UpnpServiceFactory upnpService = null;

	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	MediaPlayerDiscoveryService mediaPlayerDiscoveryService = null;

	@Autowired
	private MediaPlayerConfigService mediaPlayerConfigService = null;

	private IMediaPlayerFactory mpf = null;
	
    @Autowired
    private ApplicationEventPublisher publisher = null;

	public RestMediaRendererService() {
		log.debug("renderer service started ... " + renderer);
	}

	@PostConstruct
	private void init() {
		mpf = mediaPlayerDiscoveryService.getFirstFactory();
		if (mpf == null) {
			log.debug("Player not initialized. No factory found.");
			return;
		}
		renderer = new Nextcp2Renderer(mpf, mediaPlayerConfigService, this);
		try {
			if (!upnpService.upnpService().getRouter().isEnabled()) {
				upnpService.upnpService().getRouter().enable();
			}
		} catch (RouterException e) {
			log.error("router error ... ", e);
		}
		upnpService.upnpService().getRegistry().addDevice(renderer.getLocalDevice());
		upnpService.upnpService().getProtocolFactory().createSendingNotificationAlive(renderer.getLocalDevice()).run();
	}

	@GetMapping("/mediaPlayerExists")
	public boolean getMediaPlayerExists() {
		return mpf != null;
	}

	@GetMapping("/startPlayScreening")
	public void startPlayScreening() {
	}

	@GetMapping("/stopPlayScreening")
	public void stopPlayScreening() {
	}

	@GetMapping("/isPlayScreening")
	public boolean isPlayScreening() {
		return true;
	}

	// Test
	@PostMapping("/createFolder")
	public void create(@RequestBody String serverUdn) {
		ExtendedApiMediaDevice device = getExtendedMediaServerByUdn(serverUdn);
		try {
			device.createFolder("196", "test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/upload")
	public void upload(@RequestBody String serverUdn) {
		// NOT USED. Only for debugging ... 
		ExtendedApiMediaDevice device = getExtendedMediaServerByUdn(serverUdn);
		try {
			device.createItem("196", new File("/Volumes/Data/music/Alternative/Rhye/Blood/01 - Waste.flac"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected ExtendedApiMediaDevice getExtendedMediaServerByUdn(String udn) {
		if (udn == null || StringUtils.isBlank(udn)) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "please provide output device (media-renderer).");
		}

		MediaServerDevice device = deviceRegistry.getMediaServerByUDN(new UDN(udn));
		if (device == null) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Media-Server not found : " + udn);
		}

		if (device instanceof ExtendedApiMediaDevice) {
			return ((ExtendedApiMediaDevice) device);
		}
		throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "extended features not availbale : " + udn);
	}

	@Override
	public void songPlayed(File theFile) {
		MediaPlayerConfigDto mpc = mediaPlayerConfigService.getMediaPlayerConfigDto();
		Path theFilePath = theFile.toPath();
		if (!StringUtils.isBlank(mpc.addToFolderId.id)) {
			ExtendedApiMediaDevice device = getExtendedMediaServerByUdn(mpc.mediaServerUdn);
			try {
				int startPathAt = new File(mpc.workdir).toPath().getNameCount();
				String targetId = mpc.addToFolderId.id;
				while (startPathAt < theFilePath.getNameCount() - 1) {
					Path childName = theFilePath.getName(startPathAt++);
					targetId = device.getOrCreateChildFolderId(targetId, childName.toString());
				}
				String itemId = device.getOrCreateItem(targetId, theFile);
				if (itemId == null) {
					log.error("getOrCreateItem returned with NULL");
		            publisher.publishEvent(new ToastrMessage(null, "warn", "upload file", "adding file failed : " + theFile.getName()));
				} else {
					log.info("File created or updated. Item ID is {}", itemId);
		            publisher.publishEvent(new ToastrMessage(null, "info", "upload file", "added to media server library : " + theFile.getName()));
				}
	            
				if (mpc.addToPlaylist) {
					log.debug ("adding song to playist ... ");
		            if (itemId != null && !StringUtils.isBlank(mpc.addToPlaylistId.id)) {
		            	log.info("Adding song with id {} to playlist with id {} ", itemId, mpc.addToPlaylistId.id);
		            	try {
			            	device.addSongToPlaylist(itemId, mpc.addToPlaylistId.id);
				            publisher.publishEvent(new ToastrMessage(null, "info", "playlist", "song added to playlist : " + theFile.getName()));
		            		log.debug("success : adding song with id {} to playlist with id {}", itemId, mpc.addToPlaylistId.id);
		            	} catch (BackendException e) {
		            		log.debug("adding song to playlist failed.", e);
		        			publisher.publishEvent(new ToastrMessage(null, "warn", "create item failed ", e.getDescription()));
		            	}
		            } else {
		            	log.info("Supplied empty or NULL ids. File with id {} not being added to a playlist {}.", itemId, mpc.addToPlaylistId.id);
		            }
				}
			} catch (ResponseStatusException e) {
	            publisher.publishEvent(new ToastrMessage(null, "error", "upload file", "media server not found"));
			} catch (Exception e) {
				log.warn("error while uploading file.", e);
	            publisher.publishEvent(new ToastrMessage(null, "error", "upload file", e.getMessage()));
			} finally {
				if (theFile.exists()) {
					if (!theFile.delete()) {
			            publisher.publishEvent(new ToastrMessage(null, "error", "upload file", "cannot delete tmp file : " + theFile.getName()));
						log.error("cannot delete tmp file : " + theFile.getName());
					}
				}
			}
		} else {
			log.info("no folder defined");
		}
	}
}
