package devicedriver.McIntosh;

import java.io.IOException;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import devicedriver.McIntosh.control.Commands;
import devicedriver.McIntosh.tcp.IMcIntoshDeviceChanged;
import devicedriver.McIntosh.tcp.McIntoshDeviceConnection;
import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.devicedriver.IDeviceDriverService;
import nextcp.dto.DeviceDriverState;

public class Ma9000Binding implements IMcIntoshDeviceChanged, IDeviceDriverService
{
    private static final Logger log = LoggerFactory.getLogger(Ma9000Binding.class.getName());

    private McIntoshDeviceConnection device = null;

    private DeviceDriverState state = new DeviceDriverState();

    private IDeviceDriverCallback callback = null;

    public Ma9000Binding(SocketAddress hostAddress, IDeviceDriverCallback callback, String rendererUdn) throws IOException
    {
        if (hostAddress == null)
        {
            throw new RuntimeException("hostAddress shall not be null");
        }
        
        log.info("Initializing MA 9000 driver. connecting to : " + hostAddress.toString());

        state.rendererUDN = rendererUdn;
        state.volume = 0;
        state.standby = true;
        state.hasDeviceDriver = true;
        this.callback = callback;
        device = new McIntoshDeviceConnection(this);
        device.open(hostAddress);

        checkPowerState();
    }

    private void checkPowerState()
    {
        device.send(Commands.POWER_STATUS);
    }

    @Override
    public void standbyStateChanged(boolean standbyState)
    {
        if (deviceSwitchedOn(standbyState))
        {
            readDeviceInfo();
        }
        state.standby = standbyState;
        callback.standbyChanged(standbyState); // convert from power logic to standby logic
    }

    private boolean deviceSwitchedOn(boolean standbyState)
    {
        return standbyState == false;
    }

    /**
     * device information can only be read, if the device is powered on.
     */
    private void readDeviceInfo()
    {
        device.send(Commands.INPUT_STATUS);
        device.send(Commands.VOLUME_STATUS);
    }

    public int getVolume()
    {
        if (state.volume != null)
        {
            return state.volume;
        }
        
        return 0;
    }

    @Override
    public void setStandby(boolean gotoStandybyMode)
    {
        if (!gotoStandybyMode)
        {
            device.send(Commands.POWER_ON);
        }
        else
        {
            device.send(Commands.POWER_OFF);
        }
        log.info(String.format("MA9000 at %s : set standby to %s", device.getSocketAddress().toString(), Boolean.toString(gotoStandybyMode)));
    }

    @Override
    public void setVolume(int volInPercent)
    {
        device.send(Commands.VOLUME_SET_PERCENT, volInPercent);
    }

    /**
     * Volume is delivered in percent
     */
    @Override
    public void volumeStatusChanged(int volume)
    {
        state.volume = volume;
        callback.volumeChanged(volume);
    }

    @Override
    public DeviceDriverState getCurrentState()
    {
        return state;
    }

    @Override
    public boolean getStandby()
    {
        if (state.standby != null)
        {
            return state.standby;
        }
        
        return false;
    }
}
