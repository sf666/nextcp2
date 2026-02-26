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

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RestoreRatings;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbumMusicBrainz;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbumMusicBrainzInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedDiscogs;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedDiscogsOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedDiscogsInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.BackupAudioLikes;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetPreferEuropeanServer;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetPreferEuropeanServerInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.BackupRatings;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetUpnpCdsWrite;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetUpnpCdsWriteInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbumDiscogs;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbumDiscogsInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RestoreAudioLikes;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbumMusicBrainz;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbumMusicBrainzInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAnonymousDevicesWrite;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAnonymousDevicesWriteInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedMusicBrainz;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedMusicBrainzOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedMusicBrainzInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbumDiscogs;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbumDiscogsInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStore;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStoreFolder;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStoreFolderInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioUpdateRatingTag;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioUpdateRatingTagInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioLikesVisibleRoot;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioLikesVisibleRootInput;


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
        this.upnpService = upnpService;
        umsExtendedServicesService = device.findService(new ServiceType("schemas-upnp-org", "UmsExtendedServices"));
        if (umsExtendedServicesService != null)
        {
	        subscription = new UmsExtendedServicesServiceSubscription(umsExtendedServicesService, 600);
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


//
// Actions
// =========================================================================
//



    public void restoreRatings()
    {
        RestoreRatings restoreRatings = new RestoreRatings(umsExtendedServicesService,  upnpService.getControlPoint());
        restoreRatings.executeAction();
    }

    public void dislikeAlbumMusicBrainz(DislikeAlbumMusicBrainzInput inp)
    {
        DislikeAlbumMusicBrainz dislikeAlbumMusicBrainz = new DislikeAlbumMusicBrainz(umsExtendedServicesService, inp, upnpService.getControlPoint());
        dislikeAlbumMusicBrainz.executeAction();
    }

    public IsAlbumLikedDiscogsOutput isAlbumLikedDiscogs(IsAlbumLikedDiscogsInput inp)
    {
        IsAlbumLikedDiscogs isAlbumLikedDiscogs = new IsAlbumLikedDiscogs(umsExtendedServicesService, inp, upnpService.getControlPoint());
        IsAlbumLikedDiscogsOutput res = isAlbumLikedDiscogs.executeAction();
        return res;        
    }

    public void backupAudioLikes()
    {
        BackupAudioLikes backupAudioLikes = new BackupAudioLikes(umsExtendedServicesService,  upnpService.getControlPoint());
        backupAudioLikes.executeAction();
    }

    public void setPreferEuropeanServer(SetPreferEuropeanServerInput inp)
    {
        SetPreferEuropeanServer setPreferEuropeanServer = new SetPreferEuropeanServer(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setPreferEuropeanServer.executeAction();
    }

    public void backupRatings()
    {
        BackupRatings backupRatings = new BackupRatings(umsExtendedServicesService,  upnpService.getControlPoint());
        backupRatings.executeAction();
    }

    public void setUpnpCdsWrite(SetUpnpCdsWriteInput inp)
    {
        SetUpnpCdsWrite setUpnpCdsWrite = new SetUpnpCdsWrite(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setUpnpCdsWrite.executeAction();
    }

    public void likeAlbumDiscogs(LikeAlbumDiscogsInput inp)
    {
        LikeAlbumDiscogs likeAlbumDiscogs = new LikeAlbumDiscogs(umsExtendedServicesService, inp, upnpService.getControlPoint());
        likeAlbumDiscogs.executeAction();
    }

    public void restoreAudioLikes()
    {
        RestoreAudioLikes restoreAudioLikes = new RestoreAudioLikes(umsExtendedServicesService,  upnpService.getControlPoint());
        restoreAudioLikes.executeAction();
    }

    public void likeAlbumMusicBrainz(LikeAlbumMusicBrainzInput inp)
    {
        LikeAlbumMusicBrainz likeAlbumMusicBrainz = new LikeAlbumMusicBrainz(umsExtendedServicesService, inp, upnpService.getControlPoint());
        likeAlbumMusicBrainz.executeAction();
    }

    public void setAnonymousDevicesWrite(SetAnonymousDevicesWriteInput inp)
    {
        SetAnonymousDevicesWrite setAnonymousDevicesWrite = new SetAnonymousDevicesWrite(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAnonymousDevicesWrite.executeAction();
    }

    public IsAlbumLikedMusicBrainzOutput isAlbumLikedMusicBrainz(IsAlbumLikedMusicBrainzInput inp)
    {
        IsAlbumLikedMusicBrainz isAlbumLikedMusicBrainz = new IsAlbumLikedMusicBrainz(umsExtendedServicesService, inp, upnpService.getControlPoint());
        IsAlbumLikedMusicBrainzOutput res = isAlbumLikedMusicBrainz.executeAction();
        return res;        
    }

    public void dislikeAlbumDiscogs(DislikeAlbumDiscogsInput inp)
    {
        DislikeAlbumDiscogs dislikeAlbumDiscogs = new DislikeAlbumDiscogs(umsExtendedServicesService, inp, upnpService.getControlPoint());
        dislikeAlbumDiscogs.executeAction();
    }

    public void rescanMediaStore()
    {
        RescanMediaStore rescanMediaStore = new RescanMediaStore(umsExtendedServicesService,  upnpService.getControlPoint());
        rescanMediaStore.executeAction();
    }

    public void rescanMediaStoreFolder(RescanMediaStoreFolderInput inp)
    {
        RescanMediaStoreFolder rescanMediaStoreFolder = new RescanMediaStoreFolder(umsExtendedServicesService, inp, upnpService.getControlPoint());
        rescanMediaStoreFolder.executeAction();
    }

    public void setAudioUpdateRatingTag(SetAudioUpdateRatingTagInput inp)
    {
        SetAudioUpdateRatingTag setAudioUpdateRatingTag = new SetAudioUpdateRatingTag(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioUpdateRatingTag.executeAction();
    }

    public void setAudioLikesVisibleRoot(SetAudioLikesVisibleRootInput inp)
    {
        SetAudioLikesVisibleRoot setAudioLikesVisibleRoot = new SetAudioLikesVisibleRoot(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioLikesVisibleRoot.executeAction();
    }
}
