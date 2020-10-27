package nextcp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import nextcp.dto.Config;
import nextcp.util.IApplicationRestartable;

@ComponentScan(
{ "nextcp, codegen" })
@SpringBootApplication(scanBasePackages = "nextcp, devicedriver")
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
        // Startup ...
        NextcpApplicationStartup.args = args;
        NextcpApplicationStartup.context = SpringApplication.run(NextcpApplicationStartup.class, args);
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
        initLogger();
    }

    private void initLogger()
    {
        File log4jFile = new File(config.log4jConfigFile);
        if (log4jFile.exists())
        {
            System.out.println("initializing log4j with external file : " + config.log4jConfigFile);
            Configurator.initialize(null, log4jFile.getAbsolutePath());
        }
    }
}
