package nextcp.upnp.device.mediarenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.domainmodel.device.services.IProductService;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.InputSourceDto;
import nextcp.upnp.modelGen.avopenhomeorg.product1.ProductServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.VolumeService;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetVolumeInput;

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
    private IDeviceDriver physicalDeviceDriver = null;
    private boolean setCoveredUpnpDeviceToMaxVolume = false;

    public OpenHomeDeviceDriver(MediaRendererDevice device, ApplicationEventPublisher eventPublisher, IProductService productService, VolumeService volumeService,
            IDeviceDriver physicalDeviceDriver, Boolean setCoveredUpnpDeviceToMaxVolume)
    {
    	super(device.getDevice());
        this.device = device;
        this.eventPublisher = eventPublisher;
        this.productService = productService;
        this.volumeService = volumeService;
        this.physicalDeviceDriver = physicalDeviceDriver;
        this.setCoveredUpnpDeviceToMaxVolume = setCoveredUpnpDeviceToMaxVolume != null ? setCoveredUpnpDeviceToMaxVolume : false;

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
        if (physicalDeviceDriver == null)
        {
            setVolOh(vol);
        }
        else
        {
            physicalDeviceDriver.setVolume(vol);
            if (setCoveredUpnpDeviceToMaxVolume)
            {
                setVolOh(100);
            }
        }
    }

    private void setVolOh(int vol)
    {
        // volume = vol;
        SetVolumeInput inp = new SetVolumeInput();
        inp.Value = (long) vol;
        volumeService.setVolume(inp);
    }

    @Override
    public void setStandby(boolean standbyState)
    {
        // standby = standbyState;
        if (physicalDeviceDriver == null)
        {
            productService.setStandby(standbyState);
        }
        else
        {
            physicalDeviceDriver.setStandby(standbyState);
        }
    }

    //
    // DeviceDriver notification of state changes
    //

    @Override
    public DeviceDriverState getDeviceDriverState()
    {
        if (physicalDeviceDriver == null)
        {
            return new DeviceDriverState(true, device.getUdnAsString(), volume, standby, 0, new InputSourceDto());
        }
        else
        {
            return physicalDeviceDriver.getDeviceDriverState();
        }
    }

    @Override
    public void volumeChanged(int vol)
    {
        if (physicalDeviceDriver == null)
        {
            eventPublisher.publishEvent(getDeviceDriverState());
        }
    }

    @Override
    public void standbyChanged(boolean standbyState)
    {
        this.standby = standbyState;
        eventPublisher.publishEvent(getDeviceDriverState());
    }

    @Override
    public boolean isMonitoringExternalAV()
    {
        if (physicalDeviceDriver == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public int getVolume()
    {
        if (physicalDeviceDriver != null)
        {
            return physicalDeviceDriver.getVolume();
        }
        log.debug("volume: physical device driver unavailable.");
        return 0;
    }

    @Override
    public boolean getStandby()
    {
        if (physicalDeviceDriver != null)
        {
            return physicalDeviceDriver.getStandby();
        }
        log.debug("standby: physical device driver unavailable.");
        return true;
    }

    @Override
    public void setInput(String input)
    {
        log.warn("not yet implemented");
    }

    @Override
    public InputSourceDto getInput()
    {
        log.warn("not yet implemented");
        return null;
    }

    @Override
    public void inputChanged(InputSourceDto input)
    {
        //TODO 
        log.warn("NOT YET IMPLEMENTED");
    }

	@Override
	public void trimBalanaceChanged(int balance) {
		
	}

	@Override
	public void setTrimBalance(Integer balance) {
        if (physicalDeviceDriver == null)
        {
        	log.info("cannot set balance. No device driver.");
        	return;
        }
        else
        {
            physicalDeviceDriver.setTrimBalance(balance);
        }
	}

	@Override
	public void stop() {
        log.debug("stop() ... ");
	}

	@Override
	public void start() {
        log.debug("start() ... ");
	}

}
