package nextcp.upnp.modelGen.samsung.com.multiScreenService1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

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
 * Generated UPnP Service class for calling Actions synchroniously.  
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
        this.upnpService = upnpService;
        multiScreenServiceService = device.findService(new ServiceType("samsung.com", "MultiScreenService"));
        if (multiScreenServiceService != null)
        {
	        subscription = new MultiScreenServiceServiceSubscription(multiScreenServiceService, 600);
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
    
    public void addSubscriptionEventListener(IMultiScreenServiceServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IMultiScreenServiceServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getMultiScreenServiceService()
    {
        return multiScreenServiceService;
    }    


    public void sendKeyCode(SendKeyCodeInput inp)
    {
        SendKeyCode sendKeyCode = new SendKeyCode(multiScreenServiceService, inp, upnpService.getControlPoint());
        sendKeyCode.executeAction();
    }
}
