package nextcp.upnp.modelGen.avopenhomeorg.volume3;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.VolumeInc;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.VolumeLimit;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.VolumeLimitOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.Characteristics;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.CharacteristicsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.SetVolume;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.SetVolumeInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.Volume;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.VolumeOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.SetMute;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.SetMuteInput;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.Mute;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.MuteOutput;
import nextcp.upnp.modelGen.avopenhomeorg.volume3.actions.VolumeDec;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
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
    
    public void addSubscriptionEventListener(IVolumeServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IVolumeServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getVolumeService()
    {
        return volumeService;
    }    


    public void volumeInc()
    {
        VolumeInc volumeInc = new VolumeInc(volumeService,  upnpService.getControlPoint());
        volumeInc.executeAction();
    }

    public VolumeLimitOutput volumeLimit()
    {
        VolumeLimit volumeLimit = new VolumeLimit(volumeService,  upnpService.getControlPoint());
        VolumeLimitOutput res = volumeLimit.executeAction();
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

    public VolumeOutput volume()
    {
        Volume volume = new Volume(volumeService,  upnpService.getControlPoint());
        VolumeOutput res = volume.executeAction();
        return res;        
    }

    public void setMute(SetMuteInput inp)
    {
        SetMute setMute = new SetMute(volumeService, inp, upnpService.getControlPoint());
        setMute.executeAction();
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
