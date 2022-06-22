package nextcp.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.fourthline.cling.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.config.RendererConfig;
import nextcp.config.ServerConfig;
import nextcp.devicedriver.DeviceCapabilityDto;
import nextcp.devicedriver.DeviceDriverDiscoveryService;
import nextcp.dto.ApplicationConfig;
import nextcp.dto.Config;
import nextcp.dto.DeviceDriverCapability;
import nextcp.dto.MusicbrainzSupport;
import nextcp.dto.RendererConfigDto;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.dto.ServerConfigDto;
import nextcp.dto.ServerDeviceConfiguration;
import nextcp.dto.ToastrMessage;
import nextcp.dto.UiClientConfig;
import nextcp.service.ConfigService;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.DeviceDriver;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.MediaRendererFactories;
import nextcp.util.IApplicationRestartable;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/ConfigurationService")
public class RestConfigurationService
{
    private static final Logger log = LoggerFactory.getLogger(RestConfigurationService.class.getName());

    public static String CLIENT_CONFIG_QUEUENAME = "CONFIG_CHANGED";

    private List<DeviceDriverCapability> deviceDriverList = new ArrayList<DeviceDriverCapability>();

    @Autowired
    private Config config = null;

    @Autowired
    private ConfigService configService = null;

    @Autowired
    private RendererConfig rendererConfigService = null;

    @Autowired
    private ServerConfig serverConfigService = null;

    @Autowired
    private DeviceDriverDiscoveryService deviceDriverDiscoveryService = null;

    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationEventPublisher publisher = null;

    @PostConstruct
    private void init()
    {
        for (DeviceCapabilityDto d : deviceDriverDiscoveryService.getAvailableDeviceDriverTypes())
        {
            deviceDriverList.add(new DeviceDriverCapability(d.getDeviceType(), d.getDeviceDescription()));
        }
    }

    @GetMapping("/configuration")
    public Config getConfiguration()
    {
        return config;
    }

    @GetMapping("/availableDeviceDriver")
    public List<DeviceDriverCapability> getAvailableDeviceDriver()
    {
        return deviceDriverList;
    }

    @PostMapping("/saveMusicBrainzConfig")
    public void saveClientConfig(@RequestBody MusicbrainzSupport mbConfig)
    {
        try
        {
            configService.saveMusicBrainzConfig(mbConfig);
        }
        catch (Exception e)
        {
            log.error("saveMusicBrainzConfig", e);
        }
    }

    @PostMapping("/saveClientProfile")
    public void saveClientConfig(@RequestBody UiClientConfig clientProfile)
    {
        try
        {
            configService.addClientProfile(clientProfile);
        }
        catch (Exception e)
        {
            log.error("saveClientConfig", e);
        }
    }

    @PostMapping("/deleteClientProfile")
    public void deleteClientProfile(@RequestBody UiClientConfig clientProfile)
    {
        try
        {
            configService.deleteClientProfile(clientProfile);
            publisher.publishEvent(new ToastrMessage(null, "info", "client profile", "profile deleted : " + clientProfile.clientName));
            log.debug("Client config removed : " + clientProfile);
        }
        catch (Exception e)
        {
            log.error("deleteClientProfile", e);
            publisher.publishEvent(new ToastrMessage(null, "error", "client profile", "profile not deleted because of : " + clientProfile.clientName));
        }
    }

    @GetMapping("/getMediaRendererConfig")
    public RendererConfigDto getMediaRendererConfig()
    {
        try
        {
            return rendererConfigService.getConfig();
        }
        catch (Exception e)
        {
            log.error("getMediaRendererConfig", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "reading media renderer config failed : " + e.getMessage());
        }
    }

    @GetMapping("/getMediaServerConfig")
    public ServerConfigDto getMediaServerConfig()
    {
        try
        {
            return serverConfigService.getServerConfig();
        }
        catch (Exception e)
        {
            log.error("getMediaRendererConfig", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "reading media renderer config failed : " + e.getMessage());
        }
    }

    @PostMapping("/saveApplicationConfig")
    public void saveApplicationConfig(@RequestBody ApplicationConfig rendererDevice)
    {
        try
        {
            serverConfigService.updateFileServerConfig(rendererDevice);
        }
        catch (Exception e)
        {
            log.error("saveFileServerConfig", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "saving file server config failed : " + e.getMessage());
        }
    }

    @PostMapping("/saveMediaRendererConfig")
    public void saveRendererConfig(@RequestBody RendererDeviceConfiguration rendererDevice)
    {
        try
        {
            rendererConfigService.updateRendererDevice(rendererDevice);

            MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(rendererDevice.mediaRenderer.udn));
            device.updateDeviceDriver();
        }
        catch (Exception e)
        {
            log.error("saveMediaRendererConfig", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "saving media renderer config failed : " + e.getMessage());
        }
    }

    @PostMapping("/saveMediaServerConfig")
    public void saveMediaServerConfig(@RequestBody ServerDeviceConfiguration serverDevice)
    {
        try
        {
            serverConfigService.updateServerDevice(serverDevice);
        }
        catch (Exception e)
        {
            log.error("saveMediaServerConfig", e);
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "saving media server config failed : " + e.getMessage());
        }
    }

    @PostMapping("/deleteMediaRendererConfig")
    public void deleteRendererConfig(@RequestBody RendererDeviceConfiguration rendererDevice)
    {
        try
        {
            rendererConfigService.deleteRendererDevice(rendererDevice);
        }
        catch (Exception e)
        {
            log.error("deleteMediaRendererConfig", e);
        }
    }

    @PostMapping("/deleteMediaServerConfig")
    public void deleteMediaServerConfig(@RequestBody ServerDeviceConfiguration serverDevice)
    {
        try
        {
            serverConfigService.deleteServerDevice(serverDevice);
        }
        catch (Exception e)
        {
            log.error("deleteMediaServerConfig", e);
        }
    }

    @GetMapping("/restart")
    void restart()
    {
        Thread restartThread = new Thread(() -> {
            try
            {
                Thread.sleep(1000);
                IApplicationRestartable app = (IApplicationRestartable) context.getBean("nextcpApplicationStartup");
                app.restart();
            }
            catch (InterruptedException ignored)
            {
            }
        });
        restartThread.setDaemon(false);
        restartThread.start();
    }
}
