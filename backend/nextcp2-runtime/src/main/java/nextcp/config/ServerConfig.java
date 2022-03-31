package nextcp.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import nextcp.db.service.BasicDbService;
import nextcp.db.service.KeyValuePair;
import nextcp.dto.Config;
import nextcp.dto.NextcpFileConfigDto;
import nextcp.dto.ServerConfigDto;
import nextcp.dto.ServerDeviceConfiguration;
import nextcp.eventBridge.SsePublisher;
import nextcp.service.ConfigService;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

/**
 * This class is responsible for updating server based configuration entries.
 */
@Service
public class ServerConfig
{
    private static final Logger log = LoggerFactory.getLogger(ServerConfig.class.getName());
    private static final String CONFIG_KEY_SERVER_DEVICES = "SERVER_DEVICES";
    public static final String SERVER_CONFIG_QUEUENAME = "SERVER_DEVICES_CONFIG_CHANGED";

    private ServerConfigDto serverConfig = null;
    private Config globalConfig = null;

    private ConfigService configService = null;

    private NextcpFileConfigDto nextcpConfig = null;
    private ObjectMapper om = new ObjectMapper();

    private SsePublisher ssePublisher = null;
    private BasicDbService dbService = null;

    @Autowired
    public ServerConfig(BasicDbService dbService, SsePublisher ssePublisher, Config globalConfig, ConfigService configService)
    {
        this.dbService = dbService;
        this.ssePublisher = ssePublisher;
        this.globalConfig = globalConfig;
        this.configService = configService;

        om.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        serverConfig = readConfig();
    }

    public ServerConfigDto getServerConfig()
    {
        return serverConfig;
    }

    public ServerDeviceConfiguration getMediaServerConfig(String udn)
    {
        if (serverConfig == null || serverConfig.serverDevices == null)
        {
            return null;
        }

        Optional<ServerDeviceConfiguration> configEntry = serverConfig.serverDevices.stream().filter(d -> d.mediaServer.udn.contentEquals(udn)).findFirst();
        if (configEntry.isPresent())
        {
            return configEntry.get();
        }
        return null;
    }

    /**
     * Server devices configuration will be saved in database key-value store.
     */
    public void writeServerDevicesConfig()
    {
        try
        {
            ObjectWriter writer = om.writer();
            String value = writer.withDefaultPrettyPrinter().writeValueAsString(serverConfig.serverDevices);
            dbService.updateJsonStoreValue(new KeyValuePair(CONFIG_KEY_SERVER_DEVICES, value));
        }
        catch (JsonProcessingException e)
        {
            log.error("could not write config", e);
        }
    }

    /**
     * Build config from file system and database entries for client transfer
     * 
     * @return
     */
    private ServerConfigDto readConfig()
    {
        ServerConfigDto sc = new ServerConfigDto();
        sc.nextcpFileConfigDto = getNextcpFileConfigDto();

        try
        {
            String value = "";
            value = dbService.selectJsonStoreValue(CONFIG_KEY_SERVER_DEVICES);
            sc.serverDevices = readMediaServerConfig(value);
            return sc;
        }
        catch (Exception e)
        {
            return generateDefaultConfig();
        }
    }

    public void addMediaServerDeviceConfig(RemoteDevice remoteDevice, MediaServerDevice device)
    {
        Optional<ServerDeviceConfiguration> configEntry = serverConfig.serverDevices.stream()
                .filter(d -> d.mediaServer.udn.contentEquals(remoteDevice.getIdentity().getUdn().getIdentifierString())).findFirst();
        if (configEntry.isPresent())
        {
            log.debug(remoteDevice.getDetails().getFriendlyName() + " is already known.");
            updateDefaults(remoteDevice, configEntry.get());
        }
        else
        {
            ServerDeviceConfiguration c = new ServerDeviceConfiguration();
            c.enabled = true;
            c.ip = remoteDevice.getIdentity().getDescriptorURL().getHost();
            c.displayString = remoteDevice.getDisplayString();
            c.mediaServer = device.getAsDto();
            serverConfig.serverDevices.add(c);
            writeServerDevicesAndSendConfig();
            log.info(remoteDevice.getDetails().getFriendlyName() + " added ServerDevice config : " + c);
        }
    }

    public void updateServerDevice(ServerDeviceConfiguration rendererDevice)
    {
        serverConfig.serverDevices.removeIf(d -> d.mediaServer.udn.contentEquals(rendererDevice.mediaServer.udn));
        serverConfig.serverDevices.add(rendererDevice);
        writeServerDevicesAndSendConfig();
    }

    /**
     * 
     * @param fileConfig
     *            Is the editable part of the filesystem config entries
     */
    public void updateFileServerConfig(NextcpFileConfigDto fileConfig)
    {
        globalConfig.databaseFilename = fileConfig.databaseFilename;
        globalConfig.embeddedServerPort = fileConfig.embeddedServerPort;
        globalConfig.libraryPath = fileConfig.libraryPath;
        configService.writeAndSendConfig();
    }

    public void deleteServerDevice(ServerDeviceConfiguration serverDevice)
    {
        serverConfig.serverDevices.removeIf(d -> d.mediaServer.udn.contentEquals(serverDevice.mediaServer.udn));
        writeServerDevicesAndSendConfig();
    }

    private void writeServerDevicesAndSendConfig()
    {
        writeServerDevicesConfig();
        ssePublisher.sendObjectAsJson(SERVER_CONFIG_QUEUENAME, serverConfig);
    }

    private void updateDefaults(RemoteDevice remoteDevice, ServerDeviceConfiguration serverDeviceConfiguration)
    {
        if (!remoteDevice.getIdentity().getDescriptorURL().getHost().contentEquals(serverDeviceConfiguration.ip))
        {
            serverDeviceConfiguration.ip = remoteDevice.getIdentity().getDescriptorURL().getHost();
        }
        writeServerDevicesAndSendConfig();
    }

    public boolean isMediaServerActive(String udn)
    {
        if (udn == null)
        {
            return true;
        }

        Optional<ServerDeviceConfiguration> configEntry = serverConfig.serverDevices.stream().filter(d -> d.mediaServer.udn.contentEquals(udn)).findFirst();
        if (configEntry.isPresent())
        {
            return configEntry.get().enabled;
        }

        return true;
    }

    private ServerConfigDto generateDefaultConfig()
    {
        ServerConfigDto c = new ServerConfigDto();
        c.serverDevices = new ArrayList<ServerDeviceConfiguration>();
        return c;
    }

    private NextcpFileConfigDto getNextcpFileConfigDto()
    {
        if (nextcpConfig == null)
        {
            nextcpConfig = new NextcpFileConfigDto();
            nextcpConfig.databaseFilename = globalConfig.databaseFilename;
            nextcpConfig.embeddedServerPort = globalConfig.embeddedServerPort;
            nextcpConfig.libraryPath = globalConfig.libraryPath;
        }
        return nextcpConfig;
    }

    private List<ServerDeviceConfiguration> readMediaServerConfig(String json)
    {
        if (StringUtils.isAllBlank(json))
        {
            return new CopyOnWriteArrayList<ServerDeviceConfiguration>();
        }
        try
        {
            return om.readValue(json, new TypeReference<List<ServerDeviceConfiguration>>()
            {
            });
        }
        catch (JsonParseException e)
        {
            log.error("error in config file. File could not be parsed.", e);
        }
        catch (IOException e)
        {
            log.error("could not read config", e);
        }
        return new CopyOnWriteArrayList<ServerDeviceConfiguration>();
    }

}
