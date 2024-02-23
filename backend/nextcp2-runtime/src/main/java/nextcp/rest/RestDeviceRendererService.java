package nextcp.rest;

import java.util.LinkedList;
import java.util.Set;

import org.jupnp.model.types.UDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import nextcp.devicedriver.DeviceCapabilityDto;
import nextcp.devicedriver.DeviceDriverDiscoveryService;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.InputSourceDto;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaRendererSetVolume;
import nextcp.dto.MediaRendererSwitchPower;
import nextcp.dto.TrackInfoDto;
import nextcp.dto.TransportServiceStateDto;
import nextcp.upnp.device.DeviceRegistry;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.product.OpenHomeProductDevice;

/**
 * This class bridges rendering device information.
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/DeviceRendererService")
public class RestDeviceRendererService
{
    private static final Logger log = LoggerFactory.getLogger(RestDeviceRendererService.class.getName());

    @Autowired
    private DeviceRegistry deviceRegistry = null;

    @Autowired
    private DeviceDriverDiscoveryService deviceDriverDiscoveryService = null;

    @PostMapping("/setStandby")
    public void setStandby(@RequestBody MediaRendererSwitchPower newState)
    {
        log.info(String.format("Setting standby state on device %s to : %s", newState.rendererUDN, newState.standby.toString()));
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(newState.rendererUDN));

        if (newState.standby)
        {
            device.getPlaylistServiceBridge().pause();
        }
        device.setStandby(newState.standby);
    }

    public Set<DeviceCapabilityDto> getAvailableDeviceDriverTypes(@RequestBody MediaRendererDto dto)
    {
        Set<DeviceCapabilityDto> externalDeviceDriver = deviceDriverDiscoveryService.getAvailableDeviceDriverTypes();
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(dto.udn));
        checkDevice(device, "get available device driver");
        if (device.hasProductService())
        {
            externalDeviceDriver.add(new OpenHomeProductDevice());
        }
        return externalDeviceDriver;
    }

    @PostMapping("/setVolume")
    public void setVolume(@RequestBody MediaRendererSetVolume newVol)
    {
        log.info(String.format("Setting volume on device %s to : %d", newVol.rendererUDN, newVol.volume));
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(newVol.rendererUDN));
        device.setVolume(newVol.volume);
    }

    @PostMapping("/getDeviceState")
    public DeviceDriverState getDeviceState(@RequestBody MediaRendererDto dto)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(dto.udn));
        checkDevice(device, "get device state");
        if (device.hasDeviceDriver())
        {
            log.info(String.format("Getting device state for device %s : %s", dto.friendlyName, device.getDeviceDriverState()));
            DeviceDriverState dd = device.getDeviceDriverState();
            return dd;
        }
        else
        {
            log.info(String.format("device %s has no active device driver service", dto.friendlyName));
            return new DeviceDriverState(false, dto.udn, null, null, 0, null);
        }
    }

    @PostMapping("/getDeviceTransportServiceState")
    public TransportServiceStateDto getDeviceTransportServiceState(@RequestBody MediaRendererDto dto)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(dto.udn));
        checkDevice(device, "get Device Transport Service State");
        return device.getTransportServiceBridge().getCurrentTransportServiceState();
    }

    @PostMapping("/getDeviceInputSourceList")
    public LinkedList<InputSourceDto> getDeviceInputSourceList(@RequestBody MediaRendererDto dto)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(dto.udn));
        checkDevice(device, "get device input sources");
        return device.getProductService().getSourceList();
    }

    @PostMapping("/getCurrentSourceTrackInfo")
    public TrackInfoDto getDeviceCurrentSourceInfo(@RequestBody MediaRendererDto dto)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(dto.udn));
        checkDevice(device, "get device current source info");
        return device.getTrackInfo();
    }

    @PostMapping("/getDeviceInputSource")
    public InputSourceDto getDeviceInputSource(@RequestBody MediaRendererDto dto)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(dto.udn));
        checkDevice(device, "get device input sources");
        return device.getProductService().getCurrentInputSource();
    }

    //
    // Transport Services
    //
    @PostMapping("/play")
    public void play(@RequestBody String udn)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(udn));
        checkDevice(device, "play");
        device.getTransportServiceBridge().play();
    }

    @PostMapping("/stop")
    public void stop(@RequestBody String udn)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(udn));
        checkDevice(device, "play");
        device.getTransportServiceBridge().stop();
    }

    @PostMapping("/pause")
    public void pause(@RequestBody String udn)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(udn));
        checkDevice(device, "play");
        device.getTransportServiceBridge().pause();
    }

    @PostMapping("/next")
    public void next(@RequestBody String udn)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(udn));
        checkDevice(device, "play");
        device.getTransportServiceBridge().next();
    }


    //
    // Maintenance methods
    //
    
    @PostMapping("/initServices")
    public void initServices(@RequestBody String udn)
    {
        MediaRendererDevice device = deviceRegistry.getMediaRendererByUDN(new UDN(udn));
        checkDevice(device, "play");
        device.initServices();
    }
    
    //
    // Utility methods
    //

    private void checkDevice(MediaRendererDevice device, String action)
    {
        if (device == null)
        {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, action + " failed. Unknown device. Select an available media renderer.");
        }
    }    
}
