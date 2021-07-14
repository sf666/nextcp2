package nextcp.upnp.modelGen.samsung.com.multiScreenService;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.samsung.com.multiScreenService.actions.SendKeyCode;
import nextcp.upnp.modelGen.samsung.com.multiScreenService.actions.SendKeyCodeInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
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
