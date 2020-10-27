package nextcp.devicedriver;

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
}
