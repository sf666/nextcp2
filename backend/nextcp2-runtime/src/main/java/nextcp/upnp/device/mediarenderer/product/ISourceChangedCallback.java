package nextcp.upnp.device.mediarenderer.product;

import nextcp.dto.InputSourceDto;

public interface ISourceChangedCallback
{
    public void sourceChanged(InputSourceDto source);
}
