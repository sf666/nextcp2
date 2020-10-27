package nextcp.devicedriver;

public interface IDeviceDriverFactory
{
    public IDeviceCapabilities getDriverCapabilities();
    public IDeviceDriverService getDriverFor(String connectionString, IDeviceDriverCallback callback, String rendererUdn);
    public String getTargetUsageDescription();
}
