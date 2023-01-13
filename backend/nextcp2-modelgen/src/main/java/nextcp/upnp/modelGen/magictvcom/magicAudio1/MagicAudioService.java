package nextcp.upnp.modelGen.magictvcom.magicAudio1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLEDControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLEDControlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlaybackClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlaybackClockSourceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMaxVolume;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMaxVolumeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalQualityOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetScreensaver;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetScreensaverInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSongcastMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSongcastModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControlSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControlSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQASupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQASupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLED;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLEDOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingModeDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingModeDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetScreensaver;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetScreensaverOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPublicDNS;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPublicDNSInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCMEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCMEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputClockSourceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetUltraSonicFilterDSD;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetUltraSonicFilterDSDInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetUltraSonicFilterDSD;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetUltraSonicFilterDSDOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.Firmware;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.FirmwareOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.FirmwareInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputClockSourceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicDNS;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicDNSOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetVolumeControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetVolumeControlInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDeemphasis;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDeemphasisOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMaxVolume;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMaxVolumeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDigitalAudioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDigitalAudioEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOauth;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOauthInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogOutLvl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogOutLvlInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.EnableServer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.EnableServerInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSpotifyEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSpotifyEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetExternalClockSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetExternalClockSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalanceSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalanceSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMagicPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMagicPlayInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetLeedhVolumeEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetLeedhVolumeEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInputLabelSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInputLabelSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzQualityOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInputLabel;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInputLabelInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTuneInLogin;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTuneInDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTuneInDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalance;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalanceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInvertPhase;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInvertPhaseOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetCustomCode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetCustomCodeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalQualityInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDeemphasis;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDeemphasisInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPBrightness;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPBrightnessOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetRAATEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetRAATEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPlaybackClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPlaybackClockSourceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetCustomCode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetCustomCodeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMQAMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMQAModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicKey;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicKeyOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAutoPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAutoPlayOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogOutLvl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogOutLvlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetNetworkLED;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetNetworkLEDInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAutoPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAutoPlayInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDetailsEx;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDetailsExOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQobuzQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQobuzQualityInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTuneInDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTuneInDetailsOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAboutString;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAboutStringOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAboutStringInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalConnectEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInvertPhase;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInvertPhaseInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogBalance;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogBalanceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeDetailsOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDefaultRadioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDefaultRadioEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicAudioVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicAudioVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQAMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQAModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCM;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCMOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDigitalAudioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDigitalAudioEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicPlayOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPBrightness;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPBrightnessInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class MagicAudioService
{
    private static Logger log = LoggerFactory.getLogger(MagicAudioService.class.getName());

    private RemoteService magicAudioService = null;

    private UpnpService upnpService = null;

    private MagicAudioServiceStateVariable magicAudioServiceStateVariable = new MagicAudioServiceStateVariable();
    
    private MagicAudioServiceSubscription subscription = null;
    
    public MagicAudioService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        magicAudioService = device.findService(new ServiceType("magictv-com", "MagicAudio"));
        if (magicAudioService != null)
        {
	        subscription = new MagicAudioServiceSubscription(magicAudioService, 600);
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'MagicAudio' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'MagicAudio' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }
    
    public void addSubscriptionEventListener(IMagicAudioServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IMagicAudioServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getMagicAudioService()
    {
        return magicAudioService;
    }    


    public GetNetworkLEDControlOutput getNetworkLEDControl()
    {
        GetNetworkLEDControl getNetworkLEDControl = new GetNetworkLEDControl(magicAudioService,  upnpService.getControlPoint());
        GetNetworkLEDControlOutput res = getNetworkLEDControl.executeAction();
        return res;        
    }

    public GetPlaybackClockSourceOutput getPlaybackClockSource()
    {
        GetPlaybackClockSource getPlaybackClockSource = new GetPlaybackClockSource(magicAudioService,  upnpService.getControlPoint());
        GetPlaybackClockSourceOutput res = getPlaybackClockSource.executeAction();
        return res;        
    }

    public void setMaxVolume(SetMaxVolumeInput inp)
    {
        SetMaxVolume setMaxVolume = new SetMaxVolume(magicAudioService, inp, upnpService.getControlPoint());
        setMaxVolume.executeAction();
    }

    public GetTidalQualityOutput getTidalQuality()
    {
        GetTidalQuality getTidalQuality = new GetTidalQuality(magicAudioService,  upnpService.getControlPoint());
        GetTidalQualityOutput res = getTidalQuality.executeAction();
        return res;        
    }

    public void setScreensaver(SetScreensaverInput inp)
    {
        SetScreensaver setScreensaver = new SetScreensaver(magicAudioService, inp, upnpService.getControlPoint());
        setScreensaver.executeAction();
    }

    public void setSongcastMode(SetSongcastModeInput inp)
    {
        SetSongcastMode setSongcastMode = new SetSongcastMode(magicAudioService, inp, upnpService.getControlPoint());
        setSongcastMode.executeAction();
    }

    public GetVolumeControlSupportOutput getVolumeControlSupport()
    {
        GetVolumeControlSupport getVolumeControlSupport = new GetVolumeControlSupport(magicAudioService,  upnpService.getControlPoint());
        GetVolumeControlSupportOutput res = getVolumeControlSupport.executeAction();
        return res;        
    }

    public GetMQASupportOutput getMQASupport()
    {
        GetMQASupport getMQASupport = new GetMQASupport(magicAudioService,  upnpService.getControlPoint());
        GetMQASupportOutput res = getMQASupport.executeAction();
        return res;        
    }

    public GetNetworkLEDOutput getNetworkLED()
    {
        GetNetworkLED getNetworkLED = new GetNetworkLED(magicAudioService,  upnpService.getControlPoint());
        GetNetworkLEDOutput res = getNetworkLED.executeAction();
        return res;        
    }

    public void setResamplingModeDetails(SetResamplingModeDetailsInput inp)
    {
        SetResamplingModeDetails setResamplingModeDetails = new SetResamplingModeDetails(magicAudioService, inp, upnpService.getControlPoint());
        setResamplingModeDetails.executeAction();
    }

    public GetScreensaverOutput getScreensaver()
    {
        GetScreensaver getScreensaver = new GetScreensaver(magicAudioService,  upnpService.getControlPoint());
        GetScreensaverOutput res = getScreensaver.executeAction();
        return res;        
    }

    public void setOutputEnable(SetOutputEnableInput inp)
    {
        SetOutputEnable setOutputEnable = new SetOutputEnable(magicAudioService, inp, upnpService.getControlPoint());
        setOutputEnable.executeAction();
    }

    public void setPublicDNS(SetPublicDNSInput inp)
    {
        SetPublicDNS setPublicDNS = new SetPublicDNS(magicAudioService, inp, upnpService.getControlPoint());
        setPublicDNS.executeAction();
    }

    public void dSDtoPCMEnable(DSDtoPCMEnableInput inp)
    {
        DSDtoPCMEnable dSDtoPCMEnable = new DSDtoPCMEnable(magicAudioService, inp, upnpService.getControlPoint());
        dSDtoPCMEnable.executeAction();
    }

    public GetOutputClockSourceOutput getOutputClockSource()
    {
        GetOutputClockSource getOutputClockSource = new GetOutputClockSource(magicAudioService,  upnpService.getControlPoint());
        GetOutputClockSourceOutput res = getOutputClockSource.executeAction();
        return res;        
    }

    public void setFPMode(SetFPModeInput inp)
    {
        SetFPMode setFPMode = new SetFPMode(magicAudioService, inp, upnpService.getControlPoint());
        setFPMode.executeAction();
    }

    public GetSongcastSupportOutput getSongcastSupport()
    {
        GetSongcastSupport getSongcastSupport = new GetSongcastSupport(magicAudioService,  upnpService.getControlPoint());
        GetSongcastSupportOutput res = getSongcastSupport.executeAction();
        return res;        
    }

    public void setUltraSonicFilterDSD(SetUltraSonicFilterDSDInput inp)
    {
        SetUltraSonicFilterDSD setUltraSonicFilterDSD = new SetUltraSonicFilterDSD(magicAudioService, inp, upnpService.getControlPoint());
        setUltraSonicFilterDSD.executeAction();
    }

    public GetSpotifyVerOutput getSpotifyVer()
    {
        GetSpotifyVer getSpotifyVer = new GetSpotifyVer(magicAudioService,  upnpService.getControlPoint());
        GetSpotifyVerOutput res = getSpotifyVer.executeAction();
        return res;        
    }

    public GetUltraSonicFilterDSDOutput getUltraSonicFilterDSD()
    {
        GetUltraSonicFilterDSD getUltraSonicFilterDSD = new GetUltraSonicFilterDSD(magicAudioService,  upnpService.getControlPoint());
        GetUltraSonicFilterDSDOutput res = getUltraSonicFilterDSD.executeAction();
        return res;        
    }

    public FirmwareOutput firmware(FirmwareInput inp)
    {
        Firmware firmware = new Firmware(magicAudioService, inp, upnpService.getControlPoint());
        FirmwareOutput res = firmware.executeAction();
        return res;        
    }

    public void setOutputClockSource(SetOutputClockSourceInput inp)
    {
        SetOutputClockSource setOutputClockSource = new SetOutputClockSource(magicAudioService, inp, upnpService.getControlPoint());
        setOutputClockSource.executeAction();
    }

    public GetPublicDNSOutput getPublicDNS()
    {
        GetPublicDNS getPublicDNS = new GetPublicDNS(magicAudioService,  upnpService.getControlPoint());
        GetPublicDNSOutput res = getPublicDNS.executeAction();
        return res;        
    }

    public void setVolumeControl(SetVolumeControlInput inp)
    {
        SetVolumeControl setVolumeControl = new SetVolumeControl(magicAudioService, inp, upnpService.getControlPoint());
        setVolumeControl.executeAction();
    }

    public GetLeedhVolumeSupportOutput getLeedhVolumeSupport()
    {
        GetLeedhVolumeSupport getLeedhVolumeSupport = new GetLeedhVolumeSupport(magicAudioService,  upnpService.getControlPoint());
        GetLeedhVolumeSupportOutput res = getLeedhVolumeSupport.executeAction();
        return res;        
    }

    public GetDeemphasisOutput getDeemphasis()
    {
        GetDeemphasis getDeemphasis = new GetDeemphasis(magicAudioService,  upnpService.getControlPoint());
        GetDeemphasisOutput res = getDeemphasis.executeAction();
        return res;        
    }

    public GetMaxVolumeOutput getMaxVolume()
    {
        GetMaxVolume getMaxVolume = new GetMaxVolume(magicAudioService,  upnpService.getControlPoint());
        GetMaxVolumeOutput res = getMaxVolume.executeAction();
        return res;        
    }

    public GetDigitalAudioEnableOutput getDigitalAudioEnable()
    {
        GetDigitalAudioEnable getDigitalAudioEnable = new GetDigitalAudioEnable(magicAudioService,  upnpService.getControlPoint());
        GetDigitalAudioEnableOutput res = getDigitalAudioEnable.executeAction();
        return res;        
    }

    public void setOauth(SetOauthInput inp)
    {
        SetOauth setOauth = new SetOauth(magicAudioService, inp, upnpService.getControlPoint());
        setOauth.executeAction();
    }

    public void setAnalogOutLvl(SetAnalogOutLvlInput inp)
    {
        SetAnalogOutLvl setAnalogOutLvl = new SetAnalogOutLvl(magicAudioService, inp, upnpService.getControlPoint());
        setAnalogOutLvl.executeAction();
    }

    public void enableServer(EnableServerInput inp)
    {
        EnableServer enableServer = new EnableServer(magicAudioService, inp, upnpService.getControlPoint());
        enableServer.executeAction();
    }

    public void setSpotifyEnable(SetSpotifyEnableInput inp)
    {
        SetSpotifyEnable setSpotifyEnable = new SetSpotifyEnable(magicAudioService, inp, upnpService.getControlPoint());
        setSpotifyEnable.executeAction();
    }

    public GetExternalClockSupportOutput getExternalClockSupport()
    {
        GetExternalClockSupport getExternalClockSupport = new GetExternalClockSupport(magicAudioService,  upnpService.getControlPoint());
        GetExternalClockSupportOutput res = getExternalClockSupport.executeAction();
        return res;        
    }

    public GetDefaultRadioEnableOutput getDefaultRadioEnable()
    {
        GetDefaultRadioEnable getDefaultRadioEnable = new GetDefaultRadioEnable(magicAudioService,  upnpService.getControlPoint());
        GetDefaultRadioEnableOutput res = getDefaultRadioEnable.executeAction();
        return res;        
    }

    public GetTidalConnectEnableOutput getTidalConnectEnable()
    {
        GetTidalConnectEnable getTidalConnectEnable = new GetTidalConnectEnable(magicAudioService,  upnpService.getControlPoint());
        GetTidalConnectEnableOutput res = getTidalConnectEnable.executeAction();
        return res;        
    }

    public GetLeedhVolumeEnableOutput getLeedhVolumeEnable()
    {
        GetLeedhVolumeEnable getLeedhVolumeEnable = new GetLeedhVolumeEnable(magicAudioService,  upnpService.getControlPoint());
        GetLeedhVolumeEnableOutput res = getLeedhVolumeEnable.executeAction();
        return res;        
    }

    public GetAnalogBalanceSupportOutput getAnalogBalanceSupport()
    {
        GetAnalogBalanceSupport getAnalogBalanceSupport = new GetAnalogBalanceSupport(magicAudioService,  upnpService.getControlPoint());
        GetAnalogBalanceSupportOutput res = getAnalogBalanceSupport.executeAction();
        return res;        
    }

    public void setMagicPlay(SetMagicPlayInput inp)
    {
        SetMagicPlay setMagicPlay = new SetMagicPlay(magicAudioService, inp, upnpService.getControlPoint());
        setMagicPlay.executeAction();
    }

    public void setLeedhVolumeEnable(SetLeedhVolumeEnableInput inp)
    {
        SetLeedhVolumeEnable setLeedhVolumeEnable = new SetLeedhVolumeEnable(magicAudioService, inp, upnpService.getControlPoint());
        setLeedhVolumeEnable.executeAction();
    }

    public GetInputLabelSupportOutput getInputLabelSupport()
    {
        GetInputLabelSupport getInputLabelSupport = new GetInputLabelSupport(magicAudioService,  upnpService.getControlPoint());
        GetInputLabelSupportOutput res = getInputLabelSupport.executeAction();
        return res;        
    }

    public GetQobuzQualityOutput getQobuzQuality()
    {
        GetQobuzQuality getQobuzQuality = new GetQobuzQuality(magicAudioService,  upnpService.getControlPoint());
        GetQobuzQualityOutput res = getQobuzQuality.executeAction();
        return res;        
    }

    public void setInputLabel(SetInputLabelInput inp)
    {
        SetInputLabel setInputLabel = new SetInputLabel(magicAudioService, inp, upnpService.getControlPoint());
        setInputLabel.executeAction();
    }

    public void setTuneInLogin()
    {
        SetTuneInLogin setTuneInLogin = new SetTuneInLogin(magicAudioService,  upnpService.getControlPoint());
        setTuneInLogin.executeAction();
    }

    public void setTuneInDetails(SetTuneInDetailsInput inp)
    {
        SetTuneInDetails setTuneInDetails = new SetTuneInDetails(magicAudioService, inp, upnpService.getControlPoint());
        setTuneInDetails.executeAction();
    }

    public GetAnalogBalanceOutput getAnalogBalance()
    {
        GetAnalogBalance getAnalogBalance = new GetAnalogBalance(magicAudioService,  upnpService.getControlPoint());
        GetAnalogBalanceOutput res = getAnalogBalance.executeAction();
        return res;        
    }

    public GetDefaultRadioSupportOutput getDefaultRadioSupport()
    {
        GetDefaultRadioSupport getDefaultRadioSupport = new GetDefaultRadioSupport(magicAudioService,  upnpService.getControlPoint());
        GetDefaultRadioSupportOutput res = getDefaultRadioSupport.executeAction();
        return res;        
    }

    public GetInvertPhaseOutput getInvertPhase()
    {
        GetInvertPhase getInvertPhase = new GetInvertPhase(magicAudioService,  upnpService.getControlPoint());
        GetInvertPhaseOutput res = getInvertPhase.executeAction();
        return res;        
    }

    public GetTidalConnectSupportOutput getTidalConnectSupport()
    {
        GetTidalConnectSupport getTidalConnectSupport = new GetTidalConnectSupport(magicAudioService,  upnpService.getControlPoint());
        GetTidalConnectSupportOutput res = getTidalConnectSupport.executeAction();
        return res;        
    }

    public GetSongcastModeOutput getSongcastMode()
    {
        GetSongcastMode getSongcastMode = new GetSongcastMode(magicAudioService,  upnpService.getControlPoint());
        GetSongcastModeOutput res = getSongcastMode.executeAction();
        return res;        
    }

    public GetOutputEnableOutput getOutputEnable(GetOutputEnableInput inp)
    {
        GetOutputEnable getOutputEnable = new GetOutputEnable(magicAudioService, inp, upnpService.getControlPoint());
        GetOutputEnableOutput res = getOutputEnable.executeAction();
        return res;        
    }

    public GetCustomCodeOutput getCustomCode()
    {
        GetCustomCode getCustomCode = new GetCustomCode(magicAudioService,  upnpService.getControlPoint());
        GetCustomCodeOutput res = getCustomCode.executeAction();
        return res;        
    }

    public void setTidalQuality(SetTidalQualityInput inp)
    {
        SetTidalQuality setTidalQuality = new SetTidalQuality(magicAudioService, inp, upnpService.getControlPoint());
        setTidalQuality.executeAction();
    }

    public void setDeemphasis(SetDeemphasisInput inp)
    {
        SetDeemphasis setDeemphasis = new SetDeemphasis(magicAudioService, inp, upnpService.getControlPoint());
        setDeemphasis.executeAction();
    }

    public GetFPBrightnessOutput getFPBrightness()
    {
        GetFPBrightness getFPBrightness = new GetFPBrightness(magicAudioService,  upnpService.getControlPoint());
        GetFPBrightnessOutput res = getFPBrightness.executeAction();
        return res;        
    }

    public GetVolumeControlOutput getVolumeControl()
    {
        GetVolumeControl getVolumeControl = new GetVolumeControl(magicAudioService,  upnpService.getControlPoint());
        GetVolumeControlOutput res = getVolumeControl.executeAction();
        return res;        
    }

    public void setRAATEnable(SetRAATEnableInput inp)
    {
        SetRAATEnable setRAATEnable = new SetRAATEnable(magicAudioService, inp, upnpService.getControlPoint());
        setRAATEnable.executeAction();
    }

    public void setPlaybackClockSource(SetPlaybackClockSourceInput inp)
    {
        SetPlaybackClockSource setPlaybackClockSource = new SetPlaybackClockSource(magicAudioService, inp, upnpService.getControlPoint());
        setPlaybackClockSource.executeAction();
    }

    public void setCustomCode(SetCustomCodeInput inp)
    {
        SetCustomCode setCustomCode = new SetCustomCode(magicAudioService, inp, upnpService.getControlPoint());
        setCustomCode.executeAction();
    }

    public void setMQAMode(SetMQAModeInput inp)
    {
        SetMQAMode setMQAMode = new SetMQAMode(magicAudioService, inp, upnpService.getControlPoint());
        setMQAMode.executeAction();
    }

    public GetPublicKeyOutput getPublicKey()
    {
        GetPublicKey getPublicKey = new GetPublicKey(magicAudioService,  upnpService.getControlPoint());
        GetPublicKeyOutput res = getPublicKey.executeAction();
        return res;        
    }

    public void setResamplingMode(SetResamplingModeInput inp)
    {
        SetResamplingMode setResamplingMode = new SetResamplingMode(magicAudioService, inp, upnpService.getControlPoint());
        setResamplingMode.executeAction();
    }

    public GetAutoPlayOutput getAutoPlay()
    {
        GetAutoPlay getAutoPlay = new GetAutoPlay(magicAudioService,  upnpService.getControlPoint());
        GetAutoPlayOutput res = getAutoPlay.executeAction();
        return res;        
    }

    public GetAnalogOutLvlOutput getAnalogOutLvl()
    {
        GetAnalogOutLvl getAnalogOutLvl = new GetAnalogOutLvl(magicAudioService,  upnpService.getControlPoint());
        GetAnalogOutLvlOutput res = getAnalogOutLvl.executeAction();
        return res;        
    }

    public GetResamplingModeOutput getResamplingMode()
    {
        GetResamplingMode getResamplingMode = new GetResamplingMode(magicAudioService,  upnpService.getControlPoint());
        GetResamplingModeOutput res = getResamplingMode.executeAction();
        return res;        
    }

    public GetRAATEnableOutput getRAATEnable()
    {
        GetRAATEnable getRAATEnable = new GetRAATEnable(magicAudioService,  upnpService.getControlPoint());
        GetRAATEnableOutput res = getRAATEnable.executeAction();
        return res;        
    }

    public void setNetworkLED(SetNetworkLEDInput inp)
    {
        SetNetworkLED setNetworkLED = new SetNetworkLED(magicAudioService, inp, upnpService.getControlPoint());
        setNetworkLED.executeAction();
    }

    public void setAutoPlay(SetAutoPlayInput inp)
    {
        SetAutoPlay setAutoPlay = new SetAutoPlay(magicAudioService, inp, upnpService.getControlPoint());
        setAutoPlay.executeAction();
    }

    public GetRAATVerOutput getRAATVer()
    {
        GetRAATVer getRAATVer = new GetRAATVer(magicAudioService,  upnpService.getControlPoint());
        GetRAATVerOutput res = getRAATVer.executeAction();
        return res;        
    }

    public GetFPModeOutput getFPMode()
    {
        GetFPMode getFPMode = new GetFPMode(magicAudioService,  upnpService.getControlPoint());
        GetFPModeOutput res = getFPMode.executeAction();
        return res;        
    }

    public GetDetailsExOutput getDetailsEx()
    {
        GetDetailsEx getDetailsEx = new GetDetailsEx(magicAudioService,  upnpService.getControlPoint());
        GetDetailsExOutput res = getDetailsEx.executeAction();
        return res;        
    }

    public void setQobuzQuality(SetQobuzQualityInput inp)
    {
        SetQobuzQuality setQobuzQuality = new SetQobuzQuality(magicAudioService, inp, upnpService.getControlPoint());
        setQobuzQuality.executeAction();
    }

    public GetTuneInDetailsOutput getTuneInDetails()
    {
        GetTuneInDetails getTuneInDetails = new GetTuneInDetails(magicAudioService,  upnpService.getControlPoint());
        GetTuneInDetailsOutput res = getTuneInDetails.executeAction();
        return res;        
    }

    public GetAboutStringOutput getAboutString(GetAboutStringInput inp)
    {
        GetAboutString getAboutString = new GetAboutString(magicAudioService, inp, upnpService.getControlPoint());
        GetAboutStringOutput res = getAboutString.executeAction();
        return res;        
    }

    public void setTidalConnectEnable(SetTidalConnectEnableInput inp)
    {
        SetTidalConnectEnable setTidalConnectEnable = new SetTidalConnectEnable(magicAudioService, inp, upnpService.getControlPoint());
        setTidalConnectEnable.executeAction();
    }

    public void setInvertPhase(SetInvertPhaseInput inp)
    {
        SetInvertPhase setInvertPhase = new SetInvertPhase(magicAudioService, inp, upnpService.getControlPoint());
        setInvertPhase.executeAction();
    }

    public void setAnalogBalance(SetAnalogBalanceInput inp)
    {
        SetAnalogBalance setAnalogBalance = new SetAnalogBalance(magicAudioService, inp, upnpService.getControlPoint());
        setAnalogBalance.executeAction();
    }

    public GetResamplingModeDetailsOutput getResamplingModeDetails(GetResamplingModeDetailsInput inp)
    {
        GetResamplingModeDetails getResamplingModeDetails = new GetResamplingModeDetails(magicAudioService, inp, upnpService.getControlPoint());
        GetResamplingModeDetailsOutput res = getResamplingModeDetails.executeAction();
        return res;        
    }

    public void setDefaultRadioEnable(SetDefaultRadioEnableInput inp)
    {
        SetDefaultRadioEnable setDefaultRadioEnable = new SetDefaultRadioEnable(magicAudioService, inp, upnpService.getControlPoint());
        setDefaultRadioEnable.executeAction();
    }

    public GetMagicAudioVerOutput getMagicAudioVer()
    {
        GetMagicAudioVer getMagicAudioVer = new GetMagicAudioVer(magicAudioService,  upnpService.getControlPoint());
        GetMagicAudioVerOutput res = getMagicAudioVer.executeAction();
        return res;        
    }

    public GetSpotifyEnableOutput getSpotifyEnable()
    {
        GetSpotifyEnable getSpotifyEnable = new GetSpotifyEnable(magicAudioService,  upnpService.getControlPoint());
        GetSpotifyEnableOutput res = getSpotifyEnable.executeAction();
        return res;        
    }

    public GetMQAModeOutput getMQAMode()
    {
        GetMQAMode getMQAMode = new GetMQAMode(magicAudioService,  upnpService.getControlPoint());
        GetMQAModeOutput res = getMQAMode.executeAction();
        return res;        
    }

    public DSDtoPCMOutput dSDtoPCM()
    {
        DSDtoPCM dSDtoPCM = new DSDtoPCM(magicAudioService,  upnpService.getControlPoint());
        DSDtoPCMOutput res = dSDtoPCM.executeAction();
        return res;        
    }

    public void setDigitalAudioEnable(SetDigitalAudioEnableInput inp)
    {
        SetDigitalAudioEnable setDigitalAudioEnable = new SetDigitalAudioEnable(magicAudioService, inp, upnpService.getControlPoint());
        setDigitalAudioEnable.executeAction();
    }

    public GetMagicPlayOutput getMagicPlay()
    {
        GetMagicPlay getMagicPlay = new GetMagicPlay(magicAudioService,  upnpService.getControlPoint());
        GetMagicPlayOutput res = getMagicPlay.executeAction();
        return res;        
    }

    public void setFPBrightness(SetFPBrightnessInput inp)
    {
        SetFPBrightness setFPBrightness = new SetFPBrightness(magicAudioService, inp, upnpService.getControlPoint());
        setFPBrightness.executeAction();
    }
}
