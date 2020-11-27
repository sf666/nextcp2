package nextcp.upnp.device.mediarenderer.product;

import nextcp.devicedriver.DeviceCapabilityDto;

public class OpenHomeProductDevice extends DeviceCapabilityDto
{
    public OpenHomeProductDevice()
    {
        setDeviceDescription("UPnP OpenHome product service implementation able to control: Input Sources, Power and Volume.");
        setDeviceType("UPnP_OpenHome");
    }
}
