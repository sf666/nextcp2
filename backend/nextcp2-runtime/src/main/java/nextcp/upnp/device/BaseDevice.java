package nextcp.upnp.device;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.support.model.DIDLContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.rest.DtoBuilder;
import nextcp.util.DidlContent;

public class BaseDevice
{
    private static final Logger log = LoggerFactory.getLogger(BaseDevice.class.getName());
    
    private RemoteDevice device;
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
        return getDevice().getIdentity().getUdn();
    }

    public String getUdnAsString()
    {
        return getDevice().getIdentity().getUdn().getIdentifierString();
    }

    public String getFriendlyName()
    {
        return getDevice().getDetails().getFriendlyName();
    }

    protected DIDLContent generateDidlContent(String didlContentXml) throws Exception
    {
        log.debug(didlContentXml);
        return didlContent.generateDidlContent(didlContentXml);
    }

}