package nextcp.upnp.modelGen.avopenhomeorg.transport1;

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

import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModeInfo;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModeInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Modes;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Pause;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.PlayAs;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.PlayAsInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Repeat;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.RepeatOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondAbsolute;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondRelative;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondRelativeInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetRepeat;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetRepeatInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetShuffle;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetShuffleInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Shuffle;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ShuffleOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SkipNext;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SkipPrevious;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamId;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamIdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamInfo;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.TransportStateOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class TransportService
{
    private static Logger log = LoggerFactory.getLogger(TransportService.class.getName());

    private RemoteService transportService = null;

    private UpnpService upnpService = null;

//    private TransportServiceStateVariable transportServiceStateVariable = new TransportServiceStateVariable();
    
    private TransportServiceSubscription subscription = null;
    
    public TransportService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public TransportService(UpnpService upnpService, RemoteDevice device, ITransportServiceEventListener listener)
    {
        this.upnpService = upnpService;
        transportService = device.findService(new ServiceType("av-openhome-org", "Transport"));
        if (transportService != null)
        {
	        subscription = new TransportServiceSubscription(transportService, 600);
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
	
	        log.info(String.format("initialized service 'Transport' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Transport' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(ITransportServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(ITransportServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getTransportService()
    {
        return transportService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return transportService != null && transportService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public ModeInfoOutput modeInfo()
    {
        if (!hasAction("ModeInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ModeInfo of service Transport");
        }
        ModeInfo modeInfo = new ModeInfo(transportService,  upnpService.getControlPoint());
        ModeInfoOutput res = modeInfo.executeAction();
        return res;        
    }

    public ModesOutput modes()
    {
        if (!hasAction("Modes"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Modes of service Transport");
        }
        Modes modes = new Modes(transportService,  upnpService.getControlPoint());
        ModesOutput res = modes.executeAction();
        return res;        
    }

    public void pause()
    {
        if (!hasAction("Pause"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Pause of service Transport");
        }
        Pause pause = new Pause(transportService,  upnpService.getControlPoint());
        pause.executeAction();
    }

    public void play()
    {
        if (!hasAction("Play"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Play of service Transport");
        }
        Play play = new Play(transportService,  upnpService.getControlPoint());
        play.executeAction();
    }

    public void playAs(PlayAsInput inp)
    {
        if (!hasAction("PlayAs"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action PlayAs of service Transport");
        }
        PlayAs playAs = new PlayAs(transportService, inp, upnpService.getControlPoint());
        playAs.executeAction();
    }

    public RepeatOutput repeat()
    {
        if (!hasAction("Repeat"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Repeat of service Transport");
        }
        Repeat repeat = new Repeat(transportService,  upnpService.getControlPoint());
        RepeatOutput res = repeat.executeAction();
        return res;        
    }

    public void seekSecondAbsolute(SeekSecondAbsoluteInput inp)
    {
        if (!hasAction("SeekSecondAbsolute"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekSecondAbsolute of service Transport");
        }
        SeekSecondAbsolute seekSecondAbsolute = new SeekSecondAbsolute(transportService, inp, upnpService.getControlPoint());
        seekSecondAbsolute.executeAction();
    }

    public void seekSecondRelative(SeekSecondRelativeInput inp)
    {
        if (!hasAction("SeekSecondRelative"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekSecondRelative of service Transport");
        }
        SeekSecondRelative seekSecondRelative = new SeekSecondRelative(transportService, inp, upnpService.getControlPoint());
        seekSecondRelative.executeAction();
    }

    public void setRepeat(SetRepeatInput inp)
    {
        if (!hasAction("SetRepeat"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetRepeat of service Transport");
        }
        SetRepeat setRepeat = new SetRepeat(transportService, inp, upnpService.getControlPoint());
        setRepeat.executeAction();
    }

    public void setShuffle(SetShuffleInput inp)
    {
        if (!hasAction("SetShuffle"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetShuffle of service Transport");
        }
        SetShuffle setShuffle = new SetShuffle(transportService, inp, upnpService.getControlPoint());
        setShuffle.executeAction();
    }

    public ShuffleOutput shuffle()
    {
        if (!hasAction("Shuffle"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Shuffle of service Transport");
        }
        Shuffle shuffle = new Shuffle(transportService,  upnpService.getControlPoint());
        ShuffleOutput res = shuffle.executeAction();
        return res;        
    }

    public void skipNext()
    {
        if (!hasAction("SkipNext"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SkipNext of service Transport");
        }
        SkipNext skipNext = new SkipNext(transportService,  upnpService.getControlPoint());
        skipNext.executeAction();
    }

    public void skipPrevious()
    {
        if (!hasAction("SkipPrevious"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SkipPrevious of service Transport");
        }
        SkipPrevious skipPrevious = new SkipPrevious(transportService,  upnpService.getControlPoint());
        skipPrevious.executeAction();
    }

    public void stop()
    {
        if (!hasAction("Stop"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Stop of service Transport");
        }
        Stop stop = new Stop(transportService,  upnpService.getControlPoint());
        stop.executeAction();
    }

    public StreamIdOutput streamId()
    {
        if (!hasAction("StreamId"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StreamId of service Transport");
        }
        StreamId streamId = new StreamId(transportService,  upnpService.getControlPoint());
        StreamIdOutput res = streamId.executeAction();
        return res;        
    }

    public StreamInfoOutput streamInfo()
    {
        if (!hasAction("StreamInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action StreamInfo of service Transport");
        }
        StreamInfo streamInfo = new StreamInfo(transportService,  upnpService.getControlPoint());
        StreamInfoOutput res = streamInfo.executeAction();
        return res;        
    }

    public TransportStateOutput transportState()
    {
        if (!hasAction("TransportState"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action TransportState of service Transport");
        }
        TransportState transportState = new TransportState(transportService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }
}
