package nextcp;

import org.eclipse.jetty.http.UriCompliance;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import nextcp.dto.Config;

@Component
public class SpringBootServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableJettyWebServerFactory>
{
    private static final Logger log = LoggerFactory.getLogger(SpringBootServerPortCustomizer.class.getName());

    @Autowired
    private Config config = null;

    @Override
    public void customize(ConfigurableJettyWebServerFactory factory)
    {
        log.info("starting application on port " + config.applicationConfig.embeddedServerPort);

        factory.setPort(config.applicationConfig.embeddedServerPort);
//        Http2 http2 = new Http2();
//        http2.setEnabled(true);
//        factory.setHttp2(http2);
        JettyServerCustomizer c = new JettyServerCustomizer()
        {
            @Override
            public void customize(Server server)
            {
                server.setStopAtShutdown(true);
                server.setStopTimeout(5000L);
                
                // Jetty doesn't accept encoded URL's for the REST services due to security reasons. 
                // Possible URI compliance level : UriCompliance.LEGACY, UriCompliance.RFC3986 or UriCompliance.UNSAFE
                //
                // In example `SimpleDeviceControl/playDefaultRadio` wont't accept spaces in the radio station name in the default
                // configuration.
                for (Connector connector : server.getConnectors()) {
                    if (connector instanceof ServerConnector) {
                        HttpConnectionFactory connectionFactory = ((ServerConnector) connector)
                                .getConnectionFactory(HttpConnectionFactory.class);
                        connectionFactory.getHttpConfiguration()
                                .setUriCompliance(UriCompliance.LEGACY);
                    }
                }                
            }
        }; 
        
        factory.addServerCustomizers(c);
    }
}
