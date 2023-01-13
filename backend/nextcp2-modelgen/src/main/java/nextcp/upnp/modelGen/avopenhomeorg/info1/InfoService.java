package nextcp.upnp.modelGen.avopenhomeorg.info1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.Details;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.DetailsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.Counters;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.CountersOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.Metatext;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.MetatextOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.Track;
import nextcp.upnp.modelGen.avopenhomeorg.info1.actions.TrackOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
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
