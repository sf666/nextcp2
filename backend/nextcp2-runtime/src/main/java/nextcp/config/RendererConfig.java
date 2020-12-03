package nextcp.config;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import nextcp.db.service.BasicDbService;
import nextcp.db.service.KeyValuePair;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.RendererConfigDto;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.eventBridge.SsePublisher;

@Service
public class RendererConfig
{
    private static final Logger log = LoggerFactory.getLogger(RendererConfig.class.getName());

    private ObjectMapper om = new ObjectMapper();

    private static final String CONFIG_KEY_RENDERER_DEVICES = "RENDERER_DEVICES";
    public static final String RENDERE_CONFIG_QUEUENAME = "RENDERER_CONFIG_CHANGED";

    private RendererConfigDto config = new RendererConfigDto();

    private SsePublisher ssePublisher = null;

    private BasicDbService dbService = null;

    @Autowired
    public RendererConfig(BasicDbService dbService, SsePublisher ssePublisher)
    {
        this.dbService = dbService;
        this.ssePublisher = ssePublisher;
        config = readConfig();
    }

    public void writeConfig()
    {
        try
        {
            ObjectWriter writer = om.writer();
            String value = writer.withDefaultPrettyPrinter().writeValueAsString(config);
            dbService.updateConfigValue(new KeyValuePair(CONFIG_KEY_RENDERER_DEVICES, value));
        }
        catch (JsonProcessingException e)
        {
            log.error("could not write config", e);
        }
    }

    public RendererConfigDto getConfig()
    {
        return config;
    }

    private RendererConfigDto readConfig()
    {
        String value = dbService.selectConfigValue(CONFIG_KEY_RENDERER_DEVICES);
        RendererConfigDto renderer = readConfig(value);
        return renderer;
    }

    private RendererConfigDto readConfig(String json)
    {
        if (StringUtils.isAllBlank(json))
        {
            return new RendererConfigDto(new CopyOnWriteArrayList<RendererDeviceConfiguration>());
        }
        try
        {
            return om.readValue(json, RendererConfigDto.class);
        }
        catch (JsonParseException e)
        {
            log.error("error in config file. File could not be parsed.", e);
        }
        catch (IOException e)
        {
            log.error("could not write config", e);
        }
        return null;
    }

    public boolean isMediaRendererActive(String udn)
    {
        if (udn == null)
        {
            return true;
        }

        Optional<RendererDeviceConfiguration> configEntry = config.rendererDevices.stream().filter(d -> d.mediaRenderer.udn.contentEquals(udn)).findFirst();
        if (configEntry.isPresent())
        {
            return configEntry.get().active;
        }

        return true;
    }

    public void addMediaRendererDeviceConfig(RemoteDevice remoteDevice)
    {
        Optional<RendererDeviceConfiguration> configEntry = config.rendererDevices.stream()
                .filter(d -> d.mediaRenderer.udn.contentEquals(remoteDevice.getIdentity().getUdn().getIdentifierString())).findFirst();
        if (configEntry.isPresent())
        {
            log.debug(remoteDevice.getDetails().getFriendlyName() + " is already known.");
            if (!remoteDevice.getIdentity().getDescriptorURL().getHost().contentEquals(configEntry.get().ip))
            {
                configEntry.get().ip = remoteDevice.getIdentity().getDescriptorURL().getHost();
                writeConfig();
            }
        }
        else
        {
            RendererDeviceConfiguration c = new RendererDeviceConfiguration();
            c.active = true;
            c.ip = remoteDevice.getIdentity().getDescriptorURL().getHost();
            c.connectionString = "";
            c.deviceDriverType = "";
            c.displayString = remoteDevice.getDisplayString();
            c.hasOpenHomeDeviceDriver = remoteDevice.findService(new ServiceType("av-openhome-org", "Product")) != null;
            c.mediaRenderer = new MediaRendererDto(remoteDevice.getIdentity().getUdn().getIdentifierString(), remoteDevice.getDetails().getFriendlyName());
            config.rendererDevices.add(c);
            writeAndSendConfig();
            log.info(remoteDevice.getDetails().getFriendlyName() + " added RendererDevice config : " + c);
        }
    }

    public void updateRendererDevice(RendererDeviceConfiguration rendererDevice)
    {
        config.rendererDevices.removeIf(d -> d.mediaRenderer.udn.contentEquals(rendererDevice.mediaRenderer.udn));
        config.rendererDevices.add(rendererDevice);
        writeAndSendConfig();
    }

    public void deleteRendererDevice(RendererDeviceConfiguration rendererDevice)
    {
        config.rendererDevices.removeIf(d -> d.mediaRenderer.udn.contentEquals(rendererDevice.mediaRenderer.udn));
        writeAndSendConfig();
    }

    private void writeAndSendConfig()
    {
        writeConfig();
        ssePublisher.sendObjectAsJson(RENDERE_CONFIG_QUEUENAME, config);
    }

    public RendererDeviceConfiguration getMediaRendererConfig(String udn)
    {
        Optional<RendererDeviceConfiguration> configEntry = config.rendererDevices.stream().filter(d -> d.mediaRenderer.udn.contentEquals(udn)).findFirst();
        if (configEntry.isPresent())
        {
            return configEntry.get();
        }
        return null;
    }
}
