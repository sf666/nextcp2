package nextcp.devicedriver;

import nextcp.dto.InputSourceDto;

/**
 * <pre>
 * Callbacks from external device
 * ====================================================================================
 * </pre>
 */
public interface IDeviceDriverCallback
{
    /**
     * External volume changed
     * 
     * @param vol
     *            new device volume
     * 
     */
    public void volumeChanged(int vol);

    /**
     * Standby changed
     * 
     * @param standbyState
     *            new Standy state. true = device is in standby. false = device is power on.
     */
    public void standbyChanged(boolean standbyState);
    
    public void inputChanged(InputSourceDto input);
}
