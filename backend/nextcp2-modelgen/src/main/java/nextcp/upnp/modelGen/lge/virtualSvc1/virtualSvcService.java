package nextcp.upnp.modelGen.lge.virtualSvc1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

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
public class virtualSvcService
{
    private static Logger log = LoggerFactory.getLogger(virtualSvcService.class.getName());

    private RemoteService virtualSvcService = null;

    private UpnpService upnpService = null;

    private virtualSvcServiceStateVariable virtualSvcServiceStateVariable = new virtualSvcServiceStateVariable();
    
    private virtualSvcServiceSubscription subscription = null;
    
    public virtualSvcService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        virtualSvcService = device.findService(new ServiceType("lge", "virtualSvc"));
        if (virtualSvcService != null)
        {
	        subscription = new virtualSvcServiceSubscription(virtualSvcService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'virtualSvc' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'virtualSvc' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IvirtualSvcServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IvirtualSvcServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getvirtualSvcService()
    {
        return virtualSvcService;
    }    

}
