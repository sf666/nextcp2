package nextcp.upnp.device.mediarenderer.playlist;

import nextcp.dto.RendererPlaylist;

public class PlaylistChangedEvent
{
    public RendererPlaylist rendererPlaylist = null;

    public PlaylistChangedEvent()
    {
    }

    public PlaylistChangedEvent(RendererPlaylist rendererPlaylist)
    {
        this.rendererPlaylist = rendererPlaylist;
    }
}
