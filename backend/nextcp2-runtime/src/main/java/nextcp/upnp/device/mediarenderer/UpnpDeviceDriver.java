package nextcp.upnp.device.mediarenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.domainmodel.device.services.IProductService;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.InputSourceDto;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.RenderingControlService;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.RenderingControlServiceEventListenerImpl;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVolumeInput;

public class UpnpDeviceDriver extends RenderingControlServiceEventListenerImpl implements IDeviceDriver, IDeviceDriverCallback
{
    private static final Logger log = LoggerFactory.getLogger(UpnpDeviceDriver.class.getName());
    private int volume = 0;
    private boolean standby = true;
    private MediaRendererDevice device = null;
    private ApplicationEventPublisher eventPublisher = null;
    private IProductService productService = null;
    private RenderingControlService volumeService = null;
    private IDeviceDriver physicalDeviceDriver = null;
    private boolean setMaxVol = false;

    public UpnpDeviceDriver(MediaRendererDevice device, ApplicationEventPublisher eventPublisher, RenderingControlService volumeService, IDeviceDriver physicalDeviceDriver,
            Boolean setMaxVol)
    {
    	super(device.getDevice());
        this.device = device;
        this.eventPublisher = eventPublisher;
        this.volumeService = volumeService;
        this.physicalDeviceDriver = physicalDeviceDriver;
        this.setMaxVol = setMaxVol != null ? setMaxVol : false;

        volume = 0;
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

    @Override
    public void volumeChanged(int vol)
    {
        this.volume = vol;
        if (physicalDeviceDriver == null)
        {
            // just publish devicedriver state to client
            eventPublisher.publishEvent(getDeviceDriverState());
        }
    }

    @Override
    public void standbyChanged(boolean standbyState)
    {
        log.debug("standbyChanged not implemented");
    }

    @Override
    public DeviceDriverState getDeviceDriverState()
    {
        if (physicalDeviceDriver == null)
        {
            return new DeviceDriverState(true, device.getUdnAsString(), volume, standby, new InputSourceDto());
        }
        else
        {
            return physicalDeviceDriver.getDeviceDriverState();
        }
    }

    @Override
    public void setVolume(int vol)
    {
        if (physicalDeviceDriver == null)
        {
            setVolUpnp(vol);
        }
        else
        {
            physicalDeviceDriver.setVolume(vol);
            if (setMaxVol)
            {
                setVolUpnp(100);
            }
        }
    }

    private void setVolUpnp(int vol)
    {
        SetVolumeInput inp = new SetVolumeInput();
        inp.InstanceID = 0L;
        inp.DesiredVolume = Long.valueOf(vol);
        inp.Channel = "Master";
        volumeService.setVolume(inp);
    }

    @Override
    public void setStandby(boolean standbyState)
    {
        if (physicalDeviceDriver == null)
        {
            log.warn("not handled setStandby ...");
        }
        else
        {
            physicalDeviceDriver.setStandby(standbyState);
        }
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
    	if (isMonitoringExternalAV()) {
    		return physicalDeviceDriver.getVolume();
    	}
    	log.warn("physical device driver is not set : return default volume of : 0");
    	return 0;
    }

    @Override
    public boolean getStandby()
    {
    	if (isMonitoringExternalAV()) {
    		return physicalDeviceDriver.getStandby();
    	}
    	log.warn("physical device driver is not set : return default standby : TRUE");
    	return true;
    }

    @Override
    public void setInput(String input)
    {
    	if (isMonitoringExternalAV()) {
    		physicalDeviceDriver.setInput(input);
    	}
    	log.warn("physical device driver is not set : cannot set input");    	
    }

    @Override
    public InputSourceDto getInput()
    {
    	if (isMonitoringExternalAV()) {
    		return physicalDeviceDriver.getInput();
    	}
    	log.warn("physical device driver is not set : return empty InputSourceDto");
    	return new InputSourceDto();
    }

    @Override
    public void inputChanged(InputSourceDto input)
    {
        //TODO 
        log.warn("NOT YET IMPLEMENTED");
    }

}
