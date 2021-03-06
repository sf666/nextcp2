package nextcp.upnp.modelGen.schemasupnporg.renderingControl;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBRange;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBRangeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBRangeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetMuteOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetMuteInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SelectPreset;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SelectPresetInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.ListPresets;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.ListPresetsOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.ListPresetsInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetMuteInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDB;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
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
