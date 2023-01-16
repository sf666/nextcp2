package nextcp.upnp.modelGen.avopenhomeorg.receiver1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.Sender;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.SenderOutput;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.ProtocolInfo;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.ProtocolInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.SetSender;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.SetSenderInput;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.receiver1.actions.TransportStateOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class ReceiverService
{
    private static Logger log = LoggerFactory.getLogger(ReceiverService.class.getName());

    private RemoteService receiverService = null;

    private UpnpService upnpService = null;

    private ReceiverServiceStateVariable receiverServiceStateVariable = new ReceiverServiceStateVariable();
    
    private ReceiverServiceSubscription subscription = null;
    
    public ReceiverService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        receiverService = device.findService(new ServiceType("av-openhome-org", "Receiver"));
        if (receiverService != null)
        {
	        subscription = new ReceiverServiceSubscription(receiverService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Receiver' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Receiver' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IReceiverServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IReceiverServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getReceiverService()
    {
        return receiverService;
    }    


    public SenderOutput sender()
    {
        Sender sender = new Sender(receiverService,  upnpService.getControlPoint());
        SenderOutput res = sender.executeAction();
        return res;        
    }

    public void play()
    {
        Play play = new Play(receiverService,  upnpService.getControlPoint());
        play.executeAction();
    }

    public void stop()
    {
        Stop stop = new Stop(receiverService,  upnpService.getControlPoint());
        stop.executeAction();
    }

    public ProtocolInfoOutput protocolInfo()
    {
        ProtocolInfo protocolInfo = new ProtocolInfo(receiverService,  upnpService.getControlPoint());
        ProtocolInfoOutput res = protocolInfo.executeAction();
        return res;        
    }

    public void setSender(SetSenderInput inp)
    {
        SetSender setSender = new SetSender(receiverService, inp, upnpService.getControlPoint());
        setSender.executeAction();
    }

    public TransportStateOutput transportState()
    {
        TransportState transportState = new TransportState(receiverService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }
}
