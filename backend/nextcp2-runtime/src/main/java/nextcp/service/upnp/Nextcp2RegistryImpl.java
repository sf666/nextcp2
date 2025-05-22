package nextcp.service.upnp;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.registry.RegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.config.RendererConfig;

public class Nextcp2RegistryImpl extends RegistryImpl {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2RegistryImpl.class.getName());

	private RemoteDeviceFacade deviceFacade = new RemoteDeviceFacade();
    private RendererConfig rendererConfigService = null;
	

	public Nextcp2RegistryImpl() {
		log.info("[Nextcp2RegistryImpl] constructor ...");
	}

	public Nextcp2RegistryImpl(UpnpService upnpService, RendererConfig rendererConfigService) {
		super(upnpService);
		this.rendererConfigService = rendererConfigService;
	}

	@Override
	public void addDevice(RemoteDevice remoteDevice) {
		log.debug("adding device : " + remoteDevice.getDetails().getFriendlyName());
		super.addDevice(remoteDevice);
	}
	
    public Nextcp2DefaultUpnpServiceConfiguration getConfiguration() {
        return (Nextcp2DefaultUpnpServiceConfiguration) getUpnpService().getConfiguration();
    }
}
