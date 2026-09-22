package nextcp.upnp.modelGen.avopenhomeorg.playlist1;

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

import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.DeleteAll;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.DeleteId;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.DeleteIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Id;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdArray;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdArrayOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdArrayChanged;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdArrayChangedOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdArrayChangedInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Insert;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.InsertOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.InsertInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Next;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Pause;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Previous;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ProtocolInfo;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ProtocolInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Read;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadList;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Repeat;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.RepeatOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekId;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekIndex;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekIndexInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekSecondAbsolute;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekSecondRelative;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekSecondRelativeInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SetRepeat;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SetRepeatInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SetShuffle;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SetShuffleInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Shuffle;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ShuffleOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.TracksMax;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.TracksMaxOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.TransportStateOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class PlaylistService
{
    private static Logger log = LoggerFactory.getLogger(PlaylistService.class.getName());

    private RemoteService playlistService = null;

    private UpnpService upnpService = null;

//    private PlaylistServiceStateVariable playlistServiceStateVariable = new PlaylistServiceStateVariable();
    
    private PlaylistServiceSubscription subscription = null;
    
    public PlaylistService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public PlaylistService(UpnpService upnpService, RemoteDevice device, IPlaylistServiceEventListener listener)
    {
        this.upnpService = upnpService;
        playlistService = device.findService(new ServiceType("av-openhome-org", "Playlist"));
        if (playlistService != null)
        {
	        subscription = new PlaylistServiceSubscription(playlistService, 600);
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
	
	        log.info(String.format("initialized service 'Playlist' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Playlist' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IPlaylistServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IPlaylistServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getPlaylistService()
    {
        return playlistService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return playlistService != null && playlistService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public void deleteAll()
    {
        if (!hasAction("DeleteAll"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DeleteAll of service Playlist");
        }
        DeleteAll deleteAll = new DeleteAll(playlistService,  upnpService.getControlPoint());
        deleteAll.executeAction();
    }

    public void deleteId(DeleteIdInput inp)
    {
        if (!hasAction("DeleteId"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DeleteId of service Playlist");
        }
        DeleteId deleteId = new DeleteId(playlistService, inp, upnpService.getControlPoint());
        deleteId.executeAction();
    }

    public IdOutput id()
    {
        if (!hasAction("Id"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Id of service Playlist");
        }
        Id id = new Id(playlistService,  upnpService.getControlPoint());
        IdOutput res = id.executeAction();
        return res;        
    }

    public IdArrayOutput idArray()
    {
        if (!hasAction("IdArray"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action IdArray of service Playlist");
        }
        IdArray idArray = new IdArray(playlistService,  upnpService.getControlPoint());
        IdArrayOutput res = idArray.executeAction();
        return res;        
    }

    public IdArrayChangedOutput idArrayChanged(IdArrayChangedInput inp)
    {
        if (!hasAction("IdArrayChanged"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action IdArrayChanged of service Playlist");
        }
        IdArrayChanged idArrayChanged = new IdArrayChanged(playlistService, inp, upnpService.getControlPoint());
        IdArrayChangedOutput res = idArrayChanged.executeAction();
        return res;        
    }

    public InsertOutput insert(InsertInput inp)
    {
        if (!hasAction("Insert"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Insert of service Playlist");
        }
        Insert insert = new Insert(playlistService, inp, upnpService.getControlPoint());
        InsertOutput res = insert.executeAction();
        return res;        
    }

    public void next()
    {
        if (!hasAction("Next"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Next of service Playlist");
        }
        Next next = new Next(playlistService,  upnpService.getControlPoint());
        next.executeAction();
    }

    public void pause()
    {
        if (!hasAction("Pause"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Pause of service Playlist");
        }
        Pause pause = new Pause(playlistService,  upnpService.getControlPoint());
        pause.executeAction();
    }

    public void play()
    {
        if (!hasAction("Play"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Play of service Playlist");
        }
        Play play = new Play(playlistService,  upnpService.getControlPoint());
        play.executeAction();
    }

    public void previous()
    {
        if (!hasAction("Previous"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Previous of service Playlist");
        }
        Previous previous = new Previous(playlistService,  upnpService.getControlPoint());
        previous.executeAction();
    }

    public ProtocolInfoOutput protocolInfo()
    {
        if (!hasAction("ProtocolInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ProtocolInfo of service Playlist");
        }
        ProtocolInfo protocolInfo = new ProtocolInfo(playlistService,  upnpService.getControlPoint());
        ProtocolInfoOutput res = protocolInfo.executeAction();
        return res;        
    }

    public ReadOutput read(ReadInput inp)
    {
        if (!hasAction("Read"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Read of service Playlist");
        }
        Read read = new Read(playlistService, inp, upnpService.getControlPoint());
        ReadOutput res = read.executeAction();
        return res;        
    }

    public ReadListOutput readList(ReadListInput inp)
    {
        if (!hasAction("ReadList"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ReadList of service Playlist");
        }
        ReadList readList = new ReadList(playlistService, inp, upnpService.getControlPoint());
        ReadListOutput res = readList.executeAction();
        return res;        
    }

    public RepeatOutput repeat()
    {
        if (!hasAction("Repeat"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Repeat of service Playlist");
        }
        Repeat repeat = new Repeat(playlistService,  upnpService.getControlPoint());
        RepeatOutput res = repeat.executeAction();
        return res;        
    }

    public void seekId(SeekIdInput inp)
    {
        if (!hasAction("SeekId"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekId of service Playlist");
        }
        SeekId seekId = new SeekId(playlistService, inp, upnpService.getControlPoint());
        seekId.executeAction();
    }

    public void seekIndex(SeekIndexInput inp)
    {
        if (!hasAction("SeekIndex"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekIndex of service Playlist");
        }
        SeekIndex seekIndex = new SeekIndex(playlistService, inp, upnpService.getControlPoint());
        seekIndex.executeAction();
    }

    public void seekSecondAbsolute(SeekSecondAbsoluteInput inp)
    {
        if (!hasAction("SeekSecondAbsolute"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekSecondAbsolute of service Playlist");
        }
        SeekSecondAbsolute seekSecondAbsolute = new SeekSecondAbsolute(playlistService, inp, upnpService.getControlPoint());
        seekSecondAbsolute.executeAction();
    }

    public void seekSecondRelative(SeekSecondRelativeInput inp)
    {
        if (!hasAction("SeekSecondRelative"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SeekSecondRelative of service Playlist");
        }
        SeekSecondRelative seekSecondRelative = new SeekSecondRelative(playlistService, inp, upnpService.getControlPoint());
        seekSecondRelative.executeAction();
    }

    public void setRepeat(SetRepeatInput inp)
    {
        if (!hasAction("SetRepeat"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetRepeat of service Playlist");
        }
        SetRepeat setRepeat = new SetRepeat(playlistService, inp, upnpService.getControlPoint());
        setRepeat.executeAction();
    }

    public void setShuffle(SetShuffleInput inp)
    {
        if (!hasAction("SetShuffle"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetShuffle of service Playlist");
        }
        SetShuffle setShuffle = new SetShuffle(playlistService, inp, upnpService.getControlPoint());
        setShuffle.executeAction();
    }

    public ShuffleOutput shuffle()
    {
        if (!hasAction("Shuffle"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Shuffle of service Playlist");
        }
        Shuffle shuffle = new Shuffle(playlistService,  upnpService.getControlPoint());
        ShuffleOutput res = shuffle.executeAction();
        return res;        
    }

    public void stop()
    {
        if (!hasAction("Stop"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Stop of service Playlist");
        }
        Stop stop = new Stop(playlistService,  upnpService.getControlPoint());
        stop.executeAction();
    }

    public TracksMaxOutput tracksMax()
    {
        if (!hasAction("TracksMax"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action TracksMax of service Playlist");
        }
        TracksMax tracksMax = new TracksMax(playlistService,  upnpService.getControlPoint());
        TracksMaxOutput res = tracksMax.executeAction();
        return res;        
    }

    public TransportStateOutput transportState()
    {
        if (!hasAction("TransportState"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action TransportState of service Playlist");
        }
        TransportState transportState = new TransportState(playlistService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }
}
