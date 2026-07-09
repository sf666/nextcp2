package nextcp.config;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Explicit data-directory override. When set (environment variable {@code NEXTCP_DATA} or
     * system property {@code nextcp.dataDir}), all freshly generated defaults live inside this
     * directory. This is what the Docker image uses: a single mounted volume that survives
     * container restarts.
     */
    private static final String ENV_DATA_DIR = "NEXTCP_DATA";
    private static final String PROP_DATA_DIR = "nextcp.dataDir";

    /**
     * Device-driver library directory override ({@code NEXTCP_LIB} env / {@code nextcp.libDir}
     * property). The Docker image sets this to {@code /nextcp2/lib}, where the bundled MA9000
     * driver is copied, so a freshly generated config finds the driver. Unset elsewhere, in which
     * case the library path defaults to the data directory.
     */
    private static final String ENV_LIB_DIR = "NEXTCP_LIB";
    private static final String PROP_LIB_DIR = "nextcp.libDir";

    /**
     * HTTP listen-port override ({@code NEXTCP_PORT} env / {@code nextcp.port} property) applied
     * to a freshly generated config's {@code embeddedServerPort}. Unset elsewhere, in which case
     * the default port is used.
     */
    private static final String ENV_PORT = "NEXTCP_PORT";
    private static final String PROP_PORT = "nextcp.port";
    private static final int DEFAULT_PORT = 8085;

    /**
     * UPnP / stream-server bind interface override ({@code NEXTCP_BIND_INTERFACE} env /
     * {@code nextcp.bindInterface} property) applied to a freshly generated config's
     * {@code upnpBindInterface} (an interface name such as {@code eth0}). When unset and running
     * with an explicit data directory (i.e. the Docker image), the primary interface of the host
     * is auto-detected — host networking exposes the host's interfaces to the container, so this
     * picks the interface the host uses to reach the LAN. Empty everywhere else (= bind to all
     * interfaces, the previous behavior for desktop / plain-jar installs).
     */
    private static final String ENV_BIND_IFACE = "NEXTCP_BIND_INTERFACE";
    private static final String PROP_BIND_IFACE = "nextcp.bindInterface";

    /** Subdirectory name used for the app data folder under a per-user location. */
    private static final String APP_DIR_NAME = "nextcp2";

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
            return;
        }

        // An explicit data-directory override (NEXTCP_DATA / -Dnextcp.dataDir, e.g. the Docker
        // volume) is authoritative: use the config inside it, or generate a fresh one there,
        // WITHOUT consulting the legacy /etc, user.home or user.dir locations.
        if (resolveDataDirOverride() != null)
        {
            String existing = getConfigFileIfExists(getDefaultBaseDir());
            if (existing != null)
            {
                configurationFilename = existing;
                log.info("Using data-directory configuration file : '" + configurationFilename + "'");
            }
            else
            {
                generateDefaultConfig();
            }
            return;
        }

        if (getConfigFileIfExists(getDefaultBaseDir()) != null)
        {
            // Platform-specific per-user data directory. Checked before the legacy locations so a
            // config generated here on a previous start is picked up again (settings survive a
            // restart / app update).
            configurationFilename = getConfigFileIfExists(getDefaultBaseDir());
            log.info("Using data-directory configuration file : '" + configurationFilename + "'");
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
            generateDefaultConfig();
        }
    }

    /**
     * Generates a fresh default configuration in the resolved data directory and writes it to disk.
     */
    private void generateDefaultConfig()
    {
        configurationFilename = FilenameUtils.concat(getDefaultBaseDir(), DEFAULT_CONFIG_FILENAME);
        config = getDefaultConfig();
        log.warn("config file not found. Generating new file.");
        log.warn("creating default config file at location : " + configurationFilename);
        writeConfig();
        if (getConfigFileIfExists(getDefaultBaseDir()) == null)
        {
            log.error("failed to generate default config file. Exiting ...");
            throw new RuntimeException("No config files available.");
        }
    }

    /**
     * Base directory for freshly generated defaults (config file, database, logback config, log
     * and UPnP-code sub-directories). Resolution order:
     * <ol>
     * <li>Explicit override via {@code NEXTCP_DATA} / {@code -Dnextcp.dataDir} - used by the Docker
     * image so everything lives in one mounted volume that survives restarts.</li>
     * <li>Otherwise a platform-specific per-user application-data directory, always writable
     * (unlike the working directory, which for a native desktop app is the read-only install
     * location):
     * <ul>
     * <li>macOS: {@code ~/Library/Application Support/nextcp2}</li>
     * <li>Windows: {@code %APPDATA%\nextcp2} (falls back to {@code <user.home>\nextcp2})</li>
     * <li>Linux/other: {@code $XDG_CONFIG_HOME/nextcp2} or {@code ~/.config/nextcp2}</li>
     * </ul>
     * </li>
     * </ol>
     * Existing config files, an explicit {@code -DconfigFile} and the {@code /etc/nextcp2}
     * location are unaffected - this only governs where a brand-new default is created (and later
     * found again).
     */
    private String getDefaultBaseDir()
    {
        String override = resolveDataDirOverride();
        if (override != null)
        {
            return override;
        }
        return platformDefaultDataDir();
    }

    /**
     * @return the explicit data-directory override ({@code NEXTCP_DATA} env var or
     *         {@code nextcp.dataDir} system property), or {@code null} when neither is set.
     */
    private String resolveDataDirOverride()
    {
        String prop = systemConfig.getString(PROP_DATA_DIR);
        if (prop != null && !prop.isBlank())
        {
            return prop.trim();
        }
        String env = System.getenv(ENV_DATA_DIR);
        if (env != null && !env.isBlank())
        {
            return env.trim();
        }
        return null;
    }

    /**
     * Resolves the device-driver library path for a freshly generated config: the
     * {@code NEXTCP_LIB} / {@code nextcp.libDir} override if set (Docker points this at the
     * bundled driver directory), otherwise the given data-directory base.
     */
    private String resolveLibDir(String base)
    {
        String prop = systemConfig.getString(PROP_LIB_DIR);
        if (prop != null && !prop.isBlank())
        {
            return prop.trim();
        }
        String env = System.getenv(ENV_LIB_DIR);
        if (env != null && !env.isBlank())
        {
            return env.trim();
        }
        return base;
    }

    /**
     * @return the platform-specific per-user application-data directory for nextCP/2.
     */
    private String platformDefaultDataDir()
    {
        String home = systemConfig.getString("user.home");
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("mac") || os.contains("darwin"))
        {
            return FilenameUtils.concat(FilenameUtils.concat(home, "Library/Application Support"), APP_DIR_NAME);
        }
        if (os.contains("win"))
        {
            String appData = System.getenv("APPDATA");
            String base = (appData != null && !appData.isBlank()) ? appData : home;
            return FilenameUtils.concat(base, APP_DIR_NAME);
        }
        // Linux / other Unix: follow the XDG base-directory spec.
        String xdg = System.getenv("XDG_CONFIG_HOME");
        String base = (xdg != null && !xdg.isBlank()) ? xdg : FilenameUtils.concat(home, ".config");
        return FilenameUtils.concat(base, APP_DIR_NAME);
    }

    /**
     * Resolves the HTTP listen port for a freshly generated config: the {@code NEXTCP_PORT} /
     * {@code nextcp.port} override if set and valid, otherwise {@link #DEFAULT_PORT}.
     */
    private int resolvePort()
    {
        String value = systemConfig.getString(PROP_PORT);
        if (value == null || value.isBlank())
        {
            value = System.getenv(ENV_PORT);
        }
        if (value != null && !value.isBlank())
        {
            try
            {
                int port = Integer.parseInt(value.trim());
                if (port > 0 && port <= 65535)
                {
                    return port;
                }
                log.warn("ignoring out-of-range listen port '{}', using default {}", value, DEFAULT_PORT);
            }
            catch (NumberFormatException e)
            {
                log.warn("ignoring non-numeric listen port '{}', using default {}", value, DEFAULT_PORT);
            }
        }
        return DEFAULT_PORT;
    }

    /**
     * Resolves the UPnP bind interface for a freshly generated config: the
     * {@code NEXTCP_BIND_INTERFACE} / {@code nextcp.bindInterface} override if set; otherwise, when
     * an explicit data directory is configured (Docker), the auto-detected primary host interface;
     * otherwise empty (bind to all interfaces).
     */
    private String resolveBindInterface()
    {
        String prop = systemConfig.getString(PROP_BIND_IFACE);
        if (prop != null && !prop.isBlank())
        {
            return prop.trim();
        }
        String env = System.getenv(ENV_BIND_IFACE);
        if (env != null && !env.isBlank())
        {
            return env.trim();
        }
        if (resolveDataDirOverride() != null)
        {
            String detected = detectPrimaryInterfaceName();
            if (detected != null && !detected.isBlank())
            {
                log.info("auto-detected host bind interface : {}", detected);
                return detected;
            }
            log.info("could not auto-detect a host bind interface; binding to all interfaces");
        }
        return "";
    }

    /**
     * Detects the name of the primary network interface - the one the host would use to reach the
     * LAN. Uses a connected (but never sending) UDP socket to determine the outbound local address
     * and maps it back to its interface; falls back to the first up, non-loopback, non-virtual
     * interface with a site-local IPv4 address. Returns {@code null} if nothing suitable is found.
     */
    private String detectPrimaryInterfaceName()
    {
        try (DatagramSocket socket = new DatagramSocket())
        {
            // Connecting a UDP socket sends no packet but makes the OS pick the outbound interface.
            socket.connect(InetAddress.getByName("8.8.8.8"), 9);
            InetAddress local = socket.getLocalAddress();
            if (local != null && !local.isAnyLocalAddress())
            {
                NetworkInterface ni = NetworkInterface.getByInetAddress(local);
                if (ni != null)
                {
                    return ni.getDisplayName();
                }
            }
        }
        catch (Exception e)
        {
            log.debug("primary interface detection via socket failed: {}", e.getMessage());
        }
        // Fallback: first usable interface with a site-local IPv4 address.
        try
        {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces()))
            {
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual() || ni.isPointToPoint())
                {
                    continue;
                }
                for (InetAddress addr : Collections.list(ni.getInetAddresses()))
                {
                    if (addr.isSiteLocalAddress() && addr.getAddress().length == 4)
                    {
                        return ni.getDisplayName();
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.debug("primary interface detection via enumeration failed: {}", e.getMessage());
        }
        return null;
    }

    /** Creates the given directory (and parents) if it does not exist yet. */
    private void ensureDir(String dir)
    {
        File d = new File(dir);
        if (!d.isDirectory() && !d.mkdirs())
        {
            log.warn("could not create directory : {}", dir);
        }
    }

    private Config getDefaultConfig()
    {
        Config c = new Config();

        // All defaults live under the resolved data directory (Docker volume or platform
        // per-user dir). Logs and UPnP code get their own sub-directories; the config file,
        // logback config and database sit in the data-dir root.
        String base = getDefaultBaseDir();
        String logsDir = FilenameUtils.concat(base, "logs");
        String upnpCodeDir = FilenameUtils.concat(base, "upnp_code");
        String tmpDir = FilenameUtils.concat(base, "tmp");
        ensureDir(base);
        ensureDir(logsDir);
        ensureDir(upnpCodeDir);
        ensureDir(tmpDir);

        c.applicationConfig = new ApplicationConfig();
        c.applicationConfig.generateUpnpCode = false;
        c.applicationConfig.generateUpnpCodePath = upnpCodeDir;
        c.applicationConfig.databaseFilename = FilenameUtils.concat(base, "nextcp2_db");
        c.applicationConfig.embeddedServerPort = resolvePort();
        c.applicationConfig.embeddedServerSslPort = 18085;
        c.applicationConfig.embeddedServerSslP12Keystore = "";
        c.applicationConfig.itemsPerPage = 100L;
        c.applicationConfig.nextPageAfter = 60L;
        c.applicationConfig.sseEmitterTimeout = 180000L;
        c.applicationConfig.loggingConfigFile = FilenameUtils.concat(base, "logback.xml");
        c.applicationConfig.libraryPath = resolveLibDir(base);
        // Pre-transcode cache for the internal streaming proxy: keep it inside the data dir
        // (on the mounted volume) rather than the ephemeral system temp / container layer.
        c.applicationConfig.localPlayerCacheDir = tmpDir;
        // Bind interface: explicit override, auto-detected host interface (Docker), or empty (all).
        c.applicationConfig.upnpBindInterface = resolveBindInterface();
        c.applicationConfig.chatHistorySize = 50;
        // Hide image items while browsing by default (many folders contain cover images that
        // would otherwise clutter the listing). The UI filters them out when this is false.
        c.applicationConfig.showImageItems = false;

        createDefaultLog(c.applicationConfig.loggingConfigFile, logsDir);

        c.radioStation = new ArrayList<>();
        c.musicbrainzSupport = new MusicbrainzSupport("", "");
        c.spotifyConfig = new SpotifyConfigDto();
        return c;
    }

    private void createDefaultLog(String loggingConfigFile, String logDir)
    {
        String basePath = logDir;
        if (basePath == null || basePath.isBlank())
        {
            basePath = getDefaultBaseDir();
        }
        ensureDir(basePath);
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
