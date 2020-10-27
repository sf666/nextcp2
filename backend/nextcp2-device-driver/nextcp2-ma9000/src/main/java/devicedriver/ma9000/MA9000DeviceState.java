package devicedriver.ma9000;

public class MA9000DeviceState implements IMA9000DeviceState
{

    private boolean standby = false;
    private int volumeInPercent = 0;

    public void setStandby(boolean standby)
    {
        this.standby = standby;
    }

    public int getVolumeInPercent()
    {
        return volumeInPercent;
    }

    public void setVolumeInPercent(int volumeInPercent)
    {
        this.volumeInPercent = volumeInPercent;
    }

    @Override
    public boolean getStandby()
    {
        return standby;
    }

}
