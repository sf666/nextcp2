package nextcp.upnp.modelGen.lgecom.webos-second-screen;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;



/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class webos-second-screenService
{
    private static Logger log = LoggerFactory.getLogger(webos-second-screenService.class.getName());

    private RemoteService webos-second-screenService = null;

    private UpnpService upnpService = null;

    private webos-second-screenServiceStateVariable webos-second-screenServiceStateVariable = new webos-second-screenServiceStateVariable();
    
    private webos-second-screenServiceSubscription subscription = null;
    
    public webos-second-screenService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        webos-second-screenService = device.findService(new ServiceType("lge-com", "webos-second-screen"));
        if (webos-second-screenService != null)
        {
	        subscription = new webos-second-screenServiceSubscription(webos-second-screenService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'webos-second-screen' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'webos-second-screen' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(Iwebos-second-screenServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(Iwebos-second-screenServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getwebos-second-screenService()
    {
        return webos-second-screenService;
    }    

}
