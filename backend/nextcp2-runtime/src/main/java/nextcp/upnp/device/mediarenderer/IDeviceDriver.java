package nextcp.upnp.device.mediarenderer;

import nextcp.dto.DeviceDriverState;
import nextcp.dto.InputSourceDto;

public interface IDeviceDriver
{
	void stop();
	
	void start();
	
    DeviceDriverState getDeviceDriverState();

    void setVolume(int vol);

    void setStandby(boolean standbyState);

    void setInput(String id);

    InputSourceDto getInput();

    int getVolume();

    boolean getStandby();

    boolean isMonitoringExternalAV();

	void setTrimBalance(Integer balance);

}