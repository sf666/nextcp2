package nextcp.upnp.modelGen.avopenhomeorg.info;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.info.actions.Details;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.DetailsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.Counters;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.CountersOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.Metatext;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.MetatextOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.Track;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.TrackOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class InfoService
{
    private static Logger log = LoggerFactory.getLogger(InfoService.class.getName());

    private RemoteService infoService = null;

    private UpnpService upnpService = null;

    private InfoServiceStateVariable infoServiceStateVariable = new InfoServiceStateVariable();
    
    private InfoServiceSubscription subscription = null;
    
    public InfoService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        infoService = device.findService(new ServiceType("av-openhome-org", "Info"));
        if (infoService != null)
        {
	        subscription = new InfoServiceSubscription(infoService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Info' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Info' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IInfoServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IInfoServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getInfoService()
    {
        return infoService;
    }    


    public DetailsOutput details()
    {
        Details details = new Details(infoService,  upnpService.getControlPoint());
        DetailsOutput res = details.executeAction();
        return res;        
    }

    public CountersOutput counters()
    {
        Counters counters = new Counters(infoService,  upnpService.getControlPoint());
        CountersOutput res = counters.executeAction();
        return res;        
    }

    public MetatextOutput metatext()
    {
        Metatext metatext = new Metatext(infoService,  upnpService.getControlPoint());
        MetatextOutput res = metatext.executeAction();
        return res;        
    }

    public TrackOutput track()
    {
        Track track = new Track(infoService,  upnpService.getControlPoint());
        TrackOutput res = track.executeAction();
        return res;        
    }
}
