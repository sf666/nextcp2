package nextcp.upnp.modelGen.magictvcom.magicAudio;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetNetworkLEDControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetNetworkLEDControlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetPlaybackClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetPlaybackClockSourceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetMaxVolume;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetMaxVolumeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTidalQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTidalQualityOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetScreensaver;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetScreensaverInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetSongcastMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetSongcastModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetVolumeControlSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetVolumeControlSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMQASupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMQASupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetNetworkLED;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetNetworkLEDOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetResamplingModeDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetResamplingModeDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetScreensaver;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetScreensaverOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetOutputEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetOutputEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.DSDtoPCMEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.DSDtoPCMEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetOutputClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetOutputClockSourceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSongcastSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSongcastSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetUltraSonicFilterDSD;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetUltraSonicFilterDSDInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSpotifyVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSpotifyVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetUltraSonicFilterDSD;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetUltraSonicFilterDSDOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.Firmware;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.FirmwareOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.FirmwareInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetOutputClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetOutputClockSourceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetVolumeControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetVolumeControlInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetLeedhVolumeSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetLeedhVolumeSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetDeemphasis;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetDeemphasisOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMaxVolume;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMaxVolumeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetDigitalAudioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetDigitalAudioEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetOauth;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetOauthInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetAnalogOutLvl;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetAnalogOutLvlInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.EnableServer;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.EnableServerInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetSpotifyEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetSpotifyEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetExternalClockSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetExternalClockSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTidalConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTidalConnectEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetLeedhVolumeEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetLeedhVolumeEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetMagicPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetMagicPlayInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetLeedhVolumeEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetLeedhVolumeEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetQobuzQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetQobuzQualityOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetTuneInLogin;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetTuneInDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetTuneInDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetInvertPhase;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetInvertPhaseOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTidalConnectSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTidalConnectSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSongcastMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSongcastModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetOutputEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetOutputEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetOutputEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetCustomCode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetCustomCodeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetTidalQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetTidalQualityInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetDeemphasis;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetDeemphasisInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetFPBrightness;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetFPBrightnessOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetVolumeControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetVolumeControlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetRAATEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetRAATEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetPlaybackClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetPlaybackClockSourceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetCustomCode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetCustomCodeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetMQAMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetMQAModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetPublicKey;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetPublicKeyOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetResamplingMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetResamplingModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetAutoPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetAutoPlayOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetAnalogOutLvl;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetAnalogOutLvlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetResamplingMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetResamplingModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetRAATEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetRAATEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetNetworkLED;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetNetworkLEDInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetAutoPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetAutoPlayInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetRAATVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetRAATVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetDetailsEx;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetDetailsExOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetQobuzQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetQobuzQualityInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTuneInDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetTuneInDetailsOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetAboutString;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetAboutStringOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetAboutStringInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetTidalConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetTidalConnectEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetInvertPhase;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetInvertPhaseInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetResamplingModeDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetResamplingModeDetailsOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetResamplingModeDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMagicAudioVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMagicAudioVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSpotifyEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetSpotifyEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMQAMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMQAModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.DSDtoPCM;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.DSDtoPCMOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetDigitalAudioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetDigitalAudioEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMagicPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.GetMagicPlayOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetFPBrightness;
import nextcp.upnp.modelGen.magictvcom.magicAudio.actions.SetFPBrightnessInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
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

    public GetQobuzQualityOutput getQobuzQuality()
    {
        GetQobuzQuality getQobuzQuality = new GetQobuzQuality(magicAudioService,  upnpService.getControlPoint());
        GetQobuzQualityOutput res = getQobuzQuality.executeAction();
        return res;        
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

    public GetResamplingModeDetailsOutput getResamplingModeDetails(GetResamplingModeDetailsInput inp)
    {
        GetResamplingModeDetails getResamplingModeDetails = new GetResamplingModeDetails(magicAudioService, inp, upnpService.getControlPoint());
        GetResamplingModeDetailsOutput res = getResamplingModeDetails.executeAction();
        return res;        
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
