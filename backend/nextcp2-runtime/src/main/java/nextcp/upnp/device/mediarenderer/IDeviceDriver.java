package nextcp.upnp.device.mediarenderer;

import nextcp.dto.DeviceDriverState;

public interface IDeviceDriver
{

    DeviceDriverState getDeviceDriverState();

    void setVolume(int vol);

    void setStandby(boolean standbyState);

    int getVolume();

    boolean getStandby();

    boolean isMonitoringExternalAV();
    
    
}