package nextcp.upnp.modelGen.magictvcom.magicAudio1;

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

import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.ClearAppDisplayMessage;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.ClearAppDisplayMessageInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCM;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCMOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCMEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.DSDtoPCMEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.EnableServer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.EnableServerInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.Firmware;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.FirmwareOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.FirmwareInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAboutString;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAboutStringOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAboutStringInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAmazonMusicEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAmazonMusicEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAmazonMusicSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAmazonMusicSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalance;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalanceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalanceSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogBalanceSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogOutLvl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAnalogOutLvlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAppDisplayMessage;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAppDisplayMessageOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAutoPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetAutoPlayOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetControl4Enable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetControl4EnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetControl4Support;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetControl4SupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetCustomCode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetCustomCodeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDeemphasis;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDeemphasisOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDefaultRadioSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDetailsEx;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDetailsExOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDigitalAudioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetDigitalAudioEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetExternalClockSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetExternalClockSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPBrightness;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPBrightnessOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetFPModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetHDMICECEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetHDMICECEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetHDMICECSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetHDMICECSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInputLabelSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInputLabelSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInvertPhase;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetInvertPhaseOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetKKBOXEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetKKBOXEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetKKBOXSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetKKBOXSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLUFS;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLUFSOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLUFSSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLUFSSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetLeedhVolumeSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQAMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQAModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQASupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMQASupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicAudioVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicAudioVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMagicPlayOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMaxVolume;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetMaxVolumeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLED;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLEDOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLEDControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetNetworkLEDControlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputClockSourceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetOutputEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlaybackClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlaybackClockSourceOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexCode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexCodeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexUserInfo;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPlexUserInfoOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicDNS;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicDNSOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicKey;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetPublicKeyOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQPlayEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQPlayEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQPlaySupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQPlaySupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzConnectEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzConnectSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzConnectSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetQobuzQualityOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetRAATVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeDetailsOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetResamplingModeDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetScreensaver;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetScreensaverOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSongcastSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyNormalization;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyNormalizationOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyVer;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetSpotifyVerOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectEnableOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalConnectSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTidalQualityOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTuneInDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetTuneInDetailsOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetUSBSPDIFMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetUSBSPDIFModeOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetUltraSonicFilterDSD;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetUltraSonicFilterDSDOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControlOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControlSupport;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.GetVolumeControlSupportOutput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.LogoutPlex;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAmazonMusicEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAmazonMusicEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogBalance;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogBalanceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogOutLvl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAnalogOutLvlInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAutoPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetAutoPlayInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetControl4Enable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetControl4EnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetCustomCode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetCustomCodeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDeemphasis;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDeemphasisInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDefaultRadioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDefaultRadioEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDigitalAudioEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetDigitalAudioEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPBrightness;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPBrightnessInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetFPModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetHDMICECEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetHDMICECEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInputLabel;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInputLabelInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInvertPhase;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetInvertPhaseInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetKKBOXEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetKKBOXEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetLUFS;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetLUFSInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetLeedhVolumeEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetLeedhVolumeEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMQAMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMQAModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMagicPlay;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMagicPlayInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMaxVolume;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetMaxVolumeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetNetworkLED;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetNetworkLEDInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOauth;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOauthInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputClockSourceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetOutputEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPlaybackClockSource;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPlaybackClockSourceInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPlexEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPlexEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPublicDNS;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetPublicDNSInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQPlayEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQPlayEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQobuzConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQobuzConnectEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQobuzQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetQobuzQualityInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetRAATEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetRAATEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingModeDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetResamplingModeDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetScreensaver;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetScreensaverInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSongcastMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSongcastModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSpotifyEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSpotifyEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSpotifyNormalization;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetSpotifyNormalizationInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalConnectEnable;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalConnectEnableInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalQuality;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTidalQualityInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTuneInDetails;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTuneInDetailsInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetTuneInLogin;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetUSBSPDIFMode;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetUSBSPDIFModeInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetUltraSonicFilterDSD;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetUltraSonicFilterDSDInput;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetVolumeControl;
import nextcp.upnp.modelGen.magictvcom.magicAudio1.actions.SetVolumeControlInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class MagicAudioService
{
    private static Logger log = LoggerFactory.getLogger(MagicAudioService.class.getName());

    private RemoteService magicAudioService = null;

    private UpnpService upnpService = null;

//    private MagicAudioServiceStateVariable magicAudioServiceStateVariable = new MagicAudioServiceStateVariable();
    
    private MagicAudioServiceSubscription subscription = null;
    
    public MagicAudioService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public MagicAudioService(UpnpService upnpService, RemoteDevice device, IMagicAudioServiceEventListener listener)
    {
        this.upnpService = upnpService;
        magicAudioService = device.findService(new ServiceType("magictv-com", "MagicAudio"));
        if (magicAudioService != null)
        {
	        subscription = new MagicAudioServiceSubscription(magicAudioService, 600);
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
	
	        log.info(String.format("initialized service 'MagicAudio' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'MagicAudio' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IMagicAudioServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IMagicAudioServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getMagicAudioService()
    {
        return magicAudioService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return magicAudioService != null && magicAudioService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public void clearAppDisplayMessage(ClearAppDisplayMessageInput inp)
    {
        if (!hasAction("ClearAppDisplayMessage"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action ClearAppDisplayMessage of service MagicAudio");
        }
        ClearAppDisplayMessage clearAppDisplayMessage = new ClearAppDisplayMessage(magicAudioService, inp, upnpService.getControlPoint());
        clearAppDisplayMessage.executeAction();
    }

    public DSDtoPCMOutput dSDtoPCM()
    {
        if (!hasAction("DSDtoPCM"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DSDtoPCM of service MagicAudio");
        }
        DSDtoPCM dSDtoPCM = new DSDtoPCM(magicAudioService,  upnpService.getControlPoint());
        DSDtoPCMOutput res = dSDtoPCM.executeAction();
        return res;        
    }

    public void dSDtoPCMEnable(DSDtoPCMEnableInput inp)
    {
        if (!hasAction("DSDtoPCMEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action DSDtoPCMEnable of service MagicAudio");
        }
        DSDtoPCMEnable dSDtoPCMEnable = new DSDtoPCMEnable(magicAudioService, inp, upnpService.getControlPoint());
        dSDtoPCMEnable.executeAction();
    }

    public void enableServer(EnableServerInput inp)
    {
        if (!hasAction("EnableServer"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action EnableServer of service MagicAudio");
        }
        EnableServer enableServer = new EnableServer(magicAudioService, inp, upnpService.getControlPoint());
        enableServer.executeAction();
    }

    public FirmwareOutput firmware(FirmwareInput inp)
    {
        if (!hasAction("Firmware"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Firmware of service MagicAudio");
        }
        Firmware firmware = new Firmware(magicAudioService, inp, upnpService.getControlPoint());
        FirmwareOutput res = firmware.executeAction();
        return res;        
    }

    public GetAboutStringOutput getAboutString(GetAboutStringInput inp)
    {
        if (!hasAction("GetAboutString"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAboutString of service MagicAudio");
        }
        GetAboutString getAboutString = new GetAboutString(magicAudioService, inp, upnpService.getControlPoint());
        GetAboutStringOutput res = getAboutString.executeAction();
        return res;        
    }

    public GetAmazonMusicEnableOutput getAmazonMusicEnable()
    {
        if (!hasAction("GetAmazonMusicEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAmazonMusicEnable of service MagicAudio");
        }
        GetAmazonMusicEnable getAmazonMusicEnable = new GetAmazonMusicEnable(magicAudioService,  upnpService.getControlPoint());
        GetAmazonMusicEnableOutput res = getAmazonMusicEnable.executeAction();
        return res;        
    }

    public GetAmazonMusicSupportOutput getAmazonMusicSupport()
    {
        if (!hasAction("GetAmazonMusicSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAmazonMusicSupport of service MagicAudio");
        }
        GetAmazonMusicSupport getAmazonMusicSupport = new GetAmazonMusicSupport(magicAudioService,  upnpService.getControlPoint());
        GetAmazonMusicSupportOutput res = getAmazonMusicSupport.executeAction();
        return res;        
    }

    public GetAnalogBalanceOutput getAnalogBalance()
    {
        if (!hasAction("GetAnalogBalance"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAnalogBalance of service MagicAudio");
        }
        GetAnalogBalance getAnalogBalance = new GetAnalogBalance(magicAudioService,  upnpService.getControlPoint());
        GetAnalogBalanceOutput res = getAnalogBalance.executeAction();
        return res;        
    }

    public GetAnalogBalanceSupportOutput getAnalogBalanceSupport()
    {
        if (!hasAction("GetAnalogBalanceSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAnalogBalanceSupport of service MagicAudio");
        }
        GetAnalogBalanceSupport getAnalogBalanceSupport = new GetAnalogBalanceSupport(magicAudioService,  upnpService.getControlPoint());
        GetAnalogBalanceSupportOutput res = getAnalogBalanceSupport.executeAction();
        return res;        
    }

    public GetAnalogOutLvlOutput getAnalogOutLvl()
    {
        if (!hasAction("GetAnalogOutLvl"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAnalogOutLvl of service MagicAudio");
        }
        GetAnalogOutLvl getAnalogOutLvl = new GetAnalogOutLvl(magicAudioService,  upnpService.getControlPoint());
        GetAnalogOutLvlOutput res = getAnalogOutLvl.executeAction();
        return res;        
    }

    public GetAppDisplayMessageOutput getAppDisplayMessage()
    {
        if (!hasAction("GetAppDisplayMessage"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAppDisplayMessage of service MagicAudio");
        }
        GetAppDisplayMessage getAppDisplayMessage = new GetAppDisplayMessage(magicAudioService,  upnpService.getControlPoint());
        GetAppDisplayMessageOutput res = getAppDisplayMessage.executeAction();
        return res;        
    }

    public GetAutoPlayOutput getAutoPlay()
    {
        if (!hasAction("GetAutoPlay"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetAutoPlay of service MagicAudio");
        }
        GetAutoPlay getAutoPlay = new GetAutoPlay(magicAudioService,  upnpService.getControlPoint());
        GetAutoPlayOutput res = getAutoPlay.executeAction();
        return res;        
    }

    public GetControl4EnableOutput getControl4Enable()
    {
        if (!hasAction("GetControl4Enable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetControl4Enable of service MagicAudio");
        }
        GetControl4Enable getControl4Enable = new GetControl4Enable(magicAudioService,  upnpService.getControlPoint());
        GetControl4EnableOutput res = getControl4Enable.executeAction();
        return res;        
    }

    public GetControl4SupportOutput getControl4Support()
    {
        if (!hasAction("GetControl4Support"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetControl4Support of service MagicAudio");
        }
        GetControl4Support getControl4Support = new GetControl4Support(magicAudioService,  upnpService.getControlPoint());
        GetControl4SupportOutput res = getControl4Support.executeAction();
        return res;        
    }

    public GetCustomCodeOutput getCustomCode()
    {
        if (!hasAction("GetCustomCode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetCustomCode of service MagicAudio");
        }
        GetCustomCode getCustomCode = new GetCustomCode(magicAudioService,  upnpService.getControlPoint());
        GetCustomCodeOutput res = getCustomCode.executeAction();
        return res;        
    }

    public GetDeemphasisOutput getDeemphasis()
    {
        if (!hasAction("GetDeemphasis"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDeemphasis of service MagicAudio");
        }
        GetDeemphasis getDeemphasis = new GetDeemphasis(magicAudioService,  upnpService.getControlPoint());
        GetDeemphasisOutput res = getDeemphasis.executeAction();
        return res;        
    }

    public GetDefaultRadioEnableOutput getDefaultRadioEnable()
    {
        if (!hasAction("GetDefaultRadioEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDefaultRadioEnable of service MagicAudio");
        }
        GetDefaultRadioEnable getDefaultRadioEnable = new GetDefaultRadioEnable(magicAudioService,  upnpService.getControlPoint());
        GetDefaultRadioEnableOutput res = getDefaultRadioEnable.executeAction();
        return res;        
    }

    public GetDefaultRadioSupportOutput getDefaultRadioSupport()
    {
        if (!hasAction("GetDefaultRadioSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDefaultRadioSupport of service MagicAudio");
        }
        GetDefaultRadioSupport getDefaultRadioSupport = new GetDefaultRadioSupport(magicAudioService,  upnpService.getControlPoint());
        GetDefaultRadioSupportOutput res = getDefaultRadioSupport.executeAction();
        return res;        
    }

    public GetDetailsExOutput getDetailsEx()
    {
        if (!hasAction("GetDetailsEx"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDetailsEx of service MagicAudio");
        }
        GetDetailsEx getDetailsEx = new GetDetailsEx(magicAudioService,  upnpService.getControlPoint());
        GetDetailsExOutput res = getDetailsEx.executeAction();
        return res;        
    }

    public GetDigitalAudioEnableOutput getDigitalAudioEnable()
    {
        if (!hasAction("GetDigitalAudioEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetDigitalAudioEnable of service MagicAudio");
        }
        GetDigitalAudioEnable getDigitalAudioEnable = new GetDigitalAudioEnable(magicAudioService,  upnpService.getControlPoint());
        GetDigitalAudioEnableOutput res = getDigitalAudioEnable.executeAction();
        return res;        
    }

    public GetExternalClockSupportOutput getExternalClockSupport()
    {
        if (!hasAction("GetExternalClockSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetExternalClockSupport of service MagicAudio");
        }
        GetExternalClockSupport getExternalClockSupport = new GetExternalClockSupport(magicAudioService,  upnpService.getControlPoint());
        GetExternalClockSupportOutput res = getExternalClockSupport.executeAction();
        return res;        
    }

    public GetFPBrightnessOutput getFPBrightness()
    {
        if (!hasAction("GetFPBrightness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetFPBrightness of service MagicAudio");
        }
        GetFPBrightness getFPBrightness = new GetFPBrightness(magicAudioService,  upnpService.getControlPoint());
        GetFPBrightnessOutput res = getFPBrightness.executeAction();
        return res;        
    }

    public GetFPModeOutput getFPMode()
    {
        if (!hasAction("GetFPMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetFPMode of service MagicAudio");
        }
        GetFPMode getFPMode = new GetFPMode(magicAudioService,  upnpService.getControlPoint());
        GetFPModeOutput res = getFPMode.executeAction();
        return res;        
    }

    public GetHDMICECEnableOutput getHDMICECEnable()
    {
        if (!hasAction("GetHDMICECEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetHDMICECEnable of service MagicAudio");
        }
        GetHDMICECEnable getHDMICECEnable = new GetHDMICECEnable(magicAudioService,  upnpService.getControlPoint());
        GetHDMICECEnableOutput res = getHDMICECEnable.executeAction();
        return res;        
    }

    public GetHDMICECSupportOutput getHDMICECSupport()
    {
        if (!hasAction("GetHDMICECSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetHDMICECSupport of service MagicAudio");
        }
        GetHDMICECSupport getHDMICECSupport = new GetHDMICECSupport(magicAudioService,  upnpService.getControlPoint());
        GetHDMICECSupportOutput res = getHDMICECSupport.executeAction();
        return res;        
    }

    public GetInputLabelSupportOutput getInputLabelSupport()
    {
        if (!hasAction("GetInputLabelSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetInputLabelSupport of service MagicAudio");
        }
        GetInputLabelSupport getInputLabelSupport = new GetInputLabelSupport(magicAudioService,  upnpService.getControlPoint());
        GetInputLabelSupportOutput res = getInputLabelSupport.executeAction();
        return res;        
    }

    public GetInvertPhaseOutput getInvertPhase()
    {
        if (!hasAction("GetInvertPhase"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetInvertPhase of service MagicAudio");
        }
        GetInvertPhase getInvertPhase = new GetInvertPhase(magicAudioService,  upnpService.getControlPoint());
        GetInvertPhaseOutput res = getInvertPhase.executeAction();
        return res;        
    }

    public GetKKBOXEnableOutput getKKBOXEnable()
    {
        if (!hasAction("GetKKBOXEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetKKBOXEnable of service MagicAudio");
        }
        GetKKBOXEnable getKKBOXEnable = new GetKKBOXEnable(magicAudioService,  upnpService.getControlPoint());
        GetKKBOXEnableOutput res = getKKBOXEnable.executeAction();
        return res;        
    }

    public GetKKBOXSupportOutput getKKBOXSupport()
    {
        if (!hasAction("GetKKBOXSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetKKBOXSupport of service MagicAudio");
        }
        GetKKBOXSupport getKKBOXSupport = new GetKKBOXSupport(magicAudioService,  upnpService.getControlPoint());
        GetKKBOXSupportOutput res = getKKBOXSupport.executeAction();
        return res;        
    }

    public GetLUFSOutput getLUFS()
    {
        if (!hasAction("GetLUFS"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetLUFS of service MagicAudio");
        }
        GetLUFS getLUFS = new GetLUFS(magicAudioService,  upnpService.getControlPoint());
        GetLUFSOutput res = getLUFS.executeAction();
        return res;        
    }

    public GetLUFSSupportOutput getLUFSSupport()
    {
        if (!hasAction("GetLUFSSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetLUFSSupport of service MagicAudio");
        }
        GetLUFSSupport getLUFSSupport = new GetLUFSSupport(magicAudioService,  upnpService.getControlPoint());
        GetLUFSSupportOutput res = getLUFSSupport.executeAction();
        return res;        
    }

    public GetLeedhVolumeEnableOutput getLeedhVolumeEnable()
    {
        if (!hasAction("GetLeedhVolumeEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetLeedhVolumeEnable of service MagicAudio");
        }
        GetLeedhVolumeEnable getLeedhVolumeEnable = new GetLeedhVolumeEnable(magicAudioService,  upnpService.getControlPoint());
        GetLeedhVolumeEnableOutput res = getLeedhVolumeEnable.executeAction();
        return res;        
    }

    public GetLeedhVolumeSupportOutput getLeedhVolumeSupport()
    {
        if (!hasAction("GetLeedhVolumeSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetLeedhVolumeSupport of service MagicAudio");
        }
        GetLeedhVolumeSupport getLeedhVolumeSupport = new GetLeedhVolumeSupport(magicAudioService,  upnpService.getControlPoint());
        GetLeedhVolumeSupportOutput res = getLeedhVolumeSupport.executeAction();
        return res;        
    }

    public GetMQAModeOutput getMQAMode()
    {
        if (!hasAction("GetMQAMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMQAMode of service MagicAudio");
        }
        GetMQAMode getMQAMode = new GetMQAMode(magicAudioService,  upnpService.getControlPoint());
        GetMQAModeOutput res = getMQAMode.executeAction();
        return res;        
    }

    public GetMQASupportOutput getMQASupport()
    {
        if (!hasAction("GetMQASupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMQASupport of service MagicAudio");
        }
        GetMQASupport getMQASupport = new GetMQASupport(magicAudioService,  upnpService.getControlPoint());
        GetMQASupportOutput res = getMQASupport.executeAction();
        return res;        
    }

    public GetMagicAudioVerOutput getMagicAudioVer()
    {
        if (!hasAction("GetMagicAudioVer"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMagicAudioVer of service MagicAudio");
        }
        GetMagicAudioVer getMagicAudioVer = new GetMagicAudioVer(magicAudioService,  upnpService.getControlPoint());
        GetMagicAudioVerOutput res = getMagicAudioVer.executeAction();
        return res;        
    }

    public GetMagicPlayOutput getMagicPlay()
    {
        if (!hasAction("GetMagicPlay"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMagicPlay of service MagicAudio");
        }
        GetMagicPlay getMagicPlay = new GetMagicPlay(magicAudioService,  upnpService.getControlPoint());
        GetMagicPlayOutput res = getMagicPlay.executeAction();
        return res;        
    }

    public GetMaxVolumeOutput getMaxVolume()
    {
        if (!hasAction("GetMaxVolume"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetMaxVolume of service MagicAudio");
        }
        GetMaxVolume getMaxVolume = new GetMaxVolume(magicAudioService,  upnpService.getControlPoint());
        GetMaxVolumeOutput res = getMaxVolume.executeAction();
        return res;        
    }

    public GetNetworkLEDOutput getNetworkLED()
    {
        if (!hasAction("GetNetworkLED"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetNetworkLED of service MagicAudio");
        }
        GetNetworkLED getNetworkLED = new GetNetworkLED(magicAudioService,  upnpService.getControlPoint());
        GetNetworkLEDOutput res = getNetworkLED.executeAction();
        return res;        
    }

    public GetNetworkLEDControlOutput getNetworkLEDControl()
    {
        if (!hasAction("GetNetworkLEDControl"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetNetworkLEDControl of service MagicAudio");
        }
        GetNetworkLEDControl getNetworkLEDControl = new GetNetworkLEDControl(magicAudioService,  upnpService.getControlPoint());
        GetNetworkLEDControlOutput res = getNetworkLEDControl.executeAction();
        return res;        
    }

    public GetOutputClockSourceOutput getOutputClockSource()
    {
        if (!hasAction("GetOutputClockSource"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetOutputClockSource of service MagicAudio");
        }
        GetOutputClockSource getOutputClockSource = new GetOutputClockSource(magicAudioService,  upnpService.getControlPoint());
        GetOutputClockSourceOutput res = getOutputClockSource.executeAction();
        return res;        
    }

    public GetOutputEnableOutput getOutputEnable(GetOutputEnableInput inp)
    {
        if (!hasAction("GetOutputEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetOutputEnable of service MagicAudio");
        }
        GetOutputEnable getOutputEnable = new GetOutputEnable(magicAudioService, inp, upnpService.getControlPoint());
        GetOutputEnableOutput res = getOutputEnable.executeAction();
        return res;        
    }

    public GetPlaybackClockSourceOutput getPlaybackClockSource()
    {
        if (!hasAction("GetPlaybackClockSource"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPlaybackClockSource of service MagicAudio");
        }
        GetPlaybackClockSource getPlaybackClockSource = new GetPlaybackClockSource(magicAudioService,  upnpService.getControlPoint());
        GetPlaybackClockSourceOutput res = getPlaybackClockSource.executeAction();
        return res;        
    }

    public GetPlexCodeOutput getPlexCode()
    {
        if (!hasAction("GetPlexCode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPlexCode of service MagicAudio");
        }
        GetPlexCode getPlexCode = new GetPlexCode(magicAudioService,  upnpService.getControlPoint());
        GetPlexCodeOutput res = getPlexCode.executeAction();
        return res;        
    }

    public GetPlexEnableOutput getPlexEnable()
    {
        if (!hasAction("GetPlexEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPlexEnable of service MagicAudio");
        }
        GetPlexEnable getPlexEnable = new GetPlexEnable(magicAudioService,  upnpService.getControlPoint());
        GetPlexEnableOutput res = getPlexEnable.executeAction();
        return res;        
    }

    public GetPlexSupportOutput getPlexSupport()
    {
        if (!hasAction("GetPlexSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPlexSupport of service MagicAudio");
        }
        GetPlexSupport getPlexSupport = new GetPlexSupport(magicAudioService,  upnpService.getControlPoint());
        GetPlexSupportOutput res = getPlexSupport.executeAction();
        return res;        
    }

    public GetPlexUserInfoOutput getPlexUserInfo()
    {
        if (!hasAction("GetPlexUserInfo"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPlexUserInfo of service MagicAudio");
        }
        GetPlexUserInfo getPlexUserInfo = new GetPlexUserInfo(magicAudioService,  upnpService.getControlPoint());
        GetPlexUserInfoOutput res = getPlexUserInfo.executeAction();
        return res;        
    }

    public GetPublicDNSOutput getPublicDNS()
    {
        if (!hasAction("GetPublicDNS"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPublicDNS of service MagicAudio");
        }
        GetPublicDNS getPublicDNS = new GetPublicDNS(magicAudioService,  upnpService.getControlPoint());
        GetPublicDNSOutput res = getPublicDNS.executeAction();
        return res;        
    }

    public GetPublicKeyOutput getPublicKey()
    {
        if (!hasAction("GetPublicKey"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetPublicKey of service MagicAudio");
        }
        GetPublicKey getPublicKey = new GetPublicKey(magicAudioService,  upnpService.getControlPoint());
        GetPublicKeyOutput res = getPublicKey.executeAction();
        return res;        
    }

    public GetQPlayEnableOutput getQPlayEnable()
    {
        if (!hasAction("GetQPlayEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetQPlayEnable of service MagicAudio");
        }
        GetQPlayEnable getQPlayEnable = new GetQPlayEnable(magicAudioService,  upnpService.getControlPoint());
        GetQPlayEnableOutput res = getQPlayEnable.executeAction();
        return res;        
    }

    public GetQPlaySupportOutput getQPlaySupport()
    {
        if (!hasAction("GetQPlaySupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetQPlaySupport of service MagicAudio");
        }
        GetQPlaySupport getQPlaySupport = new GetQPlaySupport(magicAudioService,  upnpService.getControlPoint());
        GetQPlaySupportOutput res = getQPlaySupport.executeAction();
        return res;        
    }

    public GetQobuzConnectEnableOutput getQobuzConnectEnable()
    {
        if (!hasAction("GetQobuzConnectEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetQobuzConnectEnable of service MagicAudio");
        }
        GetQobuzConnectEnable getQobuzConnectEnable = new GetQobuzConnectEnable(magicAudioService,  upnpService.getControlPoint());
        GetQobuzConnectEnableOutput res = getQobuzConnectEnable.executeAction();
        return res;        
    }

    public GetQobuzConnectSupportOutput getQobuzConnectSupport()
    {
        if (!hasAction("GetQobuzConnectSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetQobuzConnectSupport of service MagicAudio");
        }
        GetQobuzConnectSupport getQobuzConnectSupport = new GetQobuzConnectSupport(magicAudioService,  upnpService.getControlPoint());
        GetQobuzConnectSupportOutput res = getQobuzConnectSupport.executeAction();
        return res;        
    }

    public GetQobuzQualityOutput getQobuzQuality()
    {
        if (!hasAction("GetQobuzQuality"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetQobuzQuality of service MagicAudio");
        }
        GetQobuzQuality getQobuzQuality = new GetQobuzQuality(magicAudioService,  upnpService.getControlPoint());
        GetQobuzQualityOutput res = getQobuzQuality.executeAction();
        return res;        
    }

    public GetRAATEnableOutput getRAATEnable()
    {
        if (!hasAction("GetRAATEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetRAATEnable of service MagicAudio");
        }
        GetRAATEnable getRAATEnable = new GetRAATEnable(magicAudioService,  upnpService.getControlPoint());
        GetRAATEnableOutput res = getRAATEnable.executeAction();
        return res;        
    }

    public GetRAATVerOutput getRAATVer()
    {
        if (!hasAction("GetRAATVer"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetRAATVer of service MagicAudio");
        }
        GetRAATVer getRAATVer = new GetRAATVer(magicAudioService,  upnpService.getControlPoint());
        GetRAATVerOutput res = getRAATVer.executeAction();
        return res;        
    }

    public GetResamplingModeOutput getResamplingMode()
    {
        if (!hasAction("GetResamplingMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetResamplingMode of service MagicAudio");
        }
        GetResamplingMode getResamplingMode = new GetResamplingMode(magicAudioService,  upnpService.getControlPoint());
        GetResamplingModeOutput res = getResamplingMode.executeAction();
        return res;        
    }

    public GetResamplingModeDetailsOutput getResamplingModeDetails(GetResamplingModeDetailsInput inp)
    {
        if (!hasAction("GetResamplingModeDetails"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetResamplingModeDetails of service MagicAudio");
        }
        GetResamplingModeDetails getResamplingModeDetails = new GetResamplingModeDetails(magicAudioService, inp, upnpService.getControlPoint());
        GetResamplingModeDetailsOutput res = getResamplingModeDetails.executeAction();
        return res;        
    }

    public GetScreensaverOutput getScreensaver()
    {
        if (!hasAction("GetScreensaver"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetScreensaver of service MagicAudio");
        }
        GetScreensaver getScreensaver = new GetScreensaver(magicAudioService,  upnpService.getControlPoint());
        GetScreensaverOutput res = getScreensaver.executeAction();
        return res;        
    }

    public GetSongcastModeOutput getSongcastMode()
    {
        if (!hasAction("GetSongcastMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSongcastMode of service MagicAudio");
        }
        GetSongcastMode getSongcastMode = new GetSongcastMode(magicAudioService,  upnpService.getControlPoint());
        GetSongcastModeOutput res = getSongcastMode.executeAction();
        return res;        
    }

    public GetSongcastSupportOutput getSongcastSupport()
    {
        if (!hasAction("GetSongcastSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSongcastSupport of service MagicAudio");
        }
        GetSongcastSupport getSongcastSupport = new GetSongcastSupport(magicAudioService,  upnpService.getControlPoint());
        GetSongcastSupportOutput res = getSongcastSupport.executeAction();
        return res;        
    }

    public GetSpotifyEnableOutput getSpotifyEnable()
    {
        if (!hasAction("GetSpotifyEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSpotifyEnable of service MagicAudio");
        }
        GetSpotifyEnable getSpotifyEnable = new GetSpotifyEnable(magicAudioService,  upnpService.getControlPoint());
        GetSpotifyEnableOutput res = getSpotifyEnable.executeAction();
        return res;        
    }

    public GetSpotifyNormalizationOutput getSpotifyNormalization()
    {
        if (!hasAction("GetSpotifyNormalization"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSpotifyNormalization of service MagicAudio");
        }
        GetSpotifyNormalization getSpotifyNormalization = new GetSpotifyNormalization(magicAudioService,  upnpService.getControlPoint());
        GetSpotifyNormalizationOutput res = getSpotifyNormalization.executeAction();
        return res;        
    }

    public GetSpotifyVerOutput getSpotifyVer()
    {
        if (!hasAction("GetSpotifyVer"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSpotifyVer of service MagicAudio");
        }
        GetSpotifyVer getSpotifyVer = new GetSpotifyVer(magicAudioService,  upnpService.getControlPoint());
        GetSpotifyVerOutput res = getSpotifyVer.executeAction();
        return res;        
    }

    public GetTidalConnectEnableOutput getTidalConnectEnable()
    {
        if (!hasAction("GetTidalConnectEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetTidalConnectEnable of service MagicAudio");
        }
        GetTidalConnectEnable getTidalConnectEnable = new GetTidalConnectEnable(magicAudioService,  upnpService.getControlPoint());
        GetTidalConnectEnableOutput res = getTidalConnectEnable.executeAction();
        return res;        
    }

    public GetTidalConnectSupportOutput getTidalConnectSupport()
    {
        if (!hasAction("GetTidalConnectSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetTidalConnectSupport of service MagicAudio");
        }
        GetTidalConnectSupport getTidalConnectSupport = new GetTidalConnectSupport(magicAudioService,  upnpService.getControlPoint());
        GetTidalConnectSupportOutput res = getTidalConnectSupport.executeAction();
        return res;        
    }

    public GetTidalQualityOutput getTidalQuality()
    {
        if (!hasAction("GetTidalQuality"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetTidalQuality of service MagicAudio");
        }
        GetTidalQuality getTidalQuality = new GetTidalQuality(magicAudioService,  upnpService.getControlPoint());
        GetTidalQualityOutput res = getTidalQuality.executeAction();
        return res;        
    }

    public GetTuneInDetailsOutput getTuneInDetails()
    {
        if (!hasAction("GetTuneInDetails"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetTuneInDetails of service MagicAudio");
        }
        GetTuneInDetails getTuneInDetails = new GetTuneInDetails(magicAudioService,  upnpService.getControlPoint());
        GetTuneInDetailsOutput res = getTuneInDetails.executeAction();
        return res;        
    }

    public GetUSBSPDIFModeOutput getUSBSPDIFMode()
    {
        if (!hasAction("GetUSBSPDIFMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetUSBSPDIFMode of service MagicAudio");
        }
        GetUSBSPDIFMode getUSBSPDIFMode = new GetUSBSPDIFMode(magicAudioService,  upnpService.getControlPoint());
        GetUSBSPDIFModeOutput res = getUSBSPDIFMode.executeAction();
        return res;        
    }

    public GetUltraSonicFilterDSDOutput getUltraSonicFilterDSD()
    {
        if (!hasAction("GetUltraSonicFilterDSD"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetUltraSonicFilterDSD of service MagicAudio");
        }
        GetUltraSonicFilterDSD getUltraSonicFilterDSD = new GetUltraSonicFilterDSD(magicAudioService,  upnpService.getControlPoint());
        GetUltraSonicFilterDSDOutput res = getUltraSonicFilterDSD.executeAction();
        return res;        
    }

    public GetVolumeControlOutput getVolumeControl()
    {
        if (!hasAction("GetVolumeControl"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetVolumeControl of service MagicAudio");
        }
        GetVolumeControl getVolumeControl = new GetVolumeControl(magicAudioService,  upnpService.getControlPoint());
        GetVolumeControlOutput res = getVolumeControl.executeAction();
        return res;        
    }

    public GetVolumeControlSupportOutput getVolumeControlSupport()
    {
        if (!hasAction("GetVolumeControlSupport"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetVolumeControlSupport of service MagicAudio");
        }
        GetVolumeControlSupport getVolumeControlSupport = new GetVolumeControlSupport(magicAudioService,  upnpService.getControlPoint());
        GetVolumeControlSupportOutput res = getVolumeControlSupport.executeAction();
        return res;        
    }

    public void logoutPlex()
    {
        if (!hasAction("LogoutPlex"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action LogoutPlex of service MagicAudio");
        }
        LogoutPlex logoutPlex = new LogoutPlex(magicAudioService,  upnpService.getControlPoint());
        logoutPlex.executeAction();
    }

    public void setAmazonMusicEnable(SetAmazonMusicEnableInput inp)
    {
        if (!hasAction("SetAmazonMusicEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAmazonMusicEnable of service MagicAudio");
        }
        SetAmazonMusicEnable setAmazonMusicEnable = new SetAmazonMusicEnable(magicAudioService, inp, upnpService.getControlPoint());
        setAmazonMusicEnable.executeAction();
    }

    public void setAnalogBalance(SetAnalogBalanceInput inp)
    {
        if (!hasAction("SetAnalogBalance"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAnalogBalance of service MagicAudio");
        }
        SetAnalogBalance setAnalogBalance = new SetAnalogBalance(magicAudioService, inp, upnpService.getControlPoint());
        setAnalogBalance.executeAction();
    }

    public void setAnalogOutLvl(SetAnalogOutLvlInput inp)
    {
        if (!hasAction("SetAnalogOutLvl"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAnalogOutLvl of service MagicAudio");
        }
        SetAnalogOutLvl setAnalogOutLvl = new SetAnalogOutLvl(magicAudioService, inp, upnpService.getControlPoint());
        setAnalogOutLvl.executeAction();
    }

    public void setAutoPlay(SetAutoPlayInput inp)
    {
        if (!hasAction("SetAutoPlay"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetAutoPlay of service MagicAudio");
        }
        SetAutoPlay setAutoPlay = new SetAutoPlay(magicAudioService, inp, upnpService.getControlPoint());
        setAutoPlay.executeAction();
    }

    public void setControl4Enable(SetControl4EnableInput inp)
    {
        if (!hasAction("SetControl4Enable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetControl4Enable of service MagicAudio");
        }
        SetControl4Enable setControl4Enable = new SetControl4Enable(magicAudioService, inp, upnpService.getControlPoint());
        setControl4Enable.executeAction();
    }

    public void setCustomCode(SetCustomCodeInput inp)
    {
        if (!hasAction("SetCustomCode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetCustomCode of service MagicAudio");
        }
        SetCustomCode setCustomCode = new SetCustomCode(magicAudioService, inp, upnpService.getControlPoint());
        setCustomCode.executeAction();
    }

    public void setDeemphasis(SetDeemphasisInput inp)
    {
        if (!hasAction("SetDeemphasis"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetDeemphasis of service MagicAudio");
        }
        SetDeemphasis setDeemphasis = new SetDeemphasis(magicAudioService, inp, upnpService.getControlPoint());
        setDeemphasis.executeAction();
    }

    public void setDefaultRadioEnable(SetDefaultRadioEnableInput inp)
    {
        if (!hasAction("SetDefaultRadioEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetDefaultRadioEnable of service MagicAudio");
        }
        SetDefaultRadioEnable setDefaultRadioEnable = new SetDefaultRadioEnable(magicAudioService, inp, upnpService.getControlPoint());
        setDefaultRadioEnable.executeAction();
    }

    public void setDigitalAudioEnable(SetDigitalAudioEnableInput inp)
    {
        if (!hasAction("SetDigitalAudioEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetDigitalAudioEnable of service MagicAudio");
        }
        SetDigitalAudioEnable setDigitalAudioEnable = new SetDigitalAudioEnable(magicAudioService, inp, upnpService.getControlPoint());
        setDigitalAudioEnable.executeAction();
    }

    public void setFPBrightness(SetFPBrightnessInput inp)
    {
        if (!hasAction("SetFPBrightness"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetFPBrightness of service MagicAudio");
        }
        SetFPBrightness setFPBrightness = new SetFPBrightness(magicAudioService, inp, upnpService.getControlPoint());
        setFPBrightness.executeAction();
    }

    public void setFPMode(SetFPModeInput inp)
    {
        if (!hasAction("SetFPMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetFPMode of service MagicAudio");
        }
        SetFPMode setFPMode = new SetFPMode(magicAudioService, inp, upnpService.getControlPoint());
        setFPMode.executeAction();
    }

    public void setHDMICECEnable(SetHDMICECEnableInput inp)
    {
        if (!hasAction("SetHDMICECEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetHDMICECEnable of service MagicAudio");
        }
        SetHDMICECEnable setHDMICECEnable = new SetHDMICECEnable(magicAudioService, inp, upnpService.getControlPoint());
        setHDMICECEnable.executeAction();
    }

    public void setInputLabel(SetInputLabelInput inp)
    {
        if (!hasAction("SetInputLabel"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetInputLabel of service MagicAudio");
        }
        SetInputLabel setInputLabel = new SetInputLabel(magicAudioService, inp, upnpService.getControlPoint());
        setInputLabel.executeAction();
    }

    public void setInvertPhase(SetInvertPhaseInput inp)
    {
        if (!hasAction("SetInvertPhase"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetInvertPhase of service MagicAudio");
        }
        SetInvertPhase setInvertPhase = new SetInvertPhase(magicAudioService, inp, upnpService.getControlPoint());
        setInvertPhase.executeAction();
    }

    public void setKKBOXEnable(SetKKBOXEnableInput inp)
    {
        if (!hasAction("SetKKBOXEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetKKBOXEnable of service MagicAudio");
        }
        SetKKBOXEnable setKKBOXEnable = new SetKKBOXEnable(magicAudioService, inp, upnpService.getControlPoint());
        setKKBOXEnable.executeAction();
    }

    public void setLUFS(SetLUFSInput inp)
    {
        if (!hasAction("SetLUFS"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetLUFS of service MagicAudio");
        }
        SetLUFS setLUFS = new SetLUFS(magicAudioService, inp, upnpService.getControlPoint());
        setLUFS.executeAction();
    }

    public void setLeedhVolumeEnable(SetLeedhVolumeEnableInput inp)
    {
        if (!hasAction("SetLeedhVolumeEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetLeedhVolumeEnable of service MagicAudio");
        }
        SetLeedhVolumeEnable setLeedhVolumeEnable = new SetLeedhVolumeEnable(magicAudioService, inp, upnpService.getControlPoint());
        setLeedhVolumeEnable.executeAction();
    }

    public void setMQAMode(SetMQAModeInput inp)
    {
        if (!hasAction("SetMQAMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetMQAMode of service MagicAudio");
        }
        SetMQAMode setMQAMode = new SetMQAMode(magicAudioService, inp, upnpService.getControlPoint());
        setMQAMode.executeAction();
    }

    public void setMagicPlay(SetMagicPlayInput inp)
    {
        if (!hasAction("SetMagicPlay"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetMagicPlay of service MagicAudio");
        }
        SetMagicPlay setMagicPlay = new SetMagicPlay(magicAudioService, inp, upnpService.getControlPoint());
        setMagicPlay.executeAction();
    }

    public void setMaxVolume(SetMaxVolumeInput inp)
    {
        if (!hasAction("SetMaxVolume"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetMaxVolume of service MagicAudio");
        }
        SetMaxVolume setMaxVolume = new SetMaxVolume(magicAudioService, inp, upnpService.getControlPoint());
        setMaxVolume.executeAction();
    }

    public void setNetworkLED(SetNetworkLEDInput inp)
    {
        if (!hasAction("SetNetworkLED"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetNetworkLED of service MagicAudio");
        }
        SetNetworkLED setNetworkLED = new SetNetworkLED(magicAudioService, inp, upnpService.getControlPoint());
        setNetworkLED.executeAction();
    }

    public void setOauth(SetOauthInput inp)
    {
        if (!hasAction("SetOauth"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetOauth of service MagicAudio");
        }
        SetOauth setOauth = new SetOauth(magicAudioService, inp, upnpService.getControlPoint());
        setOauth.executeAction();
    }

    public void setOutputClockSource(SetOutputClockSourceInput inp)
    {
        if (!hasAction("SetOutputClockSource"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetOutputClockSource of service MagicAudio");
        }
        SetOutputClockSource setOutputClockSource = new SetOutputClockSource(magicAudioService, inp, upnpService.getControlPoint());
        setOutputClockSource.executeAction();
    }

    public void setOutputEnable(SetOutputEnableInput inp)
    {
        if (!hasAction("SetOutputEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetOutputEnable of service MagicAudio");
        }
        SetOutputEnable setOutputEnable = new SetOutputEnable(magicAudioService, inp, upnpService.getControlPoint());
        setOutputEnable.executeAction();
    }

    public void setPlaybackClockSource(SetPlaybackClockSourceInput inp)
    {
        if (!hasAction("SetPlaybackClockSource"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetPlaybackClockSource of service MagicAudio");
        }
        SetPlaybackClockSource setPlaybackClockSource = new SetPlaybackClockSource(magicAudioService, inp, upnpService.getControlPoint());
        setPlaybackClockSource.executeAction();
    }

    public void setPlexEnable(SetPlexEnableInput inp)
    {
        if (!hasAction("SetPlexEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetPlexEnable of service MagicAudio");
        }
        SetPlexEnable setPlexEnable = new SetPlexEnable(magicAudioService, inp, upnpService.getControlPoint());
        setPlexEnable.executeAction();
    }

    public void setPublicDNS(SetPublicDNSInput inp)
    {
        if (!hasAction("SetPublicDNS"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetPublicDNS of service MagicAudio");
        }
        SetPublicDNS setPublicDNS = new SetPublicDNS(magicAudioService, inp, upnpService.getControlPoint());
        setPublicDNS.executeAction();
    }

    public void setQPlayEnable(SetQPlayEnableInput inp)
    {
        if (!hasAction("SetQPlayEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetQPlayEnable of service MagicAudio");
        }
        SetQPlayEnable setQPlayEnable = new SetQPlayEnable(magicAudioService, inp, upnpService.getControlPoint());
        setQPlayEnable.executeAction();
    }

    public void setQobuzConnectEnable(SetQobuzConnectEnableInput inp)
    {
        if (!hasAction("SetQobuzConnectEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetQobuzConnectEnable of service MagicAudio");
        }
        SetQobuzConnectEnable setQobuzConnectEnable = new SetQobuzConnectEnable(magicAudioService, inp, upnpService.getControlPoint());
        setQobuzConnectEnable.executeAction();
    }

    public void setQobuzQuality(SetQobuzQualityInput inp)
    {
        if (!hasAction("SetQobuzQuality"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetQobuzQuality of service MagicAudio");
        }
        SetQobuzQuality setQobuzQuality = new SetQobuzQuality(magicAudioService, inp, upnpService.getControlPoint());
        setQobuzQuality.executeAction();
    }

    public void setRAATEnable(SetRAATEnableInput inp)
    {
        if (!hasAction("SetRAATEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetRAATEnable of service MagicAudio");
        }
        SetRAATEnable setRAATEnable = new SetRAATEnable(magicAudioService, inp, upnpService.getControlPoint());
        setRAATEnable.executeAction();
    }

    public void setResamplingMode(SetResamplingModeInput inp)
    {
        if (!hasAction("SetResamplingMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetResamplingMode of service MagicAudio");
        }
        SetResamplingMode setResamplingMode = new SetResamplingMode(magicAudioService, inp, upnpService.getControlPoint());
        setResamplingMode.executeAction();
    }

    public void setResamplingModeDetails(SetResamplingModeDetailsInput inp)
    {
        if (!hasAction("SetResamplingModeDetails"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetResamplingModeDetails of service MagicAudio");
        }
        SetResamplingModeDetails setResamplingModeDetails = new SetResamplingModeDetails(magicAudioService, inp, upnpService.getControlPoint());
        setResamplingModeDetails.executeAction();
    }

    public void setScreensaver(SetScreensaverInput inp)
    {
        if (!hasAction("SetScreensaver"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetScreensaver of service MagicAudio");
        }
        SetScreensaver setScreensaver = new SetScreensaver(magicAudioService, inp, upnpService.getControlPoint());
        setScreensaver.executeAction();
    }

    public void setSongcastMode(SetSongcastModeInput inp)
    {
        if (!hasAction("SetSongcastMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetSongcastMode of service MagicAudio");
        }
        SetSongcastMode setSongcastMode = new SetSongcastMode(magicAudioService, inp, upnpService.getControlPoint());
        setSongcastMode.executeAction();
    }

    public void setSpotifyEnable(SetSpotifyEnableInput inp)
    {
        if (!hasAction("SetSpotifyEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetSpotifyEnable of service MagicAudio");
        }
        SetSpotifyEnable setSpotifyEnable = new SetSpotifyEnable(magicAudioService, inp, upnpService.getControlPoint());
        setSpotifyEnable.executeAction();
    }

    public void setSpotifyNormalization(SetSpotifyNormalizationInput inp)
    {
        if (!hasAction("SetSpotifyNormalization"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetSpotifyNormalization of service MagicAudio");
        }
        SetSpotifyNormalization setSpotifyNormalization = new SetSpotifyNormalization(magicAudioService, inp, upnpService.getControlPoint());
        setSpotifyNormalization.executeAction();
    }

    public void setTidalConnectEnable(SetTidalConnectEnableInput inp)
    {
        if (!hasAction("SetTidalConnectEnable"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetTidalConnectEnable of service MagicAudio");
        }
        SetTidalConnectEnable setTidalConnectEnable = new SetTidalConnectEnable(magicAudioService, inp, upnpService.getControlPoint());
        setTidalConnectEnable.executeAction();
    }

    public void setTidalQuality(SetTidalQualityInput inp)
    {
        if (!hasAction("SetTidalQuality"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetTidalQuality of service MagicAudio");
        }
        SetTidalQuality setTidalQuality = new SetTidalQuality(magicAudioService, inp, upnpService.getControlPoint());
        setTidalQuality.executeAction();
    }

    public void setTuneInDetails(SetTuneInDetailsInput inp)
    {
        if (!hasAction("SetTuneInDetails"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetTuneInDetails of service MagicAudio");
        }
        SetTuneInDetails setTuneInDetails = new SetTuneInDetails(magicAudioService, inp, upnpService.getControlPoint());
        setTuneInDetails.executeAction();
    }

    public void setTuneInLogin()
    {
        if (!hasAction("SetTuneInLogin"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetTuneInLogin of service MagicAudio");
        }
        SetTuneInLogin setTuneInLogin = new SetTuneInLogin(magicAudioService,  upnpService.getControlPoint());
        setTuneInLogin.executeAction();
    }

    public void setUSBSPDIFMode(SetUSBSPDIFModeInput inp)
    {
        if (!hasAction("SetUSBSPDIFMode"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetUSBSPDIFMode of service MagicAudio");
        }
        SetUSBSPDIFMode setUSBSPDIFMode = new SetUSBSPDIFMode(magicAudioService, inp, upnpService.getControlPoint());
        setUSBSPDIFMode.executeAction();
    }

    public void setUltraSonicFilterDSD(SetUltraSonicFilterDSDInput inp)
    {
        if (!hasAction("SetUltraSonicFilterDSD"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetUltraSonicFilterDSD of service MagicAudio");
        }
        SetUltraSonicFilterDSD setUltraSonicFilterDSD = new SetUltraSonicFilterDSD(magicAudioService, inp, upnpService.getControlPoint());
        setUltraSonicFilterDSD.executeAction();
    }

    public void setVolumeControl(SetVolumeControlInput inp)
    {
        if (!hasAction("SetVolumeControl"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetVolumeControl of service MagicAudio");
        }
        SetVolumeControl setVolumeControl = new SetVolumeControl(magicAudioService, inp, upnpService.getControlPoint());
        setVolumeControl.executeAction();
    }
}
