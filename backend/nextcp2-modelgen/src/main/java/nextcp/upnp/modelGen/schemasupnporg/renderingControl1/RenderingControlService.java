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

import nextcp.upnp.GenActionException;
import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBlueVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBlueVideoBlackLevelOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBlueVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBlueVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBlueVideoGainOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBlueVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBrightness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBrightnessOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetBrightnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetColorTemperature;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetColorTemperatureOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetColorTemperatureInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetContrast;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetContrastOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetContrastInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetGreenVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetGreenVideoBlackLevelOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetGreenVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetGreenVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetGreenVideoGainOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetGreenVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetHorizontalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetHorizontalKeystoneOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetHorizontalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetLoudness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetLoudnessOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetLoudnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetMuteOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetMuteInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetRedVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetRedVideoBlackLevelOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetRedVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetRedVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetRedVideoGainOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetRedVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetSharpness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetSharpnessOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetSharpnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVerticalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVerticalKeystoneOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVerticalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDB;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBRange;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBRangeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.GetVolumeDBRangeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.ListPresets;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.ListPresetsOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.ListPresetsInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SelectPreset;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SelectPresetInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetBlueVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetBlueVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetBlueVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetBlueVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetBrightness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetBrightnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetColorTemperature;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetColorTemperatureInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetContrast;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetContrastInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetGreenVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetGreenVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetGreenVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetGreenVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetHorizontalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetHorizontalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetLoudness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetLoudnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetMuteInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetRedVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetRedVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetRedVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetRedVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetSharpness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetSharpnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVerticalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVerticalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVolumeDB;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.SetVolumeDBInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_Get3DFormatter;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_Get3DFormatterOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_Get3DFormatterInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetAudioSelection;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetAudioSelectionOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetAudioSelectionInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetSubtitle;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetSubtitleOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetSubtitleInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetVideoSelection;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetVideoSelectionOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_GetVideoSelectionInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_Set3DFormatter;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_Set3DFormatterInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_SetSubtitle;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_SetSubtitleInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_UpdateAudioSelection;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_UpdateAudioSelectionInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_UpdateVideoSelection;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl1.actions.X_UpdateVideoSelectionInput;


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
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public RenderingControlService(UpnpService upnpService, RemoteDevice device, IRenderingControlServiceEventListener listener)
    {
        this.upnpService = upnpService;
        renderingControlService = device.findService(new ServiceType("schemas-upnp-org", "RenderingControl"));
        if (renderingControlService != null)
        {
	        subscription = new RenderingControlServiceSubscription(renderingControlService, 600);
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

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return renderingControlService != null && renderingControlService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public GetBlueVideoBlackLevelOutput getBlueVideoBlackLevel(GetBlueVideoBlackLevelInput inp)
    {
        if (!hasAction("GetBlueVideoBlackLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetBlueVideoBlackLevel of service RenderingControl");
        }
        GetBlueVideoBlackLevel getBlueVideoBlackLevel = new GetBlueVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        GetBlueVideoBlackLevelOutput res = getBlueVideoBlackLevel.executeAction();
        return res;        
    }

    public GetBlueVideoGainOutput getBlueVideoGain(GetBlueVideoGainInput inp)
    {
        if (!hasAction("GetBlueVideoGain"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetBlueVideoGain of service RenderingControl");
        }
        GetBlueVideoGain getBlueVideoGain = new GetBlueVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        GetBlueVideoGainOutput res = getBlueVideoGain.executeAction();
        return res;        
    }

    public GetBrightnessOutput getBrightness(GetBrightnessInput inp)
    {
        if (!hasAction("GetBrightness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetBrightness of service RenderingControl");
        }
        GetBrightness getBrightness = new GetBrightness(renderingControlService, inp, upnpService.getControlPoint());
        GetBrightnessOutput res = getBrightness.executeAction();
        return res;        
    }

    public GetColorTemperatureOutput getColorTemperature(GetColorTemperatureInput inp)
    {
        if (!hasAction("GetColorTemperature"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetColorTemperature of service RenderingControl");
        }
        GetColorTemperature getColorTemperature = new GetColorTemperature(renderingControlService, inp, upnpService.getControlPoint());
        GetColorTemperatureOutput res = getColorTemperature.executeAction();
        return res;        
    }

    public GetContrastOutput getContrast(GetContrastInput inp)
    {
        if (!hasAction("GetContrast"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetContrast of service RenderingControl");
        }
        GetContrast getContrast = new GetContrast(renderingControlService, inp, upnpService.getControlPoint());
        GetContrastOutput res = getContrast.executeAction();
        return res;        
    }

    public GetGreenVideoBlackLevelOutput getGreenVideoBlackLevel(GetGreenVideoBlackLevelInput inp)
    {
        if (!hasAction("GetGreenVideoBlackLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetGreenVideoBlackLevel of service RenderingControl");
        }
        GetGreenVideoBlackLevel getGreenVideoBlackLevel = new GetGreenVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        GetGreenVideoBlackLevelOutput res = getGreenVideoBlackLevel.executeAction();
        return res;        
    }

    public GetGreenVideoGainOutput getGreenVideoGain(GetGreenVideoGainInput inp)
    {
        if (!hasAction("GetGreenVideoGain"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetGreenVideoGain of service RenderingControl");
        }
        GetGreenVideoGain getGreenVideoGain = new GetGreenVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        GetGreenVideoGainOutput res = getGreenVideoGain.executeAction();
        return res;        
    }

    public GetHorizontalKeystoneOutput getHorizontalKeystone(GetHorizontalKeystoneInput inp)
    {
        if (!hasAction("GetHorizontalKeystone"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetHorizontalKeystone of service RenderingControl");
        }
        GetHorizontalKeystone getHorizontalKeystone = new GetHorizontalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        GetHorizontalKeystoneOutput res = getHorizontalKeystone.executeAction();
        return res;        
    }

    public GetLoudnessOutput getLoudness(GetLoudnessInput inp)
    {
        if (!hasAction("GetLoudness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetLoudness of service RenderingControl");
        }
        GetLoudness getLoudness = new GetLoudness(renderingControlService, inp, upnpService.getControlPoint());
        GetLoudnessOutput res = getLoudness.executeAction();
        return res;        
    }

    public GetMuteOutput getMute(GetMuteInput inp)
    {
        if (!hasAction("GetMute"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMute of service RenderingControl");
        }
        GetMute getMute = new GetMute(renderingControlService, inp, upnpService.getControlPoint());
        GetMuteOutput res = getMute.executeAction();
        return res;        
    }

    public GetRedVideoBlackLevelOutput getRedVideoBlackLevel(GetRedVideoBlackLevelInput inp)
    {
        if (!hasAction("GetRedVideoBlackLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetRedVideoBlackLevel of service RenderingControl");
        }
        GetRedVideoBlackLevel getRedVideoBlackLevel = new GetRedVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        GetRedVideoBlackLevelOutput res = getRedVideoBlackLevel.executeAction();
        return res;        
    }

    public GetRedVideoGainOutput getRedVideoGain(GetRedVideoGainInput inp)
    {
        if (!hasAction("GetRedVideoGain"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetRedVideoGain of service RenderingControl");
        }
        GetRedVideoGain getRedVideoGain = new GetRedVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        GetRedVideoGainOutput res = getRedVideoGain.executeAction();
        return res;        
    }

    public GetSharpnessOutput getSharpness(GetSharpnessInput inp)
    {
        if (!hasAction("GetSharpness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSharpness of service RenderingControl");
        }
        GetSharpness getSharpness = new GetSharpness(renderingControlService, inp, upnpService.getControlPoint());
        GetSharpnessOutput res = getSharpness.executeAction();
        return res;        
    }

    public GetVerticalKeystoneOutput getVerticalKeystone(GetVerticalKeystoneInput inp)
    {
        if (!hasAction("GetVerticalKeystone"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetVerticalKeystone of service RenderingControl");
        }
        GetVerticalKeystone getVerticalKeystone = new GetVerticalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        GetVerticalKeystoneOutput res = getVerticalKeystone.executeAction();
        return res;        
    }

    public GetVolumeOutput getVolume(GetVolumeInput inp)
    {
        if (!hasAction("GetVolume"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetVolume of service RenderingControl");
        }
        GetVolume getVolume = new GetVolume(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeOutput res = getVolume.executeAction();
        return res;        
    }

    public GetVolumeDBOutput getVolumeDB(GetVolumeDBInput inp)
    {
        if (!hasAction("GetVolumeDB"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetVolumeDB of service RenderingControl");
        }
        GetVolumeDB getVolumeDB = new GetVolumeDB(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeDBOutput res = getVolumeDB.executeAction();
        return res;        
    }

    public GetVolumeDBRangeOutput getVolumeDBRange(GetVolumeDBRangeInput inp)
    {
        if (!hasAction("GetVolumeDBRange"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetVolumeDBRange of service RenderingControl");
        }
        GetVolumeDBRange getVolumeDBRange = new GetVolumeDBRange(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeDBRangeOutput res = getVolumeDBRange.executeAction();
        return res;        
    }

    public ListPresetsOutput listPresets(ListPresetsInput inp)
    {
        if (!hasAction("ListPresets"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ListPresets of service RenderingControl");
        }
        ListPresets listPresets = new ListPresets(renderingControlService, inp, upnpService.getControlPoint());
        ListPresetsOutput res = listPresets.executeAction();
        return res;        
    }

    public void selectPreset(SelectPresetInput inp)
    {
        if (!hasAction("SelectPreset"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SelectPreset of service RenderingControl");
        }
        SelectPreset selectPreset = new SelectPreset(renderingControlService, inp, upnpService.getControlPoint());
        selectPreset.executeAction();
    }

    public void setBlueVideoBlackLevel(SetBlueVideoBlackLevelInput inp)
    {
        if (!hasAction("SetBlueVideoBlackLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetBlueVideoBlackLevel of service RenderingControl");
        }
        SetBlueVideoBlackLevel setBlueVideoBlackLevel = new SetBlueVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        setBlueVideoBlackLevel.executeAction();
    }

    public void setBlueVideoGain(SetBlueVideoGainInput inp)
    {
        if (!hasAction("SetBlueVideoGain"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetBlueVideoGain of service RenderingControl");
        }
        SetBlueVideoGain setBlueVideoGain = new SetBlueVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        setBlueVideoGain.executeAction();
    }

    public void setBrightness(SetBrightnessInput inp)
    {
        if (!hasAction("SetBrightness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetBrightness of service RenderingControl");
        }
        SetBrightness setBrightness = new SetBrightness(renderingControlService, inp, upnpService.getControlPoint());
        setBrightness.executeAction();
    }

    public void setColorTemperature(SetColorTemperatureInput inp)
    {
        if (!hasAction("SetColorTemperature"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetColorTemperature of service RenderingControl");
        }
        SetColorTemperature setColorTemperature = new SetColorTemperature(renderingControlService, inp, upnpService.getControlPoint());
        setColorTemperature.executeAction();
    }

    public void setContrast(SetContrastInput inp)
    {
        if (!hasAction("SetContrast"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetContrast of service RenderingControl");
        }
        SetContrast setContrast = new SetContrast(renderingControlService, inp, upnpService.getControlPoint());
        setContrast.executeAction();
    }

    public void setGreenVideoBlackLevel(SetGreenVideoBlackLevelInput inp)
    {
        if (!hasAction("SetGreenVideoBlackLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetGreenVideoBlackLevel of service RenderingControl");
        }
        SetGreenVideoBlackLevel setGreenVideoBlackLevel = new SetGreenVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        setGreenVideoBlackLevel.executeAction();
    }

    public void setGreenVideoGain(SetGreenVideoGainInput inp)
    {
        if (!hasAction("SetGreenVideoGain"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetGreenVideoGain of service RenderingControl");
        }
        SetGreenVideoGain setGreenVideoGain = new SetGreenVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        setGreenVideoGain.executeAction();
    }

    public void setHorizontalKeystone(SetHorizontalKeystoneInput inp)
    {
        if (!hasAction("SetHorizontalKeystone"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetHorizontalKeystone of service RenderingControl");
        }
        SetHorizontalKeystone setHorizontalKeystone = new SetHorizontalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        setHorizontalKeystone.executeAction();
    }

    public void setLoudness(SetLoudnessInput inp)
    {
        if (!hasAction("SetLoudness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetLoudness of service RenderingControl");
        }
        SetLoudness setLoudness = new SetLoudness(renderingControlService, inp, upnpService.getControlPoint());
        setLoudness.executeAction();
    }

    public void setMute(SetMuteInput inp)
    {
        if (!hasAction("SetMute"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetMute of service RenderingControl");
        }
        SetMute setMute = new SetMute(renderingControlService, inp, upnpService.getControlPoint());
        setMute.executeAction();
    }

    public void setRedVideoBlackLevel(SetRedVideoBlackLevelInput inp)
    {
        if (!hasAction("SetRedVideoBlackLevel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetRedVideoBlackLevel of service RenderingControl");
        }
        SetRedVideoBlackLevel setRedVideoBlackLevel = new SetRedVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        setRedVideoBlackLevel.executeAction();
    }

    public void setRedVideoGain(SetRedVideoGainInput inp)
    {
        if (!hasAction("SetRedVideoGain"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetRedVideoGain of service RenderingControl");
        }
        SetRedVideoGain setRedVideoGain = new SetRedVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        setRedVideoGain.executeAction();
    }

    public void setSharpness(SetSharpnessInput inp)
    {
        if (!hasAction("SetSharpness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetSharpness of service RenderingControl");
        }
        SetSharpness setSharpness = new SetSharpness(renderingControlService, inp, upnpService.getControlPoint());
        setSharpness.executeAction();
    }

    public void setVerticalKeystone(SetVerticalKeystoneInput inp)
    {
        if (!hasAction("SetVerticalKeystone"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetVerticalKeystone of service RenderingControl");
        }
        SetVerticalKeystone setVerticalKeystone = new SetVerticalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        setVerticalKeystone.executeAction();
    }

    public void setVolume(SetVolumeInput inp)
    {
        if (!hasAction("SetVolume"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetVolume of service RenderingControl");
        }
        SetVolume setVolume = new SetVolume(renderingControlService, inp, upnpService.getControlPoint());
        setVolume.executeAction();
    }

    public void setVolumeDB(SetVolumeDBInput inp)
    {
        if (!hasAction("SetVolumeDB"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetVolumeDB of service RenderingControl");
        }
        SetVolumeDB setVolumeDB = new SetVolumeDB(renderingControlService, inp, upnpService.getControlPoint());
        setVolumeDB.executeAction();
    }

    public X_Get3DFormatterOutput x_Get3DFormatter(X_Get3DFormatterInput inp)
    {
        if (!hasAction("X_Get3DFormatter"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_Get3DFormatter of service RenderingControl");
        }
        X_Get3DFormatter x_Get3DFormatter = new X_Get3DFormatter(renderingControlService, inp, upnpService.getControlPoint());
        X_Get3DFormatterOutput res = x_Get3DFormatter.executeAction();
        return res;        
    }

    public X_GetAudioSelectionOutput x_GetAudioSelection(X_GetAudioSelectionInput inp)
    {
        if (!hasAction("X_GetAudioSelection"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_GetAudioSelection of service RenderingControl");
        }
        X_GetAudioSelection x_GetAudioSelection = new X_GetAudioSelection(renderingControlService, inp, upnpService.getControlPoint());
        X_GetAudioSelectionOutput res = x_GetAudioSelection.executeAction();
        return res;        
    }

    public X_GetSubtitleOutput x_GetSubtitle(X_GetSubtitleInput inp)
    {
        if (!hasAction("X_GetSubtitle"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_GetSubtitle of service RenderingControl");
        }
        X_GetSubtitle x_GetSubtitle = new X_GetSubtitle(renderingControlService, inp, upnpService.getControlPoint());
        X_GetSubtitleOutput res = x_GetSubtitle.executeAction();
        return res;        
    }

    public X_GetVideoSelectionOutput x_GetVideoSelection(X_GetVideoSelectionInput inp)
    {
        if (!hasAction("X_GetVideoSelection"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_GetVideoSelection of service RenderingControl");
        }
        X_GetVideoSelection x_GetVideoSelection = new X_GetVideoSelection(renderingControlService, inp, upnpService.getControlPoint());
        X_GetVideoSelectionOutput res = x_GetVideoSelection.executeAction();
        return res;        
    }

    public void x_Set3DFormatter(X_Set3DFormatterInput inp)
    {
        if (!hasAction("X_Set3DFormatter"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_Set3DFormatter of service RenderingControl");
        }
        X_Set3DFormatter x_Set3DFormatter = new X_Set3DFormatter(renderingControlService, inp, upnpService.getControlPoint());
        x_Set3DFormatter.executeAction();
    }

    public void x_SetSubtitle(X_SetSubtitleInput inp)
    {
        if (!hasAction("X_SetSubtitle"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_SetSubtitle of service RenderingControl");
        }
        X_SetSubtitle x_SetSubtitle = new X_SetSubtitle(renderingControlService, inp, upnpService.getControlPoint());
        x_SetSubtitle.executeAction();
    }

    public void x_UpdateAudioSelection(X_UpdateAudioSelectionInput inp)
    {
        if (!hasAction("X_UpdateAudioSelection"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_UpdateAudioSelection of service RenderingControl");
        }
        X_UpdateAudioSelection x_UpdateAudioSelection = new X_UpdateAudioSelection(renderingControlService, inp, upnpService.getControlPoint());
        x_UpdateAudioSelection.executeAction();
    }

    public void x_UpdateVideoSelection(X_UpdateVideoSelectionInput inp)
    {
        if (!hasAction("X_UpdateVideoSelection"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_UpdateVideoSelection of service RenderingControl");
        }
        X_UpdateVideoSelection x_UpdateVideoSelection = new X_UpdateVideoSelection(renderingControlService, inp, upnpService.getControlPoint());
        x_UpdateVideoSelection.executeAction();
    }
}
