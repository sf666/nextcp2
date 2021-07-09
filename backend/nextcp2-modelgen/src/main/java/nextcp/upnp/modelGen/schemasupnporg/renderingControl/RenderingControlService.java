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

import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetGreenVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetGreenVideoGainOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetGreenVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetRedVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetRedVideoBlackLevelOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetRedVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetHorizontalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetHorizontalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetColorTemperature;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetColorTemperatureOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetColorTemperatureInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetBlueVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetBlueVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetLoudness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetLoudnessOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetLoudnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVerticalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVerticalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetColorTemperature;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetColorTemperatureInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetMute;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetMuteOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetMuteInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetBrightness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetBrightnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetContrast;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetContrastInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVolume;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVolumeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBlueVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBlueVideoGainOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBlueVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetRedVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetRedVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetHorizontalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetHorizontalKeystoneOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetHorizontalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.ListPresets;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.ListPresetsOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.ListPresetsInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetGreenVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetGreenVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVerticalKeystone;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVerticalKeystoneOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVerticalKeystoneInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetGreenVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetGreenVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetContrast;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetContrastOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetContrastInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBRange;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBRangeOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetVolumeDBRangeInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetLoudness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetLoudnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBrightness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBrightnessOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBrightnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetRedVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetRedVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetBlueVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetBlueVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVolumeDB;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetVolumeDBInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SelectPreset;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SelectPresetInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetRedVideoGain;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetRedVideoGainOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetRedVideoGainInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBlueVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBlueVideoBlackLevelOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetBlueVideoBlackLevelInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetSharpness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetSharpnessOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetSharpnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetSharpness;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.SetSharpnessInput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetGreenVideoBlackLevel;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetGreenVideoBlackLevelOutput;
import nextcp.upnp.modelGen.schemasupnporg.renderingControl.actions.GetGreenVideoBlackLevelInput;
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


    public GetGreenVideoGainOutput getGreenVideoGain(GetGreenVideoGainInput inp)
    {
        GetGreenVideoGain getGreenVideoGain = new GetGreenVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        GetGreenVideoGainOutput res = getGreenVideoGain.executeAction();
        return res;        
    }

    public GetVolumeOutput getVolume(GetVolumeInput inp)
    {
        GetVolume getVolume = new GetVolume(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeOutput res = getVolume.executeAction();
        return res;        
    }

    public GetRedVideoBlackLevelOutput getRedVideoBlackLevel(GetRedVideoBlackLevelInput inp)
    {
        GetRedVideoBlackLevel getRedVideoBlackLevel = new GetRedVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        GetRedVideoBlackLevelOutput res = getRedVideoBlackLevel.executeAction();
        return res;        
    }

    public void setHorizontalKeystone(SetHorizontalKeystoneInput inp)
    {
        SetHorizontalKeystone setHorizontalKeystone = new SetHorizontalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        setHorizontalKeystone.executeAction();
    }

    public GetColorTemperatureOutput getColorTemperature(GetColorTemperatureInput inp)
    {
        GetColorTemperature getColorTemperature = new GetColorTemperature(renderingControlService, inp, upnpService.getControlPoint());
        GetColorTemperatureOutput res = getColorTemperature.executeAction();
        return res;        
    }

    public void setBlueVideoGain(SetBlueVideoGainInput inp)
    {
        SetBlueVideoGain setBlueVideoGain = new SetBlueVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        setBlueVideoGain.executeAction();
    }

    public GetLoudnessOutput getLoudness(GetLoudnessInput inp)
    {
        GetLoudness getLoudness = new GetLoudness(renderingControlService, inp, upnpService.getControlPoint());
        GetLoudnessOutput res = getLoudness.executeAction();
        return res;        
    }

    public void setVerticalKeystone(SetVerticalKeystoneInput inp)
    {
        SetVerticalKeystone setVerticalKeystone = new SetVerticalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        setVerticalKeystone.executeAction();
    }

    public void setColorTemperature(SetColorTemperatureInput inp)
    {
        SetColorTemperature setColorTemperature = new SetColorTemperature(renderingControlService, inp, upnpService.getControlPoint());
        setColorTemperature.executeAction();
    }

    public GetMuteOutput getMute(GetMuteInput inp)
    {
        GetMute getMute = new GetMute(renderingControlService, inp, upnpService.getControlPoint());
        GetMuteOutput res = getMute.executeAction();
        return res;        
    }

    public void setBrightness(SetBrightnessInput inp)
    {
        SetBrightness setBrightness = new SetBrightness(renderingControlService, inp, upnpService.getControlPoint());
        setBrightness.executeAction();
    }

    public void setContrast(SetContrastInput inp)
    {
        SetContrast setContrast = new SetContrast(renderingControlService, inp, upnpService.getControlPoint());
        setContrast.executeAction();
    }

    public void setVolume(SetVolumeInput inp)
    {
        SetVolume setVolume = new SetVolume(renderingControlService, inp, upnpService.getControlPoint());
        setVolume.executeAction();
    }

    public GetBlueVideoGainOutput getBlueVideoGain(GetBlueVideoGainInput inp)
    {
        GetBlueVideoGain getBlueVideoGain = new GetBlueVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        GetBlueVideoGainOutput res = getBlueVideoGain.executeAction();
        return res;        
    }

    public void setRedVideoGain(SetRedVideoGainInput inp)
    {
        SetRedVideoGain setRedVideoGain = new SetRedVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        setRedVideoGain.executeAction();
    }

    public GetHorizontalKeystoneOutput getHorizontalKeystone(GetHorizontalKeystoneInput inp)
    {
        GetHorizontalKeystone getHorizontalKeystone = new GetHorizontalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        GetHorizontalKeystoneOutput res = getHorizontalKeystone.executeAction();
        return res;        
    }

    public ListPresetsOutput listPresets(ListPresetsInput inp)
    {
        ListPresets listPresets = new ListPresets(renderingControlService, inp, upnpService.getControlPoint());
        ListPresetsOutput res = listPresets.executeAction();
        return res;        
    }

    public void setGreenVideoBlackLevel(SetGreenVideoBlackLevelInput inp)
    {
        SetGreenVideoBlackLevel setGreenVideoBlackLevel = new SetGreenVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        setGreenVideoBlackLevel.executeAction();
    }

    public GetVerticalKeystoneOutput getVerticalKeystone(GetVerticalKeystoneInput inp)
    {
        GetVerticalKeystone getVerticalKeystone = new GetVerticalKeystone(renderingControlService, inp, upnpService.getControlPoint());
        GetVerticalKeystoneOutput res = getVerticalKeystone.executeAction();
        return res;        
    }

    public void setGreenVideoGain(SetGreenVideoGainInput inp)
    {
        SetGreenVideoGain setGreenVideoGain = new SetGreenVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        setGreenVideoGain.executeAction();
    }

    public GetContrastOutput getContrast(GetContrastInput inp)
    {
        GetContrast getContrast = new GetContrast(renderingControlService, inp, upnpService.getControlPoint());
        GetContrastOutput res = getContrast.executeAction();
        return res;        
    }

    public GetVolumeDBRangeOutput getVolumeDBRange(GetVolumeDBRangeInput inp)
    {
        GetVolumeDBRange getVolumeDBRange = new GetVolumeDBRange(renderingControlService, inp, upnpService.getControlPoint());
        GetVolumeDBRangeOutput res = getVolumeDBRange.executeAction();
        return res;        
    }

    public void setLoudness(SetLoudnessInput inp)
    {
        SetLoudness setLoudness = new SetLoudness(renderingControlService, inp, upnpService.getControlPoint());
        setLoudness.executeAction();
    }

    public GetBrightnessOutput getBrightness(GetBrightnessInput inp)
    {
        GetBrightness getBrightness = new GetBrightness(renderingControlService, inp, upnpService.getControlPoint());
        GetBrightnessOutput res = getBrightness.executeAction();
        return res;        
    }

    public void setRedVideoBlackLevel(SetRedVideoBlackLevelInput inp)
    {
        SetRedVideoBlackLevel setRedVideoBlackLevel = new SetRedVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        setRedVideoBlackLevel.executeAction();
    }

    public void setBlueVideoBlackLevel(SetBlueVideoBlackLevelInput inp)
    {
        SetBlueVideoBlackLevel setBlueVideoBlackLevel = new SetBlueVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        setBlueVideoBlackLevel.executeAction();
    }

    public void setVolumeDB(SetVolumeDBInput inp)
    {
        SetVolumeDB setVolumeDB = new SetVolumeDB(renderingControlService, inp, upnpService.getControlPoint());
        setVolumeDB.executeAction();
    }

    public void selectPreset(SelectPresetInput inp)
    {
        SelectPreset selectPreset = new SelectPreset(renderingControlService, inp, upnpService.getControlPoint());
        selectPreset.executeAction();
    }

    public GetRedVideoGainOutput getRedVideoGain(GetRedVideoGainInput inp)
    {
        GetRedVideoGain getRedVideoGain = new GetRedVideoGain(renderingControlService, inp, upnpService.getControlPoint());
        GetRedVideoGainOutput res = getRedVideoGain.executeAction();
        return res;        
    }

    public GetBlueVideoBlackLevelOutput getBlueVideoBlackLevel(GetBlueVideoBlackLevelInput inp)
    {
        GetBlueVideoBlackLevel getBlueVideoBlackLevel = new GetBlueVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        GetBlueVideoBlackLevelOutput res = getBlueVideoBlackLevel.executeAction();
        return res;        
    }

    public GetSharpnessOutput getSharpness(GetSharpnessInput inp)
    {
        GetSharpness getSharpness = new GetSharpness(renderingControlService, inp, upnpService.getControlPoint());
        GetSharpnessOutput res = getSharpness.executeAction();
        return res;        
    }

    public void setSharpness(SetSharpnessInput inp)
    {
        SetSharpness setSharpness = new SetSharpness(renderingControlService, inp, upnpService.getControlPoint());
        setSharpness.executeAction();
    }

    public GetGreenVideoBlackLevelOutput getGreenVideoBlackLevel(GetGreenVideoBlackLevelInput inp)
    {
        GetGreenVideoBlackLevel getGreenVideoBlackLevel = new GetGreenVideoBlackLevel(renderingControlService, inp, upnpService.getControlPoint());
        GetGreenVideoBlackLevelOutput res = getGreenVideoBlackLevel.executeAction();
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
