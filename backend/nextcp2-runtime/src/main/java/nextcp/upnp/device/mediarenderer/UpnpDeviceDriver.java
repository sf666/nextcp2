package nextcp.upnp.device.mediarenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.domainmodel.device.services.IProductService;
import nextcp.dto.DeviceDriverState;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.RenderingControlService;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.RenderingControlServiceEventListenerImpl;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVolumeInput;

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
        super();
        this.device = device;
        this.eventPublisher = eventPublisher;
        this.volumeService = volumeService;
        this.physicalDeviceDriver = physicalDeviceDriver;
        this.setMaxVol = setMaxVol;
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
        eventPublisher.publishEvent(getDeviceDriverState());
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
            return new DeviceDriverState(true, device.getUdnAsString(), volume, standby);
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
        inp.DesiredVolume = Long.valueOf(vol);
        volumeService.setVolume(null);
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

}
