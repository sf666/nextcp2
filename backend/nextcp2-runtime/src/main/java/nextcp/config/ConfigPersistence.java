package nextcp.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import nextcp.dto.Config;
import nextcp.dto.LocalIndexSupport;
import nextcp.dto.MusicbrainzSupport;
import nextcp.dto.RatingStrategy;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.musicbrainz.MusicBrainzConfig;
import nextcp.rating.RatingConfig;
import nextcp.util.FileOpsNio;

@Controller
public class ConfigPersistence
{
    private static final Logger log = LoggerFactory.getLogger(ConfigPersistence.class.getName());

    private Config config = null;

    private String configurationFilename = null;

    private Configuration systemConfig = new SystemConfiguration();

    private static final String DEFAULT_CONFIG_FILENAME = "nextcp2Config.json";
    private static final String DEFAULT_UNIX_CONFIG_PATH = "/etc/nextcp2";

    private ObjectMapper om = new ObjectMapper();

    private String log4jConfigFile = "";

    public String getLog4jConfigFile()
    {
        return log4jConfigFile;
    }

    public void setLog4jConfigFile(String log4jConfigFile)
    {
        this.log4jConfigFile = log4jConfigFile;
    }

    @PostConstruct
    public void init()
    {
        findConfig();
        readConfig();
        writeConfig();
    }

    @Bean
    public MusicBrainzConfig musicBraintConfig()
    {
        MusicBrainzConfig mb = new MusicBrainzConfig();
        mb.username = config.musicbrainzSupport.username;
        mb.password = config.musicbrainzSupport.password;
        return mb;
    }

    @Bean
    public RatingConfig ratingConfigProducer()
    {
        RatingConfig rc = new RatingConfig();
        rc.isActive = config.localIndexerSupport.isActive;
        rc.databaseFilename = config.localIndexerSupport.databaseFilename;
        rc.musicDirectory = config.localIndexerSupport.musicRootPath;
        rc.supportedFileTypes = config.localIndexerSupport.supportedFileTypes;
        return rc;
    }

    @Bean
    public Config rendererConfigProducer()
    {
        return config;
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
            log.info("system-property 'configFile' not found. Can be provided on the command line '-D configFile=/PATH_TO_CONFIG_FILE' ");
        }
        return systemConfig.getString("configFile");
    }

    public void readConfig()
    {
        if (configurationFilename != null)
        {
            try
            {
                config = om.readValue(new File(configurationFilename), Config.class);
                applyDefaults();
            }
            catch (JsonParseException e)
            {
                log.error("error in config file. File could not be parsed.", e);
            }
            catch (IOException e)
            {
                log.error("could not write config", e);
            }
        }
        else
        {
            log.warn("could not read configuration. Missing config filename.");
        }
    }

    private void applyDefaults()
    {
        if (config.clientConfig == null)
        {
            config.clientConfig = new ArrayList<>();
        }
        if (config.radioStation == null)
        {
            config.radioStation = new ArrayList<>();
        }
        if (config.generateUpnpCode == null)
        {
            config.generateUpnpCode = false;
        }
        if (config.rendererDevices == null)
        {
            config.rendererDevices = new ArrayList<>();
        }
        if (config.localIndexerSupport == null)
        {
            config.localIndexerSupport = new LocalIndexSupport();
        }
        if (config.musicbrainzSupport == null)
        {
            config.musicbrainzSupport = new MusicbrainzSupport(false, "", "");
        }
        if (config.ratingStrategy == null)
        {
            config.ratingStrategy = new RatingStrategy(true, true, true, "NONE");
        }
    }

    public void writeConfig()
    {
        if (configurationFilename != null)
        {
            ObjectWriter writer = om.writer();
            try
            {
                FileOpsNio.writeFile(configurationFilename, writer.withDefaultPrettyPrinter().writeValueAsString(config).getBytes(Charset.forName("UTF-8")));
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

    private Config getDefaultConfig()
    {
        Config c = new Config();
        c.generateUpnpCode = false;
        c.generateUpnpCodePath = System.getProperty("java.io.tmpdir");
        c.embeddedServerPort = 8085;
        c.sseEmitterTimeout = 180000L;
        c.log4jConfigFile = FilenameUtils.concat(systemConfig.getString("user.dir"), "log4j2.xml");
        c.libraryPath = systemConfig.getString("user.dir");
        c.clientConfig = new ArrayList<>();
        c.radioStation = new ArrayList<>();
        c.loggingDateTimeFormat = "HH:mm:ss";
        createDefaultLog(c.log4jConfigFile);
        c.loggingDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        c.rendererDevices = new ArrayList<RendererDeviceConfiguration>();
        c.localIndexerSupport = new LocalIndexSupport();
        c.localIndexerSupport.isActive = true;
        c.localIndexerSupport.databaseFilename = FilenameUtils.concat(systemConfig.getString("user.dir"), "rating_db");
        c.localIndexerSupport.musicRootPath = "";
        c.localIndexerSupport.supportedFileTypes = "flac,mp3";
        c.musicbrainzSupport = new MusicbrainzSupport(false, "", "");
        c.ratingStrategy = new RatingStrategy(true, true, true, "FILE");
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

    public static void main(String[] args)
    {
        ConfigPersistence cr = new ConfigPersistence();
        cr.configurationFilename = "/tmp/cfg.json";

        cr.config = cr.getDefaultConfig();

        cr.writeConfig();

    }
}
