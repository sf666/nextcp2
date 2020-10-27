package devicedriver.ma9000;

import nextcp.devicedriver.IDeviceCapabilities;

public class Ma9000Capabilities implements IDeviceCapabilities
{

    @Override
    public String getDeviceDescription()
    {
        return "Device driver for MA9000. This driver requires a TCP/IP to RS232 transceiver (converter).";
    }

    @Override
    public String getDeviceType()
    {
        return "MA9000";
    }

}
