package nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1;

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

import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.AddRadioStationToPlaylist;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.AddRadioStationToPlaylistOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.AddRadioStationToPlaylistInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.BackupAudioLikes;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.BackupRatings;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbum;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbumInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetAudioArtistDir;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetAudioArtistDirOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetPlaylistNowPlaying;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetPlaylistNowPlayingOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetPlaylistNowPlayingInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetRadioFilterValues;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetRadioFilterValuesOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetRadioFilterValuesInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetWebStreamIcyOrder;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetWebStreamIcyOrderOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetWebStreamIcyOrderInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetWebStreamNowPlaying;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetWebStreamNowPlayingOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.GetWebStreamNowPlayingInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLiked;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbum;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbumInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStore;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStoreFolder;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStoreFolderInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RestoreAudioLikes;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RestoreRatings;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SearchRadioStations;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SearchRadioStationsOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SearchRadioStationsInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAnonymousDevicesWrite;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAnonymousDevicesWriteInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictPass;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictPassInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictUser;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictUserInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioArtistDir;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioArtistDirInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioLikesVisibleRoot;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioLikesVisibleRootInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioUpdateRatingTag;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioUpdateRatingTagInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetPlaylistLoop;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetPlaylistLoopInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetPreferEuropeanServer;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetPreferEuropeanServerInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetUpnpCdsWrite;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetUpnpCdsWriteInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetWebStreamIcyOrder;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetWebStreamIcyOrderInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class UmsExtendedServicesService
{
    private static Logger log = LoggerFactory.getLogger(UmsExtendedServicesService.class.getName());

    private RemoteService umsExtendedServicesService = null;

    private UpnpService upnpService = null;

//    private UmsExtendedServicesServiceStateVariable umsExtendedServicesServiceStateVariable = new UmsExtendedServicesServiceStateVariable();
    
    private UmsExtendedServicesServiceSubscription subscription = null;
    
    public UmsExtendedServicesService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public UmsExtendedServicesService(UpnpService upnpService, RemoteDevice device, IUmsExtendedServicesServiceEventListener listener)
    {
        this.upnpService = upnpService;
        umsExtendedServicesService = device.findService(new ServiceType("schemas-upnp-org", "UmsExtendedServices"));
        if (umsExtendedServicesService != null)
        {
	        subscription = new UmsExtendedServicesServiceSubscription(umsExtendedServicesService, 600);
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
	
	        log.info(String.format("initialized service 'UmsExtendedServices' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'UmsExtendedServices' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IUmsExtendedServicesServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IUmsExtendedServicesServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getUmsExtendedServicesService()
    {
        return umsExtendedServicesService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return umsExtendedServicesService != null && umsExtendedServicesService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public AddRadioStationToPlaylistOutput addRadioStationToPlaylist(AddRadioStationToPlaylistInput inp)
    {
        if (!hasAction("AddRadioStationToPlaylist"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action AddRadioStationToPlaylist of service UmsExtendedServices");
        }
        AddRadioStationToPlaylist addRadioStationToPlaylist = new AddRadioStationToPlaylist(umsExtendedServicesService, inp, upnpService.getControlPoint());
        AddRadioStationToPlaylistOutput res = addRadioStationToPlaylist.executeAction();
        return res;        
    }

    public void backupAudioLikes()
    {
        if (!hasAction("BackupAudioLikes"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action BackupAudioLikes of service UmsExtendedServices");
        }
        BackupAudioLikes backupAudioLikes = new BackupAudioLikes(umsExtendedServicesService,  upnpService.getControlPoint());
        backupAudioLikes.executeAction();
    }

    public void backupRatings()
    {
        if (!hasAction("BackupRatings"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action BackupRatings of service UmsExtendedServices");
        }
        BackupRatings backupRatings = new BackupRatings(umsExtendedServicesService,  upnpService.getControlPoint());
        backupRatings.executeAction();
    }

    public void dislikeAlbum(DislikeAlbumInput inp)
    {
        if (!hasAction("DislikeAlbum"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DislikeAlbum of service UmsExtendedServices");
        }
        DislikeAlbum dislikeAlbum = new DislikeAlbum(umsExtendedServicesService, inp, upnpService.getControlPoint());
        dislikeAlbum.executeAction();
    }

    public GetAudioArtistDirOutput getAudioArtistDir()
    {
        if (!hasAction("GetAudioArtistDir"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAudioArtistDir of service UmsExtendedServices");
        }
        GetAudioArtistDir getAudioArtistDir = new GetAudioArtistDir(umsExtendedServicesService,  upnpService.getControlPoint());
        GetAudioArtistDirOutput res = getAudioArtistDir.executeAction();
        return res;        
    }

    public GetPlaylistNowPlayingOutput getPlaylistNowPlaying(GetPlaylistNowPlayingInput inp)
    {
        if (!hasAction("GetPlaylistNowPlaying"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPlaylistNowPlaying of service UmsExtendedServices");
        }
        GetPlaylistNowPlaying getPlaylistNowPlaying = new GetPlaylistNowPlaying(umsExtendedServicesService, inp, upnpService.getControlPoint());
        GetPlaylistNowPlayingOutput res = getPlaylistNowPlaying.executeAction();
        return res;        
    }

    public GetRadioFilterValuesOutput getRadioFilterValues(GetRadioFilterValuesInput inp)
    {
        if (!hasAction("GetRadioFilterValues"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetRadioFilterValues of service UmsExtendedServices");
        }
        GetRadioFilterValues getRadioFilterValues = new GetRadioFilterValues(umsExtendedServicesService, inp, upnpService.getControlPoint());
        GetRadioFilterValuesOutput res = getRadioFilterValues.executeAction();
        return res;        
    }

    public GetWebStreamIcyOrderOutput getWebStreamIcyOrder(GetWebStreamIcyOrderInput inp)
    {
        if (!hasAction("GetWebStreamIcyOrder"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetWebStreamIcyOrder of service UmsExtendedServices");
        }
        GetWebStreamIcyOrder getWebStreamIcyOrder = new GetWebStreamIcyOrder(umsExtendedServicesService, inp, upnpService.getControlPoint());
        GetWebStreamIcyOrderOutput res = getWebStreamIcyOrder.executeAction();
        return res;        
    }

    public GetWebStreamNowPlayingOutput getWebStreamNowPlaying(GetWebStreamNowPlayingInput inp)
    {
        if (!hasAction("GetWebStreamNowPlaying"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetWebStreamNowPlaying of service UmsExtendedServices");
        }
        GetWebStreamNowPlaying getWebStreamNowPlaying = new GetWebStreamNowPlaying(umsExtendedServicesService, inp, upnpService.getControlPoint());
        GetWebStreamNowPlayingOutput res = getWebStreamNowPlaying.executeAction();
        return res;        
    }

    public IsAlbumLikedOutput isAlbumLiked(IsAlbumLikedInput inp)
    {
        if (!hasAction("IsAlbumLiked"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action IsAlbumLiked of service UmsExtendedServices");
        }
        IsAlbumLiked isAlbumLiked = new IsAlbumLiked(umsExtendedServicesService, inp, upnpService.getControlPoint());
        IsAlbumLikedOutput res = isAlbumLiked.executeAction();
        return res;        
    }

    public void likeAlbum(LikeAlbumInput inp)
    {
        if (!hasAction("LikeAlbum"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action LikeAlbum of service UmsExtendedServices");
        }
        LikeAlbum likeAlbum = new LikeAlbum(umsExtendedServicesService, inp, upnpService.getControlPoint());
        likeAlbum.executeAction();
    }

    public void rescanMediaStore()
    {
        if (!hasAction("RescanMediaStore"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action RescanMediaStore of service UmsExtendedServices");
        }
        RescanMediaStore rescanMediaStore = new RescanMediaStore(umsExtendedServicesService,  upnpService.getControlPoint());
        rescanMediaStore.executeAction();
    }

    public void rescanMediaStoreFolder(RescanMediaStoreFolderInput inp)
    {
        if (!hasAction("RescanMediaStoreFolder"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action RescanMediaStoreFolder of service UmsExtendedServices");
        }
        RescanMediaStoreFolder rescanMediaStoreFolder = new RescanMediaStoreFolder(umsExtendedServicesService, inp, upnpService.getControlPoint());
        rescanMediaStoreFolder.executeAction();
    }

    public void restoreAudioLikes()
    {
        if (!hasAction("RestoreAudioLikes"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action RestoreAudioLikes of service UmsExtendedServices");
        }
        RestoreAudioLikes restoreAudioLikes = new RestoreAudioLikes(umsExtendedServicesService,  upnpService.getControlPoint());
        restoreAudioLikes.executeAction();
    }

    public void restoreRatings()
    {
        if (!hasAction("RestoreRatings"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action RestoreRatings of service UmsExtendedServices");
        }
        RestoreRatings restoreRatings = new RestoreRatings(umsExtendedServicesService,  upnpService.getControlPoint());
        restoreRatings.executeAction();
    }

    public SearchRadioStationsOutput searchRadioStations(SearchRadioStationsInput inp)
    {
        if (!hasAction("SearchRadioStations"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SearchRadioStations of service UmsExtendedServices");
        }
        SearchRadioStations searchRadioStations = new SearchRadioStations(umsExtendedServicesService, inp, upnpService.getControlPoint());
        SearchRadioStationsOutput res = searchRadioStations.executeAction();
        return res;        
    }

    public void setAnonymousDevicesWrite(SetAnonymousDevicesWriteInput inp)
    {
        if (!hasAction("SetAnonymousDevicesWrite"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAnonymousDevicesWrite of service UmsExtendedServices");
        }
        SetAnonymousDevicesWrite setAnonymousDevicesWrite = new SetAnonymousDevicesWrite(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAnonymousDevicesWrite.executeAction();
    }

    public void setAudioAddictPass(SetAudioAddictPassInput inp)
    {
        if (!hasAction("SetAudioAddictPass"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAudioAddictPass of service UmsExtendedServices");
        }
        SetAudioAddictPass setAudioAddictPass = new SetAudioAddictPass(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioAddictPass.executeAction();
    }

    public void setAudioAddictUser(SetAudioAddictUserInput inp)
    {
        if (!hasAction("SetAudioAddictUser"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAudioAddictUser of service UmsExtendedServices");
        }
        SetAudioAddictUser setAudioAddictUser = new SetAudioAddictUser(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioAddictUser.executeAction();
    }

    public void setAudioArtistDir(SetAudioArtistDirInput inp)
    {
        if (!hasAction("SetAudioArtistDir"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAudioArtistDir of service UmsExtendedServices");
        }
        SetAudioArtistDir setAudioArtistDir = new SetAudioArtistDir(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioArtistDir.executeAction();
    }

    public void setAudioLikesVisibleRoot(SetAudioLikesVisibleRootInput inp)
    {
        if (!hasAction("SetAudioLikesVisibleRoot"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAudioLikesVisibleRoot of service UmsExtendedServices");
        }
        SetAudioLikesVisibleRoot setAudioLikesVisibleRoot = new SetAudioLikesVisibleRoot(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioLikesVisibleRoot.executeAction();
    }

    public void setAudioUpdateRatingTag(SetAudioUpdateRatingTagInput inp)
    {
        if (!hasAction("SetAudioUpdateRatingTag"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAudioUpdateRatingTag of service UmsExtendedServices");
        }
        SetAudioUpdateRatingTag setAudioUpdateRatingTag = new SetAudioUpdateRatingTag(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioUpdateRatingTag.executeAction();
    }

    public void setPlaylistLoop(SetPlaylistLoopInput inp)
    {
        if (!hasAction("SetPlaylistLoop"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetPlaylistLoop of service UmsExtendedServices");
        }
        SetPlaylistLoop setPlaylistLoop = new SetPlaylistLoop(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setPlaylistLoop.executeAction();
    }

    public void setPreferEuropeanServer(SetPreferEuropeanServerInput inp)
    {
        if (!hasAction("SetPreferEuropeanServer"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetPreferEuropeanServer of service UmsExtendedServices");
        }
        SetPreferEuropeanServer setPreferEuropeanServer = new SetPreferEuropeanServer(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setPreferEuropeanServer.executeAction();
    }

    public void setUpnpCdsWrite(SetUpnpCdsWriteInput inp)
    {
        if (!hasAction("SetUpnpCdsWrite"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetUpnpCdsWrite of service UmsExtendedServices");
        }
        SetUpnpCdsWrite setUpnpCdsWrite = new SetUpnpCdsWrite(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setUpnpCdsWrite.executeAction();
    }

    public void setWebStreamIcyOrder(SetWebStreamIcyOrderInput inp)
    {
        if (!hasAction("SetWebStreamIcyOrder"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetWebStreamIcyOrder of service UmsExtendedServices");
        }
        SetWebStreamIcyOrder setWebStreamIcyOrder = new SetWebStreamIcyOrder(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setWebStreamIcyOrder.executeAction();
    }
}
