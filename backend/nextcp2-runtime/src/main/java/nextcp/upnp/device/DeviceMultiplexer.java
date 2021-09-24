package nextcp.upnp.device;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import nextcp.dto.FileChangedEventDto;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

@Component
public class DeviceMultiplexer {

	@Autowired
	private DeviceRegistry deviveRegistry = null;
	
	public DeviceMultiplexer() {
	}

    @EventListener
    public void fileSystemChanged(FileChangedEventDto event)
    {
    	for (MediaServerDevice serverDevice : deviveRegistry.getAvailableMediaServer()) {
    		File f = new File(event.path);
			serverDevice.rescanFile(f);
		}
    }
}
