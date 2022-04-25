package nextcp.upnp.modelGen.schemasupnporg.renderingControl2;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.SelectPreset;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.SelectPresetInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.SetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.SetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.ListPresets;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.ListPresetsOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.ListPresetsInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.SetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions.SetMuteInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class RenderingControlService
{
    private static Logger log = LoggerFactory.getLogger(RenderingControlService.class.getName());

    private RemoteService renderingControlService = null;

    private UpnpService upnpService = null;

    private RenderingControlServiceStateVariable renderingControlServiceStateVariable = new RenderingControlServiceStateVariable();
    
    private RenderingControlServiceSubscription subscription = null;
    
    public RenderingControlService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        renderingControlService = device.findService(new ServiceType("schemas-upnp-org", "RenderingControl"));
        if (renderingControlService != null)
        {
	        subscription = new RenderingControlServiceSubscription(renderingControlService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'RenderingControl' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'RenderingControl' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IRenderingControlServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IRenderingControlServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getRenderingControlService()
    {
        return renderingControlService;
    }    


    public void selectPreset(SelectPresetInput inp)
    {
        SelectPreset selectPreset = new SelectPreset(renderingControlService, inp, upnpService.getControlPoint());
        selectPreset.executeAction();
    }

    public void setVolume(SetVolumeInput inp)
    {
        SetVolume setVolume = new SetVolume(renderingControlService, inp, upnpService.getControlPoint());
        setVolume.executeAction();
    }

    public ListPresetsOutput listPresets(ListPresetsInput inp)
    {
        ListPresets listPresets = new ListPresets(renderingControlService, inp, upnpService.getControlPoint());
        ListPresetsOutput res = listPresets.executeAction();
        return res;        
    }

    public void setMute(SetMuteInput inp)
    {
        SetMute setMute = new SetMute(renderingControlService, inp, upnpService.getControlPoint());
        setMute.executeAction();
    }
}
