package nextcp.upnp.modelGen.avopenhomeorg.transport1;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Modes;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetShuffle;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetShuffleInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamId;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamIdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Pause;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Shuffle;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ShuffleOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.PlayAs;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.PlayAsInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SkipNext;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Repeat;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.RepeatOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.TransportStateOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondAbsolute;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModeInfo;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.ModeInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetRepeat;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SetRepeatInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamInfo;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.StreamInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondRelative;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SeekSecondRelativeInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.SkipPrevious;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class TransportService
{
    private static Logger log = LoggerFactory.getLogger(TransportService.class.getName());

    private RemoteService transportService = null;

    private UpnpService upnpService = null;

    private TransportServiceStateVariable transportServiceStateVariable = new TransportServiceStateVariable();
    
    private TransportServiceSubscription subscription = null;
    
    public TransportService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        transportService = device.findService(new ServiceType("av-openhome-org", "Transport"));
        if (transportService != null)
        {
	        subscription = new TransportServiceSubscription(transportService, 600);
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
    
    public void addSubscriptionEventListener(ITransportServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(ITransportServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getTransportService()
    {
        return transportService;
    }    


    public ModesOutput modes()
    {
        Modes modes = new Modes(transportService,  upnpService.getControlPoint());
        ModesOutput res = modes.executeAction();
        return res;        
    }

    public void setShuffle(SetShuffleInput inp)
    {
        SetShuffle setShuffle = new SetShuffle(transportService, inp, upnpService.getControlPoint());
        setShuffle.executeAction();
    }

    public StreamIdOutput streamId()
    {
        StreamId streamId = new StreamId(transportService,  upnpService.getControlPoint());
        StreamIdOutput res = streamId.executeAction();
        return res;        
    }

    public void pause()
    {
        Pause pause = new Pause(transportService,  upnpService.getControlPoint());
        pause.executeAction();
    }

    public ShuffleOutput shuffle()
    {
        Shuffle shuffle = new Shuffle(transportService,  upnpService.getControlPoint());
        ShuffleOutput res = shuffle.executeAction();
        return res;        
    }

    public void playAs(PlayAsInput inp)
    {
        PlayAs playAs = new PlayAs(transportService, inp, upnpService.getControlPoint());
        playAs.executeAction();
    }

    public void stop()
    {
        Stop stop = new Stop(transportService,  upnpService.getControlPoint());
        stop.executeAction();
    }

    public void skipNext()
    {
        SkipNext skipNext = new SkipNext(transportService,  upnpService.getControlPoint());
        skipNext.executeAction();
    }

    public RepeatOutput repeat()
    {
        Repeat repeat = new Repeat(transportService,  upnpService.getControlPoint());
        RepeatOutput res = repeat.executeAction();
        return res;        
    }

    public TransportStateOutput transportState()
    {
        TransportState transportState = new TransportState(transportService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }

    public void seekSecondAbsolute(SeekSecondAbsoluteInput inp)
    {
        SeekSecondAbsolute seekSecondAbsolute = new SeekSecondAbsolute(transportService, inp, upnpService.getControlPoint());
        seekSecondAbsolute.executeAction();
    }

    public void play()
    {
        Play play = new Play(transportService,  upnpService.getControlPoint());
        play.executeAction();
    }

    public ModeInfoOutput modeInfo()
    {
        ModeInfo modeInfo = new ModeInfo(transportService,  upnpService.getControlPoint());
        ModeInfoOutput res = modeInfo.executeAction();
        return res;        
    }

    public void setRepeat(SetRepeatInput inp)
    {
        SetRepeat setRepeat = new SetRepeat(transportService, inp, upnpService.getControlPoint());
        setRepeat.executeAction();
    }

    public StreamInfoOutput streamInfo()
    {
        StreamInfo streamInfo = new StreamInfo(transportService,  upnpService.getControlPoint());
        StreamInfoOutput res = streamInfo.executeAction();
        return res;        
    }

    public void seekSecondRelative(SeekSecondRelativeInput inp)
    {
        SeekSecondRelative seekSecondRelative = new SeekSecondRelative(transportService, inp, upnpService.getControlPoint());
        seekSecondRelative.executeAction();
    }

    public void skipPrevious()
    {
        SkipPrevious skipPrevious = new SkipPrevious(transportService,  upnpService.getControlPoint());
        skipPrevious.executeAction();
    }
}
