package nextcp.rest;

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
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
import nextcp.dto.ContainerDto;
import nextcp.dto.ContainerIdDto;
import nextcp.dto.MediaPlayerConfigDto;
import nextcp.dto.ToastrMessage;
import nextcp.mediaplayer.MediaPlayerDiscoveryService;
import nextcp.service.upnp.Nextcp2UpnpServiceImpl;
import nextcp.service.upnp.UpnpServiceFactory;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp.util.BackendException;
import nextcp2.upnp.localdevice.IMediaPlayerFactory;
import nextcp2.upnp.localdevice.ISongPlayedCallback;
import nextcp2.upnp.localdevice.MediaPlayerConfigService;
import nextcp2.upnp.localdevice.Nextcp2Renderer;
import nextcp.dto.Config;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MediaRendererService")
public class RestMediaRendererService implements ISongPlayedCallback {

	private static final Logger log = LoggerFactory.getLogger(RestMediaRendererService.class.getName());

	private Nextcp2Renderer renderer = null;;

	@Autowired
	private UpnpServiceFactory upnpService = null;

    @Autowired
    private Nextcp2UpnpServiceImpl upnp = null;
    
	@Autowired
	private DeviceRegistry deviceRegistry = null;

	@Autowired
	MediaPlayerDiscoveryService mediaPlayerDiscoveryService = null;

	@Autowired
	private Config config = null;

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
		ISongPlayedCallback cb = this;  

		Runnable r = new Runnable() {
			public void run() {
				try {
					Thread.sleep(10000l);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				mpf = mediaPlayerDiscoveryService.getFirstFactory();
				if (mpf == null) {
					log.debug("Player not initialized. No factory found.");
					return;
				}
				renderer = new Nextcp2Renderer(mpf, mediaPlayerConfigService, cb);
				try {
					if (!upnpService.upnpService().getRouter().isEnabled()) {
						upnpService.upnpService().getRouter().enable();
					}
				} catch (RouterException e) {
					log.error("router error ... ", e);
				}
				
		 		upnp.getRegistry().addDevice(renderer.getLocalDevice());
				upnpService.upnpService().getProtocolFactory().createSendingNotificationAlive(renderer.getLocalDevice()).run();				
			}
		};
		
		Thread mrs = new Thread(r);
		mrs.setName("Media Renderer Service init thread.");
		mrs.start();
	}

	/**
	 * The UI hangs everything media player related off this: the sidebar entry and the actions in the
	 * song options. Reporting the player as absent therefore switches all of it off at once, which is
	 * what the configuration flag is for - the jar stays loaded, it just is not offered any more.
	 */
	@GetMapping("/mediaPlayerExists")
	public boolean getMediaPlayerExists() {
		return mpf != null && !Boolean.FALSE.equals(config.applicationConfig.mediaPlayerEnabled);
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
				String renamed = renamedTarget(device, mpc.addToFolderId);
				if (renamed != null) {
					String message = String.format(
						"not importing %s : the target folder is not \"%s\" any more, id %s is \"%s\" on the media server today. Pick the folder again in the media player settings.",
						theFile.getName(), mpc.addToFolderId.title, mpc.addToFolderId.id, renamed);
					log.error(message);
					publisher.publishEvent(new ToastrMessage(null, "error", "upload file", message));
					return;
				}
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
		            	String renamedPlaylist = renamedTarget(device, mpc.addToPlaylistId);
		            	if (renamedPlaylist != null) {
		            		String message = String.format(
		            			"not adding to a playlist : id %s is \"%s\" today, not \"%s\". Pick the playlist again in the media player settings.",
		            			mpc.addToPlaylistId.id, renamedPlaylist, mpc.addToPlaylistId.title);
		            		log.error(message);
		            		publisher.publishEvent(new ToastrMessage(null, "error", "playlist", message));
		            		return;
		            	}
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

	/**
	 * Whether a configured target - the import folder, the playlist - is still the one that was
	 * picked.
	 *
	 * A media server's object ids are its own database keys, and UMS hands out new ones when it
	 * rebuilds its media store, so an id stored here can come to stand for an entirely different
	 * container. Only the id was ever used and nothing compared it against anything, so imports went
	 * on landing in a stranger's folder without a word. The title is picked together with the id and
	 * is what the user recognises, so comparing the two catches exactly that drift.
	 *
	 * A lookup that fails says nothing about the target and must not stop an import - only a title
	 * that came back and differs counts.
	 *
	 * @return what the id stands for now, or null when it still matches or could not be read
	 */
	private String renamedTarget(ExtendedApiMediaDevice device, ContainerIdDto configured) {
		if (configured == null || StringUtils.isBlank(configured.id) || StringUtils.isBlank(configured.title)) {
			return null;
		}
		if (!(device instanceof MediaServerDevice server)) {
			return null;
		}
		ContainerDto current = server.browseMetadataMeta(configured.id);
		if (current == null || StringUtils.isBlank(current.title)) {
			return null;
		}
		return configured.title.equals(current.title) ? null : current.title;
	}
}
