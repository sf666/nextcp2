package nextcp.eventBridge;


import org.jupnp.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import nextcp.config.RendererConfig;
import nextcp.dto.DevicePowerChanged;
import nextcp.dto.RendererDeviceConfiguration;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;

/**
 * Bridges Device Driver events into this domain
 */
@Controller
public class DeviceDriverCallbackEvents
{
    private static final Logger log = LoggerFactory.getLogger(DeviceDriverCallbackEvents.class.getName());
    
    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @Autowired
    private RendererConfig rendererConfigService = null;
    
    public DeviceDriverCallbackEvents()
    {
    }

    @EventListener
    public void physicalDeviceDriverPowerStateChange(DevicePowerChanged event)
    {
        if (event.isPowerOn)
        {
            MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(event.udn));
            if (device != null)
            {
                log.info("setting default values for device {}", event.udn);
                RendererDeviceConfiguration config = rendererConfigService.getMediaRendererConfig(event.udn);
                if (config.powerOnVolPercent != null)
                {
                    log.info("  -> volume to {} ", config.powerOnVolPercent);
                    device.setVolume(config.powerOnVolPercent);
                }
                if (config.powerOnBalance != null)
                {
                    log.info("  -> trim balance to {} ", config.powerOnBalance);
                    device.setTrimBalance(config.powerOnBalance);
                }
            }
        }
    }
}
