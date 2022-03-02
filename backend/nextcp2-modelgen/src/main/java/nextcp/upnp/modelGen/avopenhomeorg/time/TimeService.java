package nextcp.upnp.modelGen.avopenhomeorg.time;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.time.actions.Time;
import nextcp.upnp.modelGen.avopenhomeorg.time.actions.TimeOutput;


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


    public TimeOutput time()
    {
        Time time = new Time(timeService,  upnpService.getControlPoint());
        TimeOutput res = time.executeAction();
        return res;        
    }
}
