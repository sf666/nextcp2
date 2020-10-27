package nextcp.domainmodel.device.mediaserver;

import java.util.Collection;

public class MediaServerListChanged
{
    public Collection<MediaServerDevice> availableMediaServer = null;
    
    public MediaServerListChanged()
    {
    }
    
    public MediaServerListChanged(Collection<MediaServerDevice> availableMediaServer)
    {
        this.availableMediaServer = availableMediaServer;
    }
    
}
