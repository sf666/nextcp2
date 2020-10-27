package nextcp.devicedriver;

import nextcp.dto.DeviceDriverState;

public interface IDeviceDriverService
{
    //
    // Methods for controlling external device
    // ====================================================================================
    //

    /**
     * Set volume in percent
     * 
     * @param vol
     *            volume in percent
     */
    public void setVolume(int vol);

    /**
     * Set standby state
     * 
     * @param standbyState
     *            true = device should go into standby. false = device should power on.
     */
    public void setStandby(boolean standbyState);

    public DeviceDriverState getCurrentState();
}
