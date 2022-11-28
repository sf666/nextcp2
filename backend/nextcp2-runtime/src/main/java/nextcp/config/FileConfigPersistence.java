package nextcp.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import nextcp.db.DatabaseConfig;
import nextcp.dto.ApplicationConfig;
import nextcp.dto.Config;
import nextcp.dto.MusicbrainzSupport;
import nextcp.dto.SpotifyConfigDto;
import nextcp.util.FileOpsNio;

@Service
public class FileConfigPersistence
{
    private static final Logger log = LoggerFactory.getLogger(FileConfigPersistence.class.getName());

    private Configuration systemConfig = new SystemConfiguration();

    private static final String DEFAULT_CONFIG_FILENAME = "nextcp2config.json";
    private static final String DEFAULT_UNIX_CONFIG_PATH = "/etc/nextcp2";

    private String log4jConfigFile = "";
    private String configurationFilename = null;
    private Config config = null;
    private ObjectMapper om = new ObjectMapper();
    private ConfigDefaults configDefaults = new ConfigDefaults();

    public FileConfigPersistence()
    {
        try
        {
            // Ignore member stored in database JSON Key-Value db.
            om.addMixIn(Config.class, IgnoreConfigPropertyMixin.class);

            findConfig();
            readConfig();
        }
        catch (Exception e)
        {
            log.warn("Error while reading config file.", e);
        }
    }

    @Bean
    public DatabaseConfig dbConfigProducer()
    {
        return new DatabaseConfig(config.applicationConfig.databaseFilename);
    }

    public Config getConfig()
    {
        return config;
    }

    public void writeConfig()
    {
        if (configurationFilename != null)
        {
            ObjectWriter writer = om.writer();
            try
            {
                String cc_file = writer.withDefaultPrettyPrinter().writeValueAsString(config);
                FileOpsNio.writeFile(configurationFilename, cc_file.getBytes(Charset.forName("UTF-8")));
            }
            catch (IOException e)
            {
                log.error("could not write config", e);
            }
        }
        else
        {
            log.warn("could not write configuration. Missing config filename.");
        }
    }

    private void readConfig() throws JsonParseException, JsonMappingException, IOException
    {
        if (configurationFilename != null)
        {
            try
            {
                config = om.readValue(new File(configurationFilename), Config.class);
            }
            catch (Exception e)
            {
                log.warn("supplied config file is broken. Generating default config ...");
                config = getDefaultConfig();
            }
            configDefaults.applyDefaults(config);
        }
        else
        {
            log.warn("could not read configuration. Missing config filename.");
        }
    }

    // INFO : no logging while bootstrapping config file ...
    private void findConfig()
    {
        log.info("trying to find configuration file ...");
        if (getSystemPropertyConfigFile() != null)
        {
            configurationFilename = getSystemPropertyConfigFile();
            log.info("Using configuration file provided by system-property : '" + getSystemPropertyConfigFile() + "'");
        }
        else if (getUnixPropertyFile() != null)
        {
            configurationFilename = getUnixPropertyFile();
            log.info("Using unix-style configuration file : '" + getUnixPropertyFile() + "'");
        }
        else if (getUserHomePropertyFile() != null)
        {
            configurationFilename = getUserHomePropertyFile();
            log.info("Using home-directory configuration file : '" + getUserHomePropertyFile() + "'");
        }
        else if (getUserWorkdirPropertyFile() != null)
        {
            configurationFilename = getUserWorkdirPropertyFile();
            log.info("Using work-directory configuration file : '" + getUserWorkdirPropertyFile() + "'");
        }
        else
        {
            configurationFilename = FilenameUtils.concat(systemConfig.getString("user.dir"), DEFAULT_CONFIG_FILENAME);
            config = getDefaultConfig();
            log.warn("config file not found. Generating new file.");
            log.warn("crating default config file at location : " + configurationFilename);
            writeConfig();
            if (getUserWorkdirPropertyFile() == null)
            {
                log.error("failed to generte default config file. Exiting ...");
                throw new RuntimeException("No config files available.");
            }
        }
    }

    private Config getDefaultConfig()
    {
        Config c = new Config();

        c.applicationConfig = new ApplicationConfig();
        c.applicationConfig.generateUpnpCode = false;
        c.applicationConfig.generateUpnpCodePath = System.getProperty("java.io.tmpdir");
        c.applicationConfig.databaseFilename = FilenameUtils.concat(systemConfig.getString("user.dir"), "nextcp2_db");
        c.applicationConfig.embeddedServerPort = 8085;
        c.applicationConfig.itemsPerPage = 100L;
        c.applicationConfig.nextPageAfter = 60L;
        c.applicationConfig.sseEmitterTimeout = 180000L;
        c.applicationConfig.log4jConfigFile = FilenameUtils.concat(systemConfig.getString("user.dir"), "log4j2.xml");
        c.applicationConfig.libraryPath = systemConfig.getString("user.dir");
        c.applicationConfig.loggingDateTimeFormat = "yyyy-MM-dd HH:mm:ss";

        createDefaultLog(c.applicationConfig.log4jConfigFile);

        c.clientConfig = new ArrayList<>();
        c.radioStation = new ArrayList<>();
        c.musicbrainzSupport = new MusicbrainzSupport("", "");
        c.spotifyConfig = new SpotifyConfigDto();
        return c;
    }

    private void createDefaultLog(String log4jConfigFile2)
    {
        String basePath = FilenameUtils.getFullPath(log4jConfigFile2);
        String logfile = FilenameUtils.concat(basePath, "nextcp2.log");

        String data = "<Configuration status=\"info\">\n" + "    <Appenders>\n" + "        <Console name=\"Console\" target=\"SYSTEM_OUT\">\n"
                + "            <PatternLayout pattern=\"%d{HH:mm:ss} [%t] %-5level %logger{36} - %msg%n\" />\n" + "        </Console>\n" + "\n"
                + String.format("        <File name=\"logfile\" fileName=\"%s\">\n", logfile) + "                <PatternLayout>\n"
                + "                        <Pattern>%d %p %c{5.5.~.~} [%t] %m%n</Pattern>\n" + "                </PatternLayout>\n" + "        </File>\n"
                + "        <Async name=\"Async\">\n" + "                <AppenderRef ref=\"logfile\"/>\n" + "        </Async>\n" + "    </Appenders>\n" + "\n" + "    <Loggers>\n"
                + "        <Root level=\"warn\">\n" + "                <AppenderRef ref=\"Async\" />\n" + "                <AppenderRef ref=\"Console\" />\n" + "        </Root>\n"
                + "    </Loggers>\n" + "</Configuration>\n" + "";
        try
        {
            FileOpsNio.writeFile(log4jConfigFile2, data.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String getUserWorkdirPropertyFile()
    {
        if (getConfigFileIfExists(systemConfig.getString("user.dir")) == null)
        {
            log.info(String.format("no config file found at user workdir : %s", DEFAULT_UNIX_CONFIG_PATH));
        }
        return getConfigFileIfExists(systemConfig.getString("user.dir"));
    }

    private String getUnixPropertyFile()
    {
        if (getConfigFileIfExists(DEFAULT_UNIX_CONFIG_PATH) == null)
        {
            log.info("unix config file not found : " + DEFAULT_UNIX_CONFIG_PATH);
        }
        return getConfigFileIfExists(DEFAULT_UNIX_CONFIG_PATH);
    }

    private String getSystemPropertyConfigFile()
    {
        if (systemConfig.getString("configFile") == null)
        {
            log.info("system-property 'configFile' not provided (use '-D configFile=/PATH_TO_CONFIG_FILE')");
        }
        return systemConfig.getString("configFile");
    }

    private String getUserHomePropertyFile()
    {
        if (getConfigFileIfExists(systemConfig.getString("user.home")) == null)
        {
            log.info(String.format("no config file found at user home directory : %s", systemConfig.getString("user.home")));
        }
        return getConfigFileIfExists(systemConfig.getString("user.home"));
    }

    private String getConfigFileIfExists(String dir)
    {
        String filename = FilenameUtils.concat(dir, DEFAULT_CONFIG_FILENAME);
        if (new File(filename).exists())
        {
            return filename;
        }
        else
        {
            return null;
        }
    }

    public String getLog4jConfigFile()
    {
        return log4jConfigFile;
    }

    public void setLog4jConfigFile(String log4jConfigFile)
    {
        this.log4jConfigFile = log4jConfigFile;
    }

}
