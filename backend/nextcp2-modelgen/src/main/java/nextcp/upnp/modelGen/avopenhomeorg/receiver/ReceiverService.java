package nextcp.upnp.modelGen.avopenhomeorg.receiver;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.Sender;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.SenderOutput;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.SetSender;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.SetSenderInput;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.ProtocolInfo;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.ProtocolInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.receiver.actions.TransportStateOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
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

    public void setSender(SetSenderInput inp)
    {
        SetSender setSender = new SetSender(receiverService, inp, upnpService.getControlPoint());
        setSender.executeAction();
    }

    public ProtocolInfoOutput protocolInfo()
    {
        ProtocolInfo protocolInfo = new ProtocolInfo(receiverService,  upnpService.getControlPoint());
        ProtocolInfoOutput res = protocolInfo.executeAction();
        return res;        
    }

    public TransportStateOutput transportState()
    {
        TransportState transportState = new TransportState(receiverService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }
}
