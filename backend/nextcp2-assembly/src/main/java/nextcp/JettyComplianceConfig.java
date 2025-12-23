package nextcp;

import java.util.Arrays;
import java.util.Set;
import org.eclipse.jetty.http.UriCompliance;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.springframework.boot.jetty.servlet.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyComplianceConfig {

	public JettyComplianceConfig() {
	}

	@Bean
	public WebServerFactoryCustomizer<JettyServletWebServerFactory> jettyCustomizer() {
		return factory -> factory.addServerCustomizers(server -> {
			Arrays.stream(server.getConnectors()).forEach(connector -> {
				connector.getConnectionFactories().stream().filter(f -> f instanceof HttpConnectionFactory).forEach(f -> {
					HttpConfiguration httpConfig = ((HttpConnectionFactory) f).getHttpConfiguration();
					UriCompliance compliance = UriCompliance.from(Set.of(UriCompliance.Violation.AMBIGUOUS_PATH_ENCODING));

					httpConfig.setUriCompliance(compliance);
				});
			});
		});
	}
}
