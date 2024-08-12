package nextcp.rest;

import org.jupnp.transport.RouterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.PostConstruct;
import nextcp.service.upnp.UpnpServiceFactory;
import nextcp2.upnp.localdevice.Nextcp2Renderer;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/MediaRendererService")
public class RestMediaRendererService {
	
	private static final Logger log = LoggerFactory.getLogger(RestMediaRendererService.class.getName());
	
	private Nextcp2Renderer renderer = null;;
	
	@Autowired
	private UpnpServiceFactory upnpService = null;
	
	public RestMediaRendererService() {
		log.debug("renderer service started ... " + renderer);
	}

	@PostConstruct
	private void init() {
		/*
		renderer = new Nextcp2Renderer();
		try {
			if (!upnpService.upnpService().getRouter().isEnabled()) {
				upnpService.upnpService().getRouter().enable();
			}
		} catch (RouterException e) {
			log.error("router error ... ", e);
		}		
		upnpService.upnpService().getRegistry().addDevice(renderer.getLocalDevice());
		upnpService.upnpService().getProtocolFactory().createSendingNotificationAlive(renderer.getLocalDevice()).run();
		*/
	}
}
