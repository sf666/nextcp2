package nextcp.upnp.modelGen.avopenhomeorg.sender;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.Status;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.StatusOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.PresentationUrl;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.PresentationUrlOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.Attributes;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.AttributesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.Metadata;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.MetadataOutput;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.Audio;
import nextcp.upnp.modelGen.avopenhomeorg.sender.actions.AudioOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class SenderService
{
    private static Logger log = LoggerFactory.getLogger(SenderService.class.getName());

    private RemoteService senderService = null;

    private UpnpService upnpService = null;

    private SenderServiceStateVariable senderServiceStateVariable = new SenderServiceStateVariable();
    
    private SenderServiceSubscription subscription = null;
    
    public SenderService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        senderService = device.findService(new ServiceType("av-openhome-org", "Sender"));
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
    
    public void addSubscriptionEventListener(ISenderServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(ISenderServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getSenderService()
    {
        return senderService;
    }    


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
