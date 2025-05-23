package nextcp.upnp.device.mediarenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import jakarta.annotation.PostConstruct;
import nextcp.devicedriver.DeviceDriverDiscoveryService;
import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.devicedriver.IDeviceDriverService;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.DevicePowerChanged;
import nextcp.dto.DeviceVolumeChanged;
import nextcp.dto.InputSourceDto;

/**
 * This class is responsible for controlling external AV devices directly by it's proprietary device protocol.
 * 
 * <pre>
 * Use case: Attached network streamer to an amplifier or receiver. 
 * 
 * In this scenario, volume control can be applied to to amplifier, leaving the bitstream coming from the streamer untouched.
 *
 * Current direct device control features are:
 * 
 * - power control 
 * - volume control
 * - input control
 * 
 * A reference implementation located in module nextcp2-ma9000 shows how to communicate to a McIntosh Amplifier.
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
        log.info(String.format("%s -> new vol -> %d", driverName, vol));
    }

    @Override
    public void standbyChanged(boolean standbyState)
    {
        DevicePowerChanged event = new DevicePowerChanged(rendererUdn, !standbyState);
        eventPublisher.publishEvent(event);
        eventPublisher.publishEvent(getDeviceDriverState());
        log.info(String.format("%s -> new standby -> %s", driverName, Boolean.toString(standbyState)));
    }

    @Override
    public void inputChanged(InputSourceDto input)
    {
        eventPublisher.publishEvent(getDeviceDriverState());
        log.info(String.format("%s -> new input -> %s", driverName, input.toString()));
    }

	@Override
	public void trimBalanaceChanged(int balance) {
        eventPublisher.publishEvent(getDeviceDriverState());
        log.info("balance changed to : {}", balance);
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
    public void setInput(String id)
    {
        physicalDeviceDriver.setInput(id);
    }

    @Override
    public InputSourceDto getInput()
    {
        return physicalDeviceDriver.getInput();
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

    @Override
    public int getVolume()
    {
        return physicalDeviceDriver.getVolume();
    }

    @Override
    public boolean getStandby()
    {
        return physicalDeviceDriver.getStandby();
    }

	@Override
	public void setTrimBalance(Integer balance) {
        physicalDeviceDriver.setTrimBalance(balance);
	}

	@Override
	public void stop() {
		physicalDeviceDriver.stop();
	}

	@Override
	public void start() {
		physicalDeviceDriver.start();
	}

}
