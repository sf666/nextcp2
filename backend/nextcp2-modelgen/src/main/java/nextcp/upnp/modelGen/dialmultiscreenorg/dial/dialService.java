package nextcp.upnp.modelGen.dialmultiscreenorg.dial;

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
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class dialService
{
    private static Logger log = LoggerFactory.getLogger(dialService.class.getName());

    private RemoteService dialService = null;

    private UpnpService upnpService = null;

    private dialServiceStateVariable dialServiceStateVariable = new dialServiceStateVariable();
    
    private dialServiceSubscription subscription = null;
    
    public dialService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        dialService = device.findService(new ServiceType("dial-multiscreen-org", "dial"));
        subscription = new dialServiceSubscription(dialService, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service 'dial' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(IdialServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IdialServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getdialService()
    {
        return dialService;
    }    

}
