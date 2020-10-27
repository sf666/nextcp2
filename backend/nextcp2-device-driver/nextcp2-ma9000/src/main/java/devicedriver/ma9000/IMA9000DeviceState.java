package devicedriver.ma9000;

public interface IMA9000DeviceState
{
    /**
     * 
     * @return true: device is in standby mode, else false.
     */
    boolean getStandby();

    /**
     * Get current volume in percent
     * 
     * @return volume in percent
     */
    int getVolumeInPercent();
}
