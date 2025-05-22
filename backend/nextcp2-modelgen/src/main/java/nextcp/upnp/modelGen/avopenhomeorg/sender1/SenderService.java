package nextcp.upnp.modelGen.avopenhomeorg.sender1;

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

import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.Status;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.StatusOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.PresentationUrl;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.PresentationUrlOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.Attributes;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.AttributesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.Metadata;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.MetadataOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.Audio;
import nextcp.upnp.modelGen.avopenhomeorg.sender1.actions.AudioOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class SenderService
{
    private static Logger log = LoggerFactory.getLogger(SenderService.class.getName());

    private RemoteService senderService = null;

    private UpnpService upnpService = null;

//    private SenderServiceStateVariable senderServiceStateVariable = new SenderServiceStateVariable();
    
    private SenderServiceSubscription subscription = null;
    
    public SenderService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        senderService = device.findService(new ServiceType("av-openhome-org", "Sender"));
        if (senderService != null)
        {
	        subscription = new SenderServiceSubscription(senderService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Sender' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Sender' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(ISenderServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(ISenderServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getSenderService()
    {
        return senderService;
    }    


//
// Actions
// =========================================================================
//



    public StatusOutput status()
    {
        Status status = new Status(senderService,  upnpService.getControlPoint());
        StatusOutput res = status.executeAction();
        return res;        
    }

    public PresentationUrlOutput presentationUrl()
    {
        PresentationUrl presentationUrl = new PresentationUrl(senderService,  upnpService.getControlPoint());
        PresentationUrlOutput res = presentationUrl.executeAction();
        return res;        
    }

    public AttributesOutput attributes()
    {
        Attributes attributes = new Attributes(senderService,  upnpService.getControlPoint());
        AttributesOutput res = attributes.executeAction();
        return res;        
    }

    public MetadataOutput metadata()
    {
        Metadata metadata = new Metadata(senderService,  upnpService.getControlPoint());
        MetadataOutput res = metadata.executeAction();
        return res;        
    }

    public AudioOutput audio()
    {
        Audio audio = new Audio(senderService,  upnpService.getControlPoint());
        AudioOutput res = audio.executeAction();
        return res;        
    }
}
