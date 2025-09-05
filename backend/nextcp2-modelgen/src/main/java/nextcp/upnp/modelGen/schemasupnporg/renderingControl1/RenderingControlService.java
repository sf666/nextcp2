package nextcp.upnp.modelGen.schemasupnporg.renderingControl1;

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

import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBRange;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBRangeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBRangeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetMuteOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetMuteInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SelectPreset;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SelectPresetInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.ListPresets;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.ListPresetsOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.ListPresetsInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetMuteInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDB;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class RenderingControlService
{
    private static Logger log = LoggerFactory.getLogger(RenderingControlService.class.getName());

    private RemoteService renderingControlService = null;

    private UpnpService upnpService = null;

//    private RenderingControlServiceStateVariable renderingControlServiceStateVariable = new RenderingControlServiceStateVariable();
    
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

    public void addSubscriptionEventListener(IRenderingControlServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IRenderingControlServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getRenderingControlService()
    {
        return renderingControlService;
    }    


//
// Actions
// =========================================================================
//



    public GetVolumeDBRangeOutput getVolumeDBRange(GetVolumeDBRangeInput inp)
    {
        GetVolumeDBRange getVolumeDBRange = new GetVolumeDBRange(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeDBRangeOutput res = getVolumeDBRange.executeAction();
        return res;        
    }

    public GetMuteOutput getMute(GetMuteInput inp)
    {
        GetMute getMute = new GetMute(renderingControlService, inp, upnpService.getControlPoint());
        GetMuteOutput res = getMute.executeAction();
        return res;        
    }

    public GetVolumeOutput getVolume(GetVolumeInput inp)
    {
        GetVolume getVolume = new GetVolume(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeOutput res = getVolume.executeAction();
        return res;        
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

    public GetVolumeDBOutput getVolumeDB(GetVolumeDBInput inp)
    {
        GetVolumeDB getVolumeDB = new GetVolumeDB(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeDBOutput res = getVolumeDB.executeAction();
        return res;        
    }
}
