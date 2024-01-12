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
	}

	public Nextcp2RegistryImpl(UpnpService upnpService, RendererConfig rendererConfigService) {
		super(upnpService);
		this.rendererConfigService = rendererConfigService;
	}

	@Override
	public void addDevice(RemoteDevice remoteDevice) {
		if (isDeviceMarkedActive(remoteDevice)) {
			log.debug("adding device : " + remoteDevice.getDetails().getFriendlyName());
			super.addDevice(remoteDevice);
		} else {
			log.debug("skipping inactive device : " + remoteDevice.getDetails().getFriendlyName());
		}
	}

	private boolean isDeviceMarkedActive(RemoteDevice remoteDevice) {
		if (rendererConfigService.isMediaRendererActive(deviceFacade.getUdnAsString(remoteDevice))) {
			log.debug(String.format("[%s] device marked active", deviceFacade.getFriendlyName(remoteDevice)));
			return true;
		} else {
			log.debug(String.format("[%s] device marked inactive", deviceFacade.getFriendlyName(remoteDevice)));
			getConfiguration().inactiveDeviceFound(remoteDevice);
			return false;
		}
	}
	
    public Nextcp2DefaultUpnpServiceConfiguration getConfiguration() {
        return (Nextcp2DefaultUpnpServiceConfiguration) getUpnpService().getConfiguration();
    }
}
