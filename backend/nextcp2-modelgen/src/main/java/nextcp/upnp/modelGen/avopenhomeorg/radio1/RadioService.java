package nextcp.upnp.modelGen.avopenhomeorg.radio1;

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

import nextcp.upnp.GenActionException;
import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.Channel;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ChannelOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ChannelsMax;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ChannelsMaxOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.Id;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.IdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.IdArray;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.IdArrayOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.IdArrayChanged;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.IdArrayChangedOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.IdArrayChangedInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.Pause;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ProtocolInfo;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ProtocolInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.Read;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadList;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SeekSecondAbsolute;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SeekSecondRelative;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SeekSecondRelativeInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SetChannel;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SetChannelInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SetId;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SetIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.TransportStateOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class RadioService
{
    private static Logger log = LoggerFactory.getLogger(RadioService.class.getName());

    private RemoteService radioService = null;

    private UpnpService upnpService = null;

//    private RadioServiceStateVariable radioServiceStateVariable = new RadioServiceStateVariable();
    
    private RadioServiceSubscription subscription = null;
    
    public RadioService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public RadioService(UpnpService upnpService, RemoteDevice device, IRadioServiceEventListener listener)
    {
        this.upnpService = upnpService;
        radioService = device.findService(new ServiceType("av-openhome-org", "Radio"));
        if (radioService != null)
        {
	        subscription = new RadioServiceSubscription(radioService, 600);
	        if (listener != null)
	        {
	            subscription.addSubscriptionEventListener(listener);
	        }
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Radio' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Radio' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IRadioServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IRadioServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getRadioService()
    {
        return radioService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return radioService != null && radioService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public ChannelOutput channel()
    {
        if (!hasAction("Channel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Channel of service Radio");
        }
        Channel channel = new Channel(radioService,  upnpService.getControlPoint());
        ChannelOutput res = channel.executeAction();
        return res;        
    }

    public ChannelsMaxOutput channelsMax()
    {
        if (!hasAction("ChannelsMax"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ChannelsMax of service Radio");
        }
        ChannelsMax channelsMax = new ChannelsMax(radioService,  upnpService.getControlPoint());
        ChannelsMaxOutput res = channelsMax.executeAction();
        return res;        
    }

    public IdOutput id()
    {
        if (!hasAction("Id"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Id of service Radio");
        }
        Id id = new Id(radioService,  upnpService.getControlPoint());
        IdOutput res = id.executeAction();
        return res;        
    }

    public IdArrayOutput idArray()
    {
        if (!hasAction("IdArray"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action IdArray of service Radio");
        }
        IdArray idArray = new IdArray(radioService,  upnpService.getControlPoint());
        IdArrayOutput res = idArray.executeAction();
        return res;        
    }

    public IdArrayChangedOutput idArrayChanged(IdArrayChangedInput inp)
    {
        if (!hasAction("IdArrayChanged"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action IdArrayChanged of service Radio");
        }
        IdArrayChanged idArrayChanged = new IdArrayChanged(radioService, inp, upnpService.getControlPoint());
        IdArrayChangedOutput res = idArrayChanged.executeAction();
        return res;        
    }

    public void pause()
    {
        if (!hasAction("Pause"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Pause of service Radio");
        }
        Pause pause = new Pause(radioService,  upnpService.getControlPoint());
        pause.executeAction();
    }

    public void play()
    {
        if (!hasAction("Play"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Play of service Radio");
        }
        Play play = new Play(radioService,  upnpService.getControlPoint());
        play.executeAction();
    }

    public ProtocolInfoOutput protocolInfo()
    {
        if (!hasAction("ProtocolInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ProtocolInfo of service Radio");
        }
        ProtocolInfo protocolInfo = new ProtocolInfo(radioService,  upnpService.getControlPoint());
        ProtocolInfoOutput res = protocolInfo.executeAction();
        return res;        
    }

    public ReadOutput read(ReadInput inp)
    {
        if (!hasAction("Read"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Read of service Radio");
        }
        Read read = new Read(radioService, inp, upnpService.getControlPoint());
        ReadOutput res = read.executeAction();
        return res;        
    }

    public ReadListOutput readList(ReadListInput inp)
    {
        if (!hasAction("ReadList"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ReadList of service Radio");
        }
        ReadList readList = new ReadList(radioService, inp, upnpService.getControlPoint());
        ReadListOutput res = readList.executeAction();
        return res;        
    }

    public void seekSecondAbsolute(SeekSecondAbsoluteInput inp)
    {
        if (!hasAction("SeekSecondAbsolute"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekSecondAbsolute of service Radio");
        }
        SeekSecondAbsolute seekSecondAbsolute = new SeekSecondAbsolute(radioService, inp, upnpService.getControlPoint());
        seekSecondAbsolute.executeAction();
    }

    public void seekSecondRelative(SeekSecondRelativeInput inp)
    {
        if (!hasAction("SeekSecondRelative"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekSecondRelative of service Radio");
        }
        SeekSecondRelative seekSecondRelative = new SeekSecondRelative(radioService, inp, upnpService.getControlPoint());
        seekSecondRelative.executeAction();
    }

    public void setChannel(SetChannelInput inp)
    {
        if (!hasAction("SetChannel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetChannel of service Radio");
        }
        SetChannel setChannel = new SetChannel(radioService, inp, upnpService.getControlPoint());
        setChannel.executeAction();
    }

    public void setId(SetIdInput inp)
    {
        if (!hasAction("SetId"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetId of service Radio");
        }
        SetId setId = new SetId(radioService, inp, upnpService.getControlPoint());
        setId.executeAction();
    }

    public void stop()
    {
        if (!hasAction("Stop"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Stop of service Radio");
        }
        Stop stop = new Stop(radioService,  upnpService.getControlPoint());
        stop.executeAction();
    }

    public TransportStateOutput transportState()
    {
        if (!hasAction("TransportState"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action TransportState of service Radio");
        }
        TransportState transportState = new TransportState(radioService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }
}
