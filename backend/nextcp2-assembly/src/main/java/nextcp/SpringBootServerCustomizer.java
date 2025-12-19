package nextcp;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jetty.ConfigurableJettyWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import nextcp.dto.Config;

@Component
public class SpringBootServerCustomizer implements WebServerFactoryCustomizer<ConfigurableJettyWebServerFactory> {

	private static final Logger log = LoggerFactory.getLogger(SpringBootServerCustomizer.class.getName());

	@Autowired
	private Config config = null;

	@Autowired
	private Nextcp2JettyServerCustomizer jettyCustom;

	@Override
	public void customize(ConfigurableJettyWebServerFactory factory) {
		log.info("starting application on port " + config.applicationConfig.embeddedServerPort);

		factory.setPort(config.applicationConfig.embeddedServerPort);
		factory.addServerCustomizers(jettyCustom);
	}

	private void addH3Connection(Server server) {
		// HttpConfiguration httpConfig3 = new HttpConfiguration();
		// httpConfig.addCustomizer(new SecureRequestCustomizer());

		// Create and configure the HTTP/3 connector.
		// HTTP3ServerConnector connector3 = new
		// HTTP3ServerConnector(server, sslContextFactory, new
		// HTTP3ServerConnectionFactory(httpConfig3));
		// server.addConnector(connector3);

	}
}
