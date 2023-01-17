package nextcp;

import java.io.File;
import java.nio.file.Path;

import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;

import nextcp.config.FileConfigPersistence;

public class NextcpLoggingSystemConfiguration extends Log4J2LoggingSystem
{
    private FileConfigPersistence config = new FileConfigPersistence();

    public NextcpLoggingSystemConfiguration(ClassLoader classLoader)
    {
        super(classLoader);
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
        super.initialize(initializationContext, nextcp2configFile(), logFile);
        // LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        //
        // We could set loglevel programmatically here ... lc.getLogger ... 
        // 
    }

}
