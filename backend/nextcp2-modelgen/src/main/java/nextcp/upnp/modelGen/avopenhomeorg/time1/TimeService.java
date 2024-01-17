package nextcp.upnp.modelGen.avopenhomeorg.time1;

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

import nextcp.upnp.modelGen.avopenhomeorg.time1.actions.Time;
import nextcp.upnp.modelGen.avopenhomeorg.time1.actions.TimeOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class TimeService
{
    private static Logger log = LoggerFactory.getLogger(TimeService.class.getName());

    private RemoteService timeService = null;

    private UpnpService upnpService = null;

    private TimeServiceStateVariable timeServiceStateVariable = new TimeServiceStateVariable();
    
    private TimeServiceSubscription subscription = null;
    
    public TimeService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        timeService = device.findService(new ServiceType("av-openhome-org", "Time"));
        if (timeService != null)
        {
	        subscription = new TimeServiceSubscription(timeService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Time' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Time' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(ITimeServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(ITimeServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getTimeService()
    {
        return timeService;
    }    


//
// Actions
// =========================================================================
//



    public TimeOutput time()
    {
        Time time = new Time(timeService,  upnpService.getControlPoint());
        TimeOutput res = time.executeAction();
        return res;        
    }
}
