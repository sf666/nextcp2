package nextcp;

import org.eclipse.jetty.server.Server;
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
            }
        }; 
        
        factory.addServerCustomizers(c);
    }
}
