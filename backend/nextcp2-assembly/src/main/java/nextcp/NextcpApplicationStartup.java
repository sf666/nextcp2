package nextcp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import jakarta.annotation.PostConstruct;
import nextcp.config.FileConfigPersistence;
import nextcp.dto.Config;
import nextcp.util.IApplicationRestartable;

@SpringBootApplication(scanBasePackages = "nextcp, nextcp2, devicedriver, codegen")
@EnableScheduling
public class NextcpApplicationStartup implements IApplicationRestartable
{
    private static final Logger log = LoggerFactory.getLogger(NextcpApplicationStartup.class.getName());

    @Autowired
    private Config config = null;

    // @Autowired
    // private CommandLineRunner clr = null;
    private static ConfigurableApplicationContext context;
    private static String[] args;

    public static void main(String[] args)
    {
//        System.setProperty("org.springframework.boot.logging.LoggingSystem", NextcpLoggingSystemConfiguration.class.getName());
        // Startup ...
    	Config config = new FileConfigPersistence().getConfig();
    	String log4jConfigFile = config.applicationConfig.log4jConfigFile;
        NextcpApplicationStartup.args = args;

        SpringApplicationBuilder builder = new SpringApplicationBuilder(NextcpApplicationStartup.class);
        builder = builder.properties("server.ssl.enabled=false");
//         builder = addH2Server(builder, config);
        builder = builder.properties(String.format("logging.config=file:%s", log4jConfigFile));
        builder = builder.properties(String.format("server.port=%s", config.applicationConfig.embeddedServerPort));
        
        NextcpApplicationStartup.context = builder.build().run(args);
    }

    
    private static SpringApplicationBuilder addH2Server(SpringApplicationBuilder builder, Config config) {
    	if (config.applicationConfig.embeddedServerSslP12Keystore == null || config.applicationConfig.embeddedServerSslP12Keystore.isEmpty()) {
	    	return builder.properties("server.http2.enabled=true")
		    	.properties("server.ssl.key-store=classpath:springboot.p12")
		    	.properties("server.ssl.key-store-password=password")
		    	.properties("server.ssl.key-store-type=PKCS12");
		} else {
	    	return builder.properties("server.http2.enabled=true")
				    	.properties("server.ssl.key-store=" + config.applicationConfig.embeddedServerSslP12Keystore)
				    	.properties("server.ssl.key-store-password=" + config.applicationConfig.embeddedServerSslP12KeystorePassword)
				    	.properties("server.ssl.key-store-type=PKCS12");
		}
    }
    
    public void restart()
    {
        try
        {
            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            File currentJar;
            currentJar = new File(NextcpApplicationStartup.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            /* is it a jar file? */
            if (!currentJar.getName().endsWith(".jar"))
            {
                log.warn("probably we are getting debugged ... path : " + currentJar);
                return;
            }

            /* Build command: java -jar application.jar */
            ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx)
    {
        return args -> {
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames)
            {
                log.debug(beanName);
            }
        };
    }

    @PostConstruct
    private void init()
    {
    }
}
