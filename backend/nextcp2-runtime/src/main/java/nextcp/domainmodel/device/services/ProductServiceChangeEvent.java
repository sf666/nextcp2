package nextcp.domainmodel.device.services;

import nextcp.dto.InputSourceDto;

public class ProductServiceChangeEvent
{
    public String mediaServerUdn = null;
    public InputSourceDto currentInputSource = null;

    public ProductServiceChangeEvent(String mediaServerUdn, InputSourceDto currentInputSource)
    {
        super();
        this.mediaServerUdn = mediaServerUdn;
        this.currentInputSource = currentInputSource;
    }
}
