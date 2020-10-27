package nextcp.domainmodel.device.mediarenderer;

import java.util.Collection;

/**
 * Event notification for added or removed media renderer 
 */
public class MediaRendererListChanged
{

    public Collection<MediaRendererDevice> availableMediaRenderer = null;
    
    public MediaRendererListChanged()
    {
    }
    
    public MediaRendererListChanged(Collection<MediaRendererDevice> availableMediaRenderer)
    {
        this.availableMediaRenderer = availableMediaRenderer;
    }
}
