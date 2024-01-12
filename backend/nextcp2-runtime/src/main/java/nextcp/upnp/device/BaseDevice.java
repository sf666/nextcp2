package nextcp.upnp.device;

import org.apache.commons.lang.StringUtils;
import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.types.UDN;
import org.jupnp.support.model.DIDLContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import nextcp.rest.DtoBuilder;
import nextcp.service.upnp.RemoteDeviceFacade;
import nextcp.util.DidlContent;

public class BaseDevice
{
    private static final Logger log = LoggerFactory.getLogger(BaseDevice.class.getName());

    private RemoteDevice device;
    private RemoteDeviceFacade deviceFacade = new RemoteDeviceFacade();
    
    private DidlContent didlContent = new DidlContent();

    @Autowired
    private UpnpService upnpService;

    @Autowired
    private ApplicationEventPublisher eventPublisher = null;

    @Autowired
    private DtoBuilder dtoBuilder = null;

    public BaseDevice(RemoteDevice device)
    {
        this.device = device;
    }

    public UpnpService getUpnpService()
    {
        return upnpService;
    }

    public RemoteDevice getDevice()
    {
        return device;
    }

    public DtoBuilder getDtoBuilder()
    {
        return dtoBuilder;
    }

    public ApplicationEventPublisher getEventPublisher()
    {
        return eventPublisher;
    }

    protected String getDefault(String in, String defaultValue)
    {
        if (StringUtils.isBlank(in))
        {
            return defaultValue;
        }
        return in;
    }

    protected Long getDefault(Long in, Long defaultValue)
    {
        if (in == null)
        {
            return defaultValue;
        }
        return in;
    }

    public UDN getUDN()
    {
        return deviceFacade.getUDN(device);
    }

    public String getUdnAsString()
    {
        return deviceFacade.getUdnAsString(device);
    }

    public String getFriendlyName()
    {
        return deviceFacade.getFriendlyName(device);
    }

    protected DIDLContent generateDidlContent(String didlContentXml) throws Exception
    {
        log.debug(didlContentXml);
        return didlContent.generateDidlContent(didlContentXml);
    }

}