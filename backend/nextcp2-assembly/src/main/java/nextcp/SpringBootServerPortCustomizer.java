package nextcp;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http.UriCompliance;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import nextcp.dto.Config;

@Component
public class SpringBootServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableJettyWebServerFactory> {

	private static final Logger log = LoggerFactory.getLogger(SpringBootServerPortCustomizer.class.getName());

	@Autowired
	private Config config = null;

	@Override
	public void customize(ConfigurableJettyWebServerFactory factory) {
		log.info("starting application on port " + config.applicationConfig.embeddedServerPort);

		factory.setPort(config.applicationConfig.embeddedServerPort);
		// Http2 http2 = new Http2();
		// http2.setEnabled(true);
		// factory.setHttp2(http2);
		JettyServerCustomizer c = new JettyServerCustomizer() {

			@Override
			public void customize(Server server) {
				server.setStopAtShutdown(true);
				server.setStopTimeout(5000L);

				// Jetty doesn't accept encoded URL's for the REST services due
				// to security reasons.
				// Possible URI compliance level : UriCompliance.LEGACY,
				// UriCompliance.RFC3986 or UriCompliance.UNSAFE
				//
				// In example `SimpleDeviceControl/playDefaultRadio` wont't
				// accept spaces in the radio station name in the default
				// configuration.
				for (Connector connector : server.getConnectors()) {
					if (connector instanceof ServerConnector) {
						HttpConnectionFactory connectionFactory = ((ServerConnector) connector)
							.getConnectionFactory(HttpConnectionFactory.class);
						connectionFactory.getHttpConfiguration().setUriCompliance(UriCompliance.LEGACY);
					}
				}
				try {
					createH2Connector(server);
				} catch (GeneralSecurityException | IOException e) {
					log.error("addH2Connector", e);
				}
			}

			private void createH2Connector(Server server) throws GeneralSecurityException, IOException {
				// The HTTP configuration object.
				HttpConfiguration httpConfig = new HttpConfiguration();
				// Add the SecureRequestCustomizer because we are using TLS.
				httpConfig.addCustomizer(new SecureRequestCustomizer());

				// The ConnectionFactory for HTTP/1.1.
				HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);
				http11.getHttpConfiguration().setUriCompliance(UriCompliance.LEGACY);

				// The ConnectionFactory for HTTP/2.
				HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpConfig);
				h2.getHttpConfiguration().setUriCompliance(UriCompliance.LEGACY);

				// The ALPN ConnectionFactory.
				ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
				// The default protocol to use in case there is no negotiation.
				alpn.setDefaultProtocol(http11.getProtocol());

				// Configure the SslContextFactory with the keyStore
				// information.
				SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
				KeyStore keystore = KeyStore.getInstance("PKCS12");
				keystore.load(getClass().getResourceAsStream("springboot.p12"), "password".toCharArray());
				sslContextFactory.setKeyStore(keystore);
				sslContextFactory.setCertAlias("springboot");
				// sslContextFactory.setKeyStorePath("springboot.p12");
				// sslContextFactory.setKeyStorePassword("password");

				// The ConnectionFactory for TLS.
				SslConnectionFactory tls = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

				// The ServerConnector instance.
				ServerConnector connector = new ServerConnector(server, tls, alpn, h2, http11);
				connector.setPort(38443);
				server.addConnector(connector);
				
//				HttpConfiguration httpConfig3 = new HttpConfiguration();
//				httpConfig.addCustomizer(new SecureRequestCustomizer());

				// Create and configure the HTTP/3 connector.
//				HTTP3ServerConnector connector3 = new HTTP3ServerConnector(server, sslContextFactory, new HTTP3ServerConnectionFactory(httpConfig3));
//				server.addConnector(connector3);
			}

		};

		factory.addServerCustomizers(c);
	}
}
