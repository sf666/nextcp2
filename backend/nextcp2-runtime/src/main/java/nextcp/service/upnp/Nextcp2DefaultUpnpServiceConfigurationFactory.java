package nextcp.service.upnp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import nextcp.dto.Config;


@Configuration
public class Nextcp2DefaultUpnpServiceConfigurationFactory {

	private static final Logger log = LoggerFactory.getLogger(Nextcp2DefaultUpnpServiceConfigurationFactory.class.getName());
	
	@Autowired
	private Config config = null;

	
	public Nextcp2DefaultUpnpServiceConfigurationFactory() {
		log.debug("init UpnpConfFactory");
	}
	
	@Bean
    @Scope(value = "prototype")
	public Nextcp2UpnpUserConfig getUserConfig() {
		return new Nextcp2UpnpUserConfig(config.applicationConfig.upnpBindInterface);
	}

	@Bean
    @Scope(value = "prototype")
	public Nextcp2DefaultUpnpServiceConfiguration getServcerConfig(Nextcp2UpnpUserConfig userConf) {
		return new Nextcp2DefaultUpnpServiceConfiguration(userConf);
	}
}
