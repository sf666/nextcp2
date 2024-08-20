package nextcp.config;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import nextcp.db.service.BasicDbService;
import nextcp.dto.Config;
import nextcp.dto.MusicbrainzSupport;
import nextcp.lastfm.ILastFmConfig;
import nextcp.musicbrainz.MusicBrainzConfig;
import nextcp2.upnp.localdevice.ILocalDeviceConfigProducer;

/**
 * After adding new configuration parameter, do not forget to "applyDefaults()".
 */
@Service
public class ConfigPersistence
{
    private Config config = null;

    private ConfigDefaults configDefaults = new ConfigDefaults();

    private MusicBrainzConfig mb = new MusicBrainzConfig();

    private CodeGenConfig codegen = new CodeGenConfig();

    @Autowired
    private FileConfigPersistence fileConfigPersistence = null;

    @Autowired
    private BasicDbService basicDbService = null;

    private DatabaseConfigPersistence databaseConfigPersistence = null;

    @PostConstruct
    private void init()
    {
        // Read config from filesystem
        config = fileConfigPersistence.getConfig();

        // Read config from database
        databaseConfigPersistence = new DatabaseConfigPersistence(basicDbService, config);
        readDatabaseProperties();

        // Apply defaults and write back
        configDefaults.applyDefaults(config);
        fileConfigPersistence.writeConfig();
        databaseConfigPersistence.writeDatabaseConfig();
    }

    private void readDatabaseProperties()
    {
        config.clientConfig = databaseConfigPersistence.readClientConfigDatabaseProperties();
    }

    public void updateMusicBrainzInjectionBean(MusicbrainzSupport mbConfig)
    {
        mb.username = mbConfig.username;
        mb.password = new String(Base64.getDecoder().decode(mbConfig.password));
    }

    // Beans

    @Bean
    public CodeGenConfig createCodegenConfigElements()
    {
        codegen.setGenerateUpnpCode(config.applicationConfig.generateUpnpCode);
        codegen.setGenerateUpnpCodePath(config.applicationConfig.generateUpnpCodePath);
        return codegen;
    }

    @Bean
    public MusicBrainzConfig musicBraintConfig()
    {
        mb.username = config.musicbrainzSupport.username;
        mb.password = new String(Base64.getDecoder().decode(config.musicbrainzSupport.password));
        return mb;
    }

    @Bean
    public ILastFmConfig lastFmProducer()
    {
        return new ILastFmConfig()
        {

            @Override
            public String getSharedSecret()
            {
                return "8c85da4a87193c501aa6ebd016667715";
            }

            @Override
            public String getApiKey()
            {
                return "a9292ddac1cef440892f454e95c78300";
            }
        };
    }

    @Bean
    public Config rendererConfigProducer()
    {
        return config;
    }

    /**
     * Saves config values in all backends
     */
    public void writeConfig()
    {
        fileConfigPersistence.writeConfig();
        databaseConfigPersistence.writeDatabaseConfig();
    }

    public static void main(String[] args)
    {
        // ConfigPersistence cr = new ConfigPersistence();
        // cr.configurationFilename = "/tmp/cfg.json";
        // cr.config = cr.getDefaultConfig();
        // cr.writeConfig();

    }

}
