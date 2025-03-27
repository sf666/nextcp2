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

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetBalance;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetBalanceInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.FadeInc;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.BalanceDec;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.FadeDec;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.BalanceInc;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeInc;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Volume;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Characteristics;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.CharacteristicsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetVolume;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetVolumeInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetFade;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetFadeInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Balance;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.BalanceOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetMute;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.SetMuteInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Fade;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.FadeOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.Mute;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.MuteOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume1.actions.VolumeDec;


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

    private VolumeServiceStateVariable volumeServiceStateVariable = new VolumeServiceStateVariable();
    
    private VolumeServiceSubscription subscription = null;
    
    public VolumeService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        volumeService = device.findService(new ServiceType("av-openhome-org", "Volume"));
        if (volumeService != null)
        {
	        subscription = new VolumeServiceSubscription(volumeService, 600);
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


//
// Actions
// =========================================================================
//



    public void setBalance(SetBalanceInput inp)
    {
        SetBalance setBalance = new SetBalance(volumeService, inp, upnpService.getControlPoint());
        setBalance.executeAction();
    }

    public void fadeInc()
    {
        FadeInc fadeInc = new FadeInc(volumeService,  upnpService.getControlPoint());
        fadeInc.executeAction();
    }

    public void balanceDec()
    {
        BalanceDec balanceDec = new BalanceDec(volumeService,  upnpService.getControlPoint());
        balanceDec.executeAction();
    }

    public void fadeDec()
    {
        FadeDec fadeDec = new FadeDec(volumeService,  upnpService.getControlPoint());
        fadeDec.executeAction();
    }

    public void balanceInc()
    {
        BalanceInc balanceInc = new BalanceInc(volumeService,  upnpService.getControlPoint());
        balanceInc.executeAction();
    }

    public void volumeInc()
    {
        VolumeInc volumeInc = new VolumeInc(volumeService,  upnpService.getControlPoint());
        volumeInc.executeAction();
    }

    public VolumeOutput volume()
    {
        Volume volume = new Volume(volumeService,  upnpService.getControlPoint());
        VolumeOutput res = volume.executeAction();
        return res;        
    }

    public CharacteristicsOutput characteristics()
    {
        Characteristics characteristics = new Characteristics(volumeService,  upnpService.getControlPoint());
        CharacteristicsOutput res = characteristics.executeAction();
        return res;        
    }

    public void setVolume(SetVolumeInput inp)
    {
        SetVolume setVolume = new SetVolume(volumeService, inp, upnpService.getControlPoint());
        setVolume.executeAction();
    }

    public void setFade(SetFadeInput inp)
    {
        SetFade setFade = new SetFade(volumeService, inp, upnpService.getControlPoint());
        setFade.executeAction();
    }

    public BalanceOutput balance()
    {
        Balance balance = new Balance(volumeService,  upnpService.getControlPoint());
        BalanceOutput res = balance.executeAction();
        return res;        
    }

    public void setMute(SetMuteInput inp)
    {
        SetMute setMute = new SetMute(volumeService, inp, upnpService.getControlPoint());
        setMute.executeAction();
    }

    public FadeOutput fade()
    {
        Fade fade = new Fade(volumeService,  upnpService.getControlPoint());
        FadeOutput res = fade.executeAction();
        return res;        
    }

    public MuteOutput mute()
    {
        Mute mute = new Mute(volumeService,  upnpService.getControlPoint());
        MuteOutput res = mute.executeAction();
        return res;        
    }

    public void volumeDec()
    {
        VolumeDec volumeDec = new VolumeDec(volumeService,  upnpService.getControlPoint());
        volumeDec.executeAction();
    }
}
