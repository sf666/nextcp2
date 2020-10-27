package nextcp.devicedriver;

public class DeviceCapabilityDto implements IDeviceCapabilities
{

    private String deviceType = "";

    private String deviceDescription = "";

    public DeviceCapabilityDto()
    {
    }

    public DeviceCapabilityDto(IDeviceDriverFactory fac)
    {
        this.deviceDescription = fac.getDriverCapabilities().getDeviceDescription();
        this.deviceType = fac.getDriverCapabilities().getDeviceType();
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getDeviceDescription()
    {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription)
    {
        this.deviceDescription = deviceDescription;
    }
}
