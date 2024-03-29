package devicedriver.McIntosh.tcp;

public interface IMcIntoshDeviceChanged
{
    /**
     * true = power on
     * 
     * @param on
     */
    public void standbyStateChanged(boolean on);

    /**
     * Vol in %
     * @param input
     */
    public void volumeStatusChanged(int volInPercent);
    
    public void inputChanged(String input);
    
    public void trimBalanceChanged(int balance);
    public void trimInputChanged(float balance);
    public void trimEqChanged(boolean balance);
}
