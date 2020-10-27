package nextcp.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextcp.config.ConfigService;
import nextcp.devicedriver.DeviceCapabilityDto;
import nextcp.devicedriver.DeviceDriverDiscoveryService;
import nextcp.dto.Config;
import nextcp.dto.DeviceDriverCapability;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.dto.UiClientConfig;
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
    private DeviceDriverDiscoveryService deviceDriverDiscoveryService = null;
    
    @Autowired
    private ApplicationContext context;

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

    @PostMapping("/saveAllMediaRendererConfig")
    public void saveAllRendererConfig(@RequestBody List<RendererDeviceConfiguration> rendererDevices)
    {
        try
        {
            configService.updateRendererDevices(rendererDevices);
        }
        catch (Exception e)
        {
            log.error("saveAllMediaRendererConfig", e);
        }
    }

    @PostMapping("/saveMediaRendererConfig")
    public void saveRendererConfig(@RequestBody RendererDeviceConfiguration rendererDevice)
    {
        try
        {
            configService.updateRendererDevice(rendererDevice);
        }
        catch (Exception e)
        {
            log.error("saveMediaRendererConfig", e);
        }
    }

    @PostMapping("/deleteMediaRendererConfig")
    public void deleteRendererConfig(@RequestBody RendererDeviceConfiguration rendererDevice)
    {
        try
        {
            configService.deleteRendererDevice(rendererDevice);
        }
        catch (Exception e)
        {
            log.error("deleteMediaRendererConfig", e);
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
