package nextcp.domainmodel.device.mediarenderer;

import nextcp.dto.DeviceDriverState;

public interface IDeviceDriver
{

    DeviceDriverState getDeviceDriverState();

    void setVolume(int vol);

    void setStandby(boolean standbyState);

}