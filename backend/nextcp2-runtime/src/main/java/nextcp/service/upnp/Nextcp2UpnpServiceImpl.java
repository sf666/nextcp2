package nextcp.service.upnp;

import org.jupnp.UpnpServiceConfiguration;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.protocol.ProtocolFactory;
import org.jupnp.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nextcp.config.RendererConfig;


public class Nextcp2UpnpServiceImpl extends UpnpServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2UpnpServiceImpl.class.getName());
	
	private RendererConfig rendererConfigService;
	
	public Nextcp2UpnpServiceImpl() {
	}

	public Nextcp2UpnpServiceImpl(UpnpServiceConfiguration configuration, RendererConfig rendererConfigService) {
		super(configuration);
		this.rendererConfigService = rendererConfigService;
	}

	@Override
	protected Registry createRegistry(ProtocolFactory protocolFactory) {
		log.info("[Nextcp2UpnpServiceImpl] creating UPnP registry ...");
        return new Nextcp2RegistryImpl(this, rendererConfigService);
	}
	
	@Override
    public Nextcp2RegistryImpl getRegistry() {
        return (Nextcp2RegistryImpl) super.getRegistry();
    }

}
