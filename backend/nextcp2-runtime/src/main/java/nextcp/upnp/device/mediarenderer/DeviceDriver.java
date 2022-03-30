package nextcp.upnp.device.mediarenderer;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.devicedriver.DeviceDriverDiscoveryService;
import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.devicedriver.IDeviceDriverService;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.DevicePowerChanged;
import nextcp.dto.DeviceVolumeChanged;

/**
 * Mediarenderer support for functionalities not offered by the device UPnP protocol stack.
 *
 * <pre>
 * Current features are:
 * 
 * - power control 
 * - volume control
 * 
 * The reference implementaion located in module nextcp2-ma9000 shows how to communicate to a McIntosh Amplifier.
 * A TCP/IP to RS-232 converter (bridge) is attached to the amplifiers RS-232 control port. 
 * 
 * State changes of the device update the corresponding internal control point states.
 * </pre>
 */
public class DeviceDriver implements IDeviceDriverCallback, IDeviceDriver
{
    private static final Logger log = LoggerFactory.getLogger(DeviceDriver.class.getName());

    @Autowired
    private DeviceDriverDiscoveryService discovery = null;

    @Autowired
    private ApplicationEventPublisher eventPublisher = null;

    private String driverName;
    private String connectionString;
    private String rendererUdn;

    private IDeviceDriverService physicalDeviceDriver = null;

    public DeviceDriver(String rendererUDN, String driverName, String connectionString)
    {
        this.driverName = driverName;
        this.rendererUdn = rendererUDN;
        this.connectionString = connectionString;
    }

    @PostConstruct
    private void init()
    {
        physicalDeviceDriver = discovery.getDeviceDriver(driverName, connectionString, this, rendererUdn);
        if (physicalDeviceDriver == null)
        {
            log.error(String.format("unable to initialize physical device driver layer for renderer %s. Driver name : %s, connectionString : %s", rendererUdn, driverName,
                    connectionString));
        }
    }

    @Override
    public void volumeChanged(int vol)
    {
        DeviceVolumeChanged event = new DeviceVolumeChanged(rendererUdn, vol);
        eventPublisher.publishEvent(event);
        eventPublisher.publishEvent(getDeviceDriverState());
    }

    @Override
    public void standbyChanged(boolean standbyState)
    {
        DevicePowerChanged event = new DevicePowerChanged(rendererUdn, !standbyState);
        eventPublisher.publishEvent(event);
        eventPublisher.publishEvent(getDeviceDriverState());
    }

    @Override
    public DeviceDriverState getDeviceDriverState()
    {
        if (physicalDeviceDriver == null)
        {
            return null;
        }
        return physicalDeviceDriver.getCurrentState();
    }

    @Override
    public void setVolume(int vol)
    {
        physicalDeviceDriver.setVolume(vol);
    }

    @Override
    public void setStandby(boolean standbyState)
    {
        physicalDeviceDriver.setStandby(standbyState);
    }

    @Override
    public boolean isMonitoringExternalAV()
    {
        return true;
    }

}
