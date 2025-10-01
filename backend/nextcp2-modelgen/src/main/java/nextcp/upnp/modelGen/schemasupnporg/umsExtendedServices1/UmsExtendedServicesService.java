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
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.BackupAudioLikes;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictUser;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictUserInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.BackupRatings;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetUpnpCdsWrite;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetUpnpCdsWriteInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictPass;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictPassInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RestoreAudioLikes;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAnonymousDevicesWrite;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAnonymousDevicesWriteInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStore;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbum;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.DislikeAlbumInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStoreFolder;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.RescanMediaStoreFolderInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioUpdateRatingTag;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioUpdateRatingTagInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictEurope;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioAddictEuropeInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLiked;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedOutput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.IsAlbumLikedInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioLikesVisibleRoot;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.SetAudioLikesVisibleRootInput;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbum;
import nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions.LikeAlbumInput;


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

    public void backupAudioLikes()
    {
        BackupAudioLikes backupAudioLikes = new BackupAudioLikes(umsExtendedServicesService,  upnpService.getControlPoint());
        backupAudioLikes.executeAction();
    }

    public void setAudioAddictUser(SetAudioAddictUserInput inp)
    {
        SetAudioAddictUser setAudioAddictUser = new SetAudioAddictUser(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioAddictUser.executeAction();
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

    public void setAudioAddictPass(SetAudioAddictPassInput inp)
    {
        SetAudioAddictPass setAudioAddictPass = new SetAudioAddictPass(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioAddictPass.executeAction();
    }

    public void restoreAudioLikes()
    {
        RestoreAudioLikes restoreAudioLikes = new RestoreAudioLikes(umsExtendedServicesService,  upnpService.getControlPoint());
        restoreAudioLikes.executeAction();
    }

    public void setAnonymousDevicesWrite(SetAnonymousDevicesWriteInput inp)
    {
        SetAnonymousDevicesWrite setAnonymousDevicesWrite = new SetAnonymousDevicesWrite(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAnonymousDevicesWrite.executeAction();
    }

    public void rescanMediaStore()
    {
        RescanMediaStore rescanMediaStore = new RescanMediaStore(umsExtendedServicesService,  upnpService.getControlPoint());
        rescanMediaStore.executeAction();
    }

    public void dislikeAlbum(DislikeAlbumInput inp)
    {
        DislikeAlbum dislikeAlbum = new DislikeAlbum(umsExtendedServicesService, inp, upnpService.getControlPoint());
        dislikeAlbum.executeAction();
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

    public void setAudioAddictEurope(SetAudioAddictEuropeInput inp)
    {
        SetAudioAddictEurope setAudioAddictEurope = new SetAudioAddictEurope(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioAddictEurope.executeAction();
    }

    public IsAlbumLikedOutput isAlbumLiked(IsAlbumLikedInput inp)
    {
        IsAlbumLiked isAlbumLiked = new IsAlbumLiked(umsExtendedServicesService, inp, upnpService.getControlPoint());
        IsAlbumLikedOutput res = isAlbumLiked.executeAction();
        return res;        
    }

    public void setAudioLikesVisibleRoot(SetAudioLikesVisibleRootInput inp)
    {
        SetAudioLikesVisibleRoot setAudioLikesVisibleRoot = new SetAudioLikesVisibleRoot(umsExtendedServicesService, inp, upnpService.getControlPoint());
        setAudioLikesVisibleRoot.executeAction();
    }

    public void likeAlbum(LikeAlbumInput inp)
    {
        LikeAlbum likeAlbum = new LikeAlbum(umsExtendedServicesService, inp, upnpService.getControlPoint());
        likeAlbum.executeAction();
    }
}
