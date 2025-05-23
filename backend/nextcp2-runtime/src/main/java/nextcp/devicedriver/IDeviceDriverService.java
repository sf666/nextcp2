package nextcp.devicedriver;

import nextcp.dto.DeviceDriverState;
import nextcp.dto.InputSourceDto;

public interface IDeviceDriverService
{
	//
	// Lifecycle events
	//
	
	public void start();
	
	public void stop();
	
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

    public int getVolume();

    /**
     * Set standby state
     * 
     * @param standbyState
     *            true = device should go into standby. false = device should power on.
     */
    public boolean getStandby();

    public DeviceDriverState getCurrentState();

    public void setInput(String input);

    public InputSourceDto getInput();

	public void setTrimBalance(Integer balance);
}
