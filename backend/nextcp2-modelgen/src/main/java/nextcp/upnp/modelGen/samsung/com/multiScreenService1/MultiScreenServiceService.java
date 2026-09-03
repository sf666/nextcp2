package nextcp.upnp.modelGen.samsung.com.multiScreenService1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingRenewal;
import org.jupnp.protocol.sync.SendingSubscribe;
import org.jupnp.protocol.sync.SendingUnsubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.samsung.com.multiScreenService1.actions.SendKeyCode;
import nextcp.upnp.modelGen.samsung.com.multiScreenService1.actions.SendKeyCodeInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class MultiScreenServiceService
{
    private static Logger log = LoggerFactory.getLogger(MultiScreenServiceService.class.getName());

    private RemoteService multiScreenServiceService = null;

    private UpnpService upnpService = null;

//    private MultiScreenServiceServiceStateVariable multiScreenServiceServiceStateVariable = new MultiScreenServiceServiceStateVariable();
    
    private MultiScreenServiceServiceSubscription subscription = null;
    
    public MultiScreenServiceService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public MultiScreenServiceService(UpnpService upnpService, RemoteDevice device, IMultiScreenServiceServiceEventListener listener)
    {
        this.upnpService = upnpService;
        multiScreenServiceService = device.findService(new ServiceType("samsung.com", "MultiScreenService"));
        if (multiScreenServiceService != null)
        {
	        subscription = new MultiScreenServiceServiceSubscription(multiScreenServiceService, 600);
	        if (listener != null)
	        {
	            subscription.addSubscriptionEventListener(listener);
	        }
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'MultiScreenService' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'MultiScreenService' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }

    public void unsubscribeService(UpnpService upnpService, RemoteDevice device)
    {
        SendingUnsubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingUnsubscribe(subscription);
        protocol.run();
    }

    public void renewService(UpnpService upnpService, RemoteDevice device)
    {
        SendingRenewal protocol = upnpService.getControlPoint().getProtocolFactory().createSendingRenewal(subscription);
        protocol.run();
    }

    public void addSubscriptionEventListener(IMultiScreenServiceServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IMultiScreenServiceServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getMultiScreenServiceService()
    {
        return multiScreenServiceService;
    }    


//
// Actions
// =========================================================================
//



    public void sendKeyCode(SendKeyCodeInput inp)
    {
        SendKeyCode sendKeyCode = new SendKeyCode(multiScreenServiceService, inp, upnpService.getControlPoint());
        sendKeyCode.executeAction();
    }
}
