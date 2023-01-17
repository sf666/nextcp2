package nextcp;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;

import nextcp.config.FileConfigPersistence;

public class NextcpLoggingSystemConfiguration extends Log4J2LoggingSystem
{
    private FileConfigPersistence config = new FileConfigPersistence();
    private ClassLoader classLoader = null;

    public NextcpLoggingSystemConfiguration(ClassLoader classLoader)
    {
        super(classLoader);
        this.classLoader = classLoader;
    }

    private String nextcp2configFile()
    {
        String path = config.getConfig().applicationConfig.log4jConfigFile;
        if (path.startsWith("~" + File.separator))
        {
            path = System.getProperty("user.home") + path.substring(1);
        }
        else if (path.startsWith("~"))
        {
            throw new UnsupportedOperationException("Home dir expansion not implemented for explicit usernames");
        }
        String configFile = "file://" + Path.of(path).toAbsolutePath().normalize().toString();
        return configFile;
    }

    @Override
    public void initialize(LoggingInitializationContext initializationContext, String configLocation, LogFile logFile)
    {
        try
        {
            super.initialize(initializationContext, nextcp2configFile(), logFile);
            System.err.println("initialized logging system.");
            // LoggerContext lc = (LoggerContext) LogManager.getContext(false);
            //
            // We could set loglevel programmatically here ... lc.getLogger ...
            //
        }
        catch (Exception e)
        {
            File f =  new File (nextcp2configFile());
            System.err.println("log4j2 configuration file read from nextcp2 config file could not be loaded.");
            System.err.println(e.getMessage());
            if (!f.exists())
            {
                System.err.println("File does not exist : " + f.getAbsolutePath());
            }
            
            Log4J2LoggingSystem ls = new Log4J2LoggingSystem(classLoader);
            ls.beforeInitialize();
            ls.initialize(initializationContext, configLocation, logFile);
        }
    }

}
