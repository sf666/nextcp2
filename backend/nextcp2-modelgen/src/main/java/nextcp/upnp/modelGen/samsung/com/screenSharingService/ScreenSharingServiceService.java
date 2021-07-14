package nextcp.upnp.modelGen.samsung.com.screenSharingService;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.samsung.com.screenSharingService.actions.X_ConnectScreenSharingM2TV;
import nextcp.upnp.modelGen.samsung.com.screenSharingService.actions.X_ConnectScreenSharingM2TVOutput;
import nextcp.upnp.modelGen.samsung.com.screenSharingService.actions.X_ConnectScreenSharingM2TVInput;
import nextcp.upnp.modelGen.samsung.com.screenSharingService.actions.X_ConnectScreenSharingTV2M;
import nextcp.upnp.modelGen.samsung.com.screenSharingService.actions.X_ConnectScreenSharingTV2MOutput;
import nextcp.upnp.modelGen.samsung.com.screenSharingService.actions.X_ConnectScreenSharingTV2MInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class ScreenSharingServiceService
{
    private static Logger log = LoggerFactory.getLogger(ScreenSharingServiceService.class.getName());

    private RemoteService screenSharingServiceService = null;

    private UpnpService upnpService = null;

    private ScreenSharingServiceServiceStateVariable screenSharingServiceServiceStateVariable = new ScreenSharingServiceServiceStateVariable();
    
    private ScreenSharingServiceServiceSubscription subscription = null;
    
    public ScreenSharingServiceService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        screenSharingServiceService = device.findService(new ServiceType("samsung.com", "ScreenSharingService"));
        subscription = new ScreenSharingServiceServiceSubscription(screenSharingServiceService, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service 'ScreenSharingService' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(IScreenSharingServiceServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IScreenSharingServiceServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getScreenSharingServiceService()
    {
        return screenSharingServiceService;
    }    


    public X_ConnectScreenSharingM2TVOutput x_ConnectScreenSharingM2TV(X_ConnectScreenSharingM2TVInput inp)
    {
        X_ConnectScreenSharingM2TV x_ConnectScreenSharingM2TV = new X_ConnectScreenSharingM2TV(screenSharingServiceService, inp, upnpService.getControlPoint());
        X_ConnectScreenSharingM2TVOutput res = x_ConnectScreenSharingM2TV.executeAction();
        return res;        
    }

    public X_ConnectScreenSharingTV2MOutput x_ConnectScreenSharingTV2M(X_ConnectScreenSharingTV2MInput inp)
    {
        X_ConnectScreenSharingTV2M x_ConnectScreenSharingTV2M = new X_ConnectScreenSharingTV2M(screenSharingServiceService, inp, upnpService.getControlPoint());
        X_ConnectScreenSharingTV2MOutput res = x_ConnectScreenSharingTV2M.executeAction();
        return res;        
    }
}
