package nextcp.upnp.device.mediarenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.domainmodel.device.services.IProductService;
import nextcp.dto.DeviceDriverState;
import nextcp.upnp.modelGen.avopenhomeorg.product.ProductServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.volume.VolumeService;
import nextcp.upnp.modelGen.avopenhomeorg.volume.actions.SetVolumeInput;

/**
 * 
 * OpenHome bridge implementation of DeviceDriver
 */
public class OpenHomeDeviceDriver extends ProductServiceEventListenerImpl implements IDeviceDriver, IDeviceDriverCallback
{
    private static final Logger log = LoggerFactory.getLogger(OpenHomeDeviceDriver.class.getName());

    private int volume;
    private boolean standby;
    private MediaRendererDevice device = null;
    private ApplicationEventPublisher eventPublisher = null;
    private IProductService productService = null;
    private VolumeService volumeService = null;

    public OpenHomeDeviceDriver(MediaRendererDevice device, ApplicationEventPublisher eventPublisher, IProductService productService, VolumeService volumeService)
    {
        super();
        this.device = device;
        this.eventPublisher = eventPublisher;
        this.productService = productService;
        this.volumeService = volumeService;

        try
        {
            volume = Math.toIntExact(volumeService.volume().Value);
        }
        catch (Exception e)
        {
            log.debug("volume service error ", e);
            volume = 0;
        }
        try
        {
            standby = productService.getStandby();
        }
        catch (Exception e)
        {
            log.debug("standby service error ", e);
            standby = true;
        }
    }

    //
    // DeviceDriver calls to OpenHome Services for setting values
    //

    @Override
    public void setVolume(int vol)
    {
        SetVolumeInput inp = new SetVolumeInput();
        inp.Value = (long) vol;
        volumeService.setVolume(inp);
    }

    @Override
    public void setStandby(boolean standbyState)
    {
        productService.setStandby(standbyState);
    }

    //
    // DeviceDriver notification of state changes
    //

    @Override
    public DeviceDriverState getDeviceDriverState()
    {
        return new DeviceDriverState(true, device.getUdnAsString(), volume, standby);
    }

    @Override
    public void volumeChanged(int vol)
    {
        this.volume = vol;
        eventPublisher.publishEvent(getDeviceDriverState());
    }

    @Override
    public void standbyChanged(boolean standbyState)
    {
        this.standby = standbyState;
        eventPublisher.publishEvent(getDeviceDriverState());
    }

}
