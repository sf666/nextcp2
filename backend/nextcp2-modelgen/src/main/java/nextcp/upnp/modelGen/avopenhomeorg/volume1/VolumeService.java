package nextcp.upnp.modelGen.avopenhomeorg.volume1;

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

import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Balance;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.BalanceOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.BalanceDec;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.BalanceInc;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Characteristics;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.CharacteristicsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Fade;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.FadeOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.FadeDec;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.FadeInc;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Mute;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.MuteOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetBalance;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetBalanceInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetFade;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetFadeInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetMute;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetMuteInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetVolume;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetVolumeInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Volume;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeDec;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeInc;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeLimit;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeLimitOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class VolumeService
{
    private static Logger log = LoggerFactory.getLogger(VolumeService.class.getName());

    private RemoteService volumeService = null;

    private UpnpService upnpService = null;

//    private VolumeServiceStateVariable volumeServiceStateVariable = new VolumeServiceStateVariable();
    
    private VolumeServiceSubscription subscription = null;
    
    public VolumeService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public VolumeService(UpnpService upnpService, RemoteDevice device, IVolumeServiceEventListener listener)
    {
        this.upnpService = upnpService;
        volumeService = device.findService(new ServiceType("av-openhome-org", "Volume"));
        if (volumeService != null)
        {
	        subscription = new VolumeServiceSubscription(volumeService, 600);
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
	
	        log.info(String.format("initialized service 'Volume' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Volume' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IVolumeServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IVolumeServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getVolumeService()
    {
        return volumeService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return volumeService != null && volumeService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public BalanceOutput balance()
    {
        if (!hasAction("Balance"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Balance of service Volume");
        }
        Balance balance = new Balance(volumeService,  upnpService.getControlPoint());
        BalanceOutput res = balance.executeAction();
        return res;        
    }

    public void balanceDec()
    {
        if (!hasAction("BalanceDec"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action BalanceDec of service Volume");
        }
        BalanceDec balanceDec = new BalanceDec(volumeService,  upnpService.getControlPoint());
        balanceDec.executeAction();
    }

    public void balanceInc()
    {
        if (!hasAction("BalanceInc"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action BalanceInc of service Volume");
        }
        BalanceInc balanceInc = new BalanceInc(volumeService,  upnpService.getControlPoint());
        balanceInc.executeAction();
    }

    public CharacteristicsOutput characteristics()
    {
        if (!hasAction("Characteristics"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Characteristics of service Volume");
        }
        Characteristics characteristics = new Characteristics(volumeService,  upnpService.getControlPoint());
        CharacteristicsOutput res = characteristics.executeAction();
        return res;        
    }

    public FadeOutput fade()
    {
        if (!hasAction("Fade"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Fade of service Volume");
        }
        Fade fade = new Fade(volumeService,  upnpService.getControlPoint());
        FadeOutput res = fade.executeAction();
        return res;        
    }

    public void fadeDec()
    {
        if (!hasAction("FadeDec"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action FadeDec of service Volume");
        }
        FadeDec fadeDec = new FadeDec(volumeService,  upnpService.getControlPoint());
        fadeDec.executeAction();
    }

    public void fadeInc()
    {
        if (!hasAction("FadeInc"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action FadeInc of service Volume");
        }
        FadeInc fadeInc = new FadeInc(volumeService,  upnpService.getControlPoint());
        fadeInc.executeAction();
    }

    public MuteOutput mute()
    {
        if (!hasAction("Mute"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Mute of service Volume");
        }
        Mute mute = new Mute(volumeService,  upnpService.getControlPoint());
        MuteOutput res = mute.executeAction();
        return res;        
    }

    public void setBalance(SetBalanceInput inp)
    {
        if (!hasAction("SetBalance"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetBalance of service Volume");
        }
        SetBalance setBalance = new SetBalance(volumeService, inp, upnpService.getControlPoint());
        setBalance.executeAction();
    }

    public void setFade(SetFadeInput inp)
    {
        if (!hasAction("SetFade"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetFade of service Volume");
        }
        SetFade setFade = new SetFade(volumeService, inp, upnpService.getControlPoint());
        setFade.executeAction();
    }

    public void setMute(SetMuteInput inp)
    {
        if (!hasAction("SetMute"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetMute of service Volume");
        }
        SetMute setMute = new SetMute(volumeService, inp, upnpService.getControlPoint());
        setMute.executeAction();
    }

    public void setVolume(SetVolumeInput inp)
    {
        if (!hasAction("SetVolume"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetVolume of service Volume");
        }
        SetVolume setVolume = new SetVolume(volumeService, inp, upnpService.getControlPoint());
        setVolume.executeAction();
    }

    public VolumeOutput volume()
    {
        if (!hasAction("Volume"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Volume of service Volume");
        }
        Volume volume = new Volume(volumeService,  upnpService.getControlPoint());
        VolumeOutput res = volume.executeAction();
        return res;        
    }

    public void volumeDec()
    {
        if (!hasAction("VolumeDec"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action VolumeDec of service Volume");
        }
        VolumeDec volumeDec = new VolumeDec(volumeService,  upnpService.getControlPoint());
        volumeDec.executeAction();
    }

    public void volumeInc()
    {
        if (!hasAction("VolumeInc"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action VolumeInc of service Volume");
        }
        VolumeInc volumeInc = new VolumeInc(volumeService,  upnpService.getControlPoint());
        volumeInc.executeAction();
    }

    public VolumeLimitOutput volumeLimit()
    {
        if (!hasAction("VolumeLimit"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action VolumeLimit of service Volume");
        }
        VolumeLimit volumeLimit = new VolumeLimit(volumeService,  upnpService.getControlPoint());
        VolumeLimitOutput res = volumeLimit.executeAction();
        return res;        
    }
}
