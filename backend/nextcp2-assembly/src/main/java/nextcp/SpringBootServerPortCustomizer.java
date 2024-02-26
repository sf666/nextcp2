package nextcp;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
				sslContextFactory.setProtocol("TLS");
//				sslContextFactory.setProvider("SunJSSE");
				sslContextFactory.setIncludeProtocols("TLSv1.2", "TLSv1.3"); // , "TLSv1.3"
//				sslContextFactory.setIncludeCipherSuites("TLS_AES_128_GCM_SHA256","TLS_AES_256_GCM_SHA384","TLS_CHACHA20_POLY1305_SHA256");
//				sslContextFactory.setNeedClientAuth(false);
				sslContextFactory.setKeyStorePath(getClass().getResource("/springboot.p12").toExternalForm());				
				sslContextFactory.setCertAlias("springboot");
				sslContextFactory.setKeyStorePassword("password");

				// The ConnectionFactory for TLS.
				SslConnectionFactory tls = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());
				// The ConnectionFactory for TLS.
//				SslConnectionFactory tls = new SslConnectionFactory(sslContextFactory, http11.getProtocol());

				// The ServerConnector instance.
				ServerConnector connector = new ServerConnector(server, tls, alpn, h2, http11);
//				ServerConnector connector = new ServerConnector(server, tls, http11);
				connector.setPort(config.applicationConfig.embeddedServerSslPort);
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
