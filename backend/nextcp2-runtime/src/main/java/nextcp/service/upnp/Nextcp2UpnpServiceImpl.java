package nextcp.service.upnp;

import org.jupnp.UpnpServiceConfiguration;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.protocol.ProtocolFactory;
import org.jupnp.registry.Registry;
import org.jupnp.registry.RegistryImpl;
import nextcp.config.RendererConfig;
import nextcp.dto.Config;


public class Nextcp2UpnpServiceImpl extends UpnpServiceImpl {

	private RendererConfig rendererConfigService;
	
	public Nextcp2UpnpServiceImpl() {
	}

	public Nextcp2UpnpServiceImpl(UpnpServiceConfiguration configuration, RendererConfig rendererConfigService) {
		super(configuration);
		this.rendererConfigService = rendererConfigService;
	}

	@Override
	protected Registry createRegistry(ProtocolFactory protocolFactory) {
        return new Nextcp2RegistryImpl(this, rendererConfigService);
	}
	
	@Override
    public Nextcp2RegistryImpl getRegistry() {
        return (Nextcp2RegistryImpl) super.getRegistry();
    }

}
