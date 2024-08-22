package nextcp.rest;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.jupnp.model.types.UDN;
import org.jupnp.transport.RouterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.annotation.PostConstruct;
import nextcp.config.MediaPlayerConfigService;
import nextcp.mediaplayer.MediaPlayerDiscoveryService;
import nextcp.service.upnp.UpnpServiceFactory;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediaserver.ExtendedApiMediaDevice;
import nextcp.upnp.device.mediaserver.MediaServerDevice;
import nextcp2.upnp.localdevice.IMediaPlayerFactory;
import nextcp2.upnp.localdevice.Nextcp2Renderer;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MediaRendererService")
public class RestMediaRendererService {

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

	public RestMediaRendererService() {
		log.debug("renderer service started ... " + renderer);
	}

	@PostConstruct
	private void init() {
		mpf = mediaPlayerDiscoveryService.getFirstFactory();
		renderer = new Nextcp2Renderer(mpf, mediaPlayerConfigService);
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
		renderer.startPlayScreening();
	}

	@GetMapping("/stopPlayScreening")
	public void stopPlayScreening() {
		renderer.stopPlayScreening();
	}

	@GetMapping("/isPlayScreening")
	public boolean isPlayScreening() {
		return renderer.isPlayScreening();
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
}
