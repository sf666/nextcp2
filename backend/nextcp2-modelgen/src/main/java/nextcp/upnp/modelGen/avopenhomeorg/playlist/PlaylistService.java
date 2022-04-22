package nextcp.upnp.modelGen.avopenhomeorg.playlist;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.IdArrayChanged;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.IdArrayChangedOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.IdArrayChangedInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SetShuffle;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SetShuffleInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Pause;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.TracksMax;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.TracksMaxOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Shuffle;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ShuffleOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Stop;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.DeleteAll;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Repeat;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.RepeatOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.IdArray;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.IdArrayOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Insert;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.InsertOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.InsertInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.TransportState;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.TransportStateOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekSecondAbsolute;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Read;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ReadOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ReadInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekIndex;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekIndexInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.DeleteId;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.DeleteIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Play;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekId;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ReadList;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Next;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SetRepeat;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SetRepeatInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ProtocolInfo;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.ProtocolInfoOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekSecondRelative;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.SeekSecondRelativeInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Previous;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.Id;
import nextcp.upnp.modelGen.avopenhomeorg.playlist.actions.IdOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class PlaylistService
{
    private static Logger log = LoggerFactory.getLogger(PlaylistService.class.getName());

    private RemoteService playlistService = null;

    private UpnpService upnpService = null;

    private PlaylistServiceStateVariable playlistServiceStateVariable = new PlaylistServiceStateVariable();
    
    private PlaylistServiceSubscription subscription = null;
    
    public PlaylistService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        playlistService = device.findService(new ServiceType("av-openhome-org", "Playlist"));
        if (playlistService != null)
        {
	        subscription = new PlaylistServiceSubscription(playlistService, 600);
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
    
    public void addSubscriptionEventListener(IPlaylistServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IPlaylistServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getPlaylistService()
    {
        return playlistService;
    }    


    public IdArrayChangedOutput idArrayChanged(IdArrayChangedInput inp)
    {
        IdArrayChanged idArrayChanged = new IdArrayChanged(playlistService, inp, upnpService.getControlPoint());
        IdArrayChangedOutput res = idArrayChanged.executeAction();
        return res;        
    }

    public void setShuffle(SetShuffleInput inp)
    {
        SetShuffle setShuffle = new SetShuffle(playlistService, inp, upnpService.getControlPoint());
        setShuffle.executeAction();
    }

    public void pause()
    {
        Pause pause = new Pause(playlistService,  upnpService.getControlPoint());
        pause.executeAction();
    }

    public TracksMaxOutput tracksMax()
    {
        TracksMax tracksMax = new TracksMax(playlistService,  upnpService.getControlPoint());
        TracksMaxOutput res = tracksMax.executeAction();
        return res;        
    }

    public ShuffleOutput shuffle()
    {
        Shuffle shuffle = new Shuffle(playlistService,  upnpService.getControlPoint());
        ShuffleOutput res = shuffle.executeAction();
        return res;        
    }

    public void stop()
    {
        Stop stop = new Stop(playlistService,  upnpService.getControlPoint());
        stop.executeAction();
    }

    public void deleteAll()
    {
        DeleteAll deleteAll = new DeleteAll(playlistService,  upnpService.getControlPoint());
        deleteAll.executeAction();
    }

    public RepeatOutput repeat()
    {
        Repeat repeat = new Repeat(playlistService,  upnpService.getControlPoint());
        RepeatOutput res = repeat.executeAction();
        return res;        
    }

    public IdArrayOutput idArray()
    {
        IdArray idArray = new IdArray(playlistService,  upnpService.getControlPoint());
        IdArrayOutput res = idArray.executeAction();
        return res;        
    }

    public InsertOutput insert(InsertInput inp)
    {
        Insert insert = new Insert(playlistService, inp, upnpService.getControlPoint());
        InsertOutput res = insert.executeAction();
        return res;        
    }

    public TransportStateOutput transportState()
    {
        TransportState transportState = new TransportState(playlistService,  upnpService.getControlPoint());
        TransportStateOutput res = transportState.executeAction();
        return res;        
    }

    public void seekSecondAbsolute(SeekSecondAbsoluteInput inp)
    {
        SeekSecondAbsolute seekSecondAbsolute = new SeekSecondAbsolute(playlistService, inp, upnpService.getControlPoint());
        seekSecondAbsolute.executeAction();
    }

    public ReadOutput read(ReadInput inp)
    {
        Read read = new Read(playlistService, inp, upnpService.getControlPoint());
        ReadOutput res = read.executeAction();
        return res;        
    }

    public void seekIndex(SeekIndexInput inp)
    {
        SeekIndex seekIndex = new SeekIndex(playlistService, inp, upnpService.getControlPoint());
        seekIndex.executeAction();
    }

    public void deleteId(DeleteIdInput inp)
    {
        DeleteId deleteId = new DeleteId(playlistService, inp, upnpService.getControlPoint());
        deleteId.executeAction();
    }

    public void play()
    {
        Play play = new Play(playlistService,  upnpService.getControlPoint());
        play.executeAction();
    }

    public void seekId(SeekIdInput inp)
    {
        SeekId seekId = new SeekId(playlistService, inp, upnpService.getControlPoint());
        seekId.executeAction();
    }

    public ReadListOutput readList(ReadListInput inp)
    {
        ReadList readList = new ReadList(playlistService, inp, upnpService.getControlPoint());
        ReadListOutput res = readList.executeAction();
        return res;        
    }

    public void next()
    {
        Next next = new Next(playlistService,  upnpService.getControlPoint());
        next.executeAction();
    }

    public void setRepeat(SetRepeatInput inp)
    {
        SetRepeat setRepeat = new SetRepeat(playlistService, inp, upnpService.getControlPoint());
        setRepeat.executeAction();
    }

    public ProtocolInfoOutput protocolInfo()
    {
        ProtocolInfo protocolInfo = new ProtocolInfo(playlistService,  upnpService.getControlPoint());
        ProtocolInfoOutput res = protocolInfo.executeAction();
        return res;        
    }

    public void seekSecondRelative(SeekSecondRelativeInput inp)
    {
        SeekSecondRelative seekSecondRelative = new SeekSecondRelative(playlistService, inp, upnpService.getControlPoint());
        seekSecondRelative.executeAction();
    }

    public void previous()
    {
        Previous previous = new Previous(playlistService,  upnpService.getControlPoint());
        previous.executeAction();
    }

    public IdOutput id()
    {
        Id id = new Id(playlistService,  upnpService.getControlPoint());
        IdOutput res = id.executeAction();
        return res;        
    }
}
