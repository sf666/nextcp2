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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

    private String configurationFilename = null;
    private Config config = null;
    private ObjectMapper om = null;
    private ConfigDefaults configDefaults = new ConfigDefaults();

    public FileConfigPersistence()
    {
        try
        {
            // Ignore member stored in database JSON Key-Value db.
        	om = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();

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
                log.warn("[FileConfigPersistence] Supplied config file is broken. Generating default config ...", e);
                config = getDefaultConfig();
            }
            configDefaults.applyDefaults(config);
            log.info("[FileConfigPersistence] Dump ... ");
            log.info(config.toString());
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
        c.applicationConfig.embeddedServerSslPort = 18085;
        c.applicationConfig.embeddedServerSslP12Keystore = "";
        c.applicationConfig.itemsPerPage = 100L;
        c.applicationConfig.nextPageAfter = 60L;
        c.applicationConfig.sseEmitterTimeout = 180000L;
        c.applicationConfig.loggingConfigFile = FilenameUtils.concat(systemConfig.getString("user.dir"), "logback.xml");
        c.applicationConfig.libraryPath = systemConfig.getString("user.dir");
        c.applicationConfig.chatHistorySize = 50;

        createDefaultLog(c.applicationConfig.loggingConfigFile);

        c.radioStation = new ArrayList<>();
        c.musicbrainzSupport = new MusicbrainzSupport("", "");
        c.spotifyConfig = new SpotifyConfigDto();
        return c;
    }

    private void createDefaultLog(String loggingConfigFile)
    {
        File loggingFile = new File(loggingConfigFile);
        String basePath = loggingFile.getParent();
        if (basePath == null)
        {
            basePath = systemConfig.getString("user.dir");
        }
        String logfile = FilenameUtils.getBaseName(loggingConfigFile);

        // NOTE: This is a Logback configuration (default Spring Boot logging system).
        // We use plain token replacement instead of String.format because the template
        // contains many Logback conversion words ("%d", "%level", "%msg", ...) that would
        // otherwise be misinterpreted as format specifiers.
        String data = """
<configuration>

    <property name="LOG_DIR" value="@LOG_DIR@" />
    <property name="LOG_FILE" value="${LOG_DIR}/@LOG_FILE@" />
    <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" />
    <property name="LOG_PATTERN_SHORT" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" />

    <!-- Console Appender -->
    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <!-- Rolling File Appender -->
    <appender name="rollingFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_FILE}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- Roll over daily and additionally when the file reaches 200MB; keep 30 days, gz compressed -->
            <fileNamePattern>${LOG_DIR}/@LOG_FILE@.%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
            <maxFileSize>200MB</maxFileSize>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${LOG_PATTERN_SHORT}</pattern>
        </encoder>
    </appender>

    <!-- Async Appender with increased queue -->
    <appender name="Async" class="ch.qos.logback.classic.AsyncAppender">
        <queueSize>2048</queueSize>
        <discardingThreshold>0</discardingThreshold>
        <appender-ref ref="rollingFile" />
    </appender>

    <root level="warn">
        <appender-ref ref="Async" />
        <!--
        <appender-ref ref="Console" />
        -->
    </root>

    <logger name="nextcp.service.upnp" level="warn" additivity="false">
        <appender-ref ref="Async" />
    </logger>
    <logger name="nextcp.upnp.device.UpnpDeviceDiscovery" level="warn" additivity="false">
        <appender-ref ref="Async" />
    </logger>

    <!-- JAudioTagger -->
    <logger name="org.jaudiotagger" level="warn" additivity="false">
        <appender-ref ref="Async" />
    </logger>

    <!-- Spring -->
    <logger name="org.springframework" level="warn" additivity="false">
        <appender-ref ref="Async" />
    </logger>

</configuration>
        	""".replace("@LOG_DIR@", basePath).replace("@LOG_FILE@", logfile);

        try
        {
            FileOpsNio.writeFile(loggingConfigFile, data.getBytes());
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
}
