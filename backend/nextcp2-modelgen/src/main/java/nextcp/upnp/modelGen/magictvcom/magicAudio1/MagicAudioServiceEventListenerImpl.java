package nextcp.upnp.modelGen.magictvcom.magicAudio1;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.state.StateVariableValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventImpl.ftl
 *  
 * Generated UPnP EventListener Implementation.  
 */
public class MagicAudioServiceEventListenerImpl implements IMagicAudioServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(MagicAudioServiceEventListenerImpl.class.getName());
    private MagicAudioServiceStateVariable stateVariable = new MagicAudioServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public MagicAudioServiceStateVariable getStateVariable()
    {
        return stateVariable;
    }

    //
    // Generic event callbacks
    // =============================================================================================================================================================================

    @Override
    public void invalidMessage(UnsupportedDataException ex)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("invalidMessage : %s", ex.getMessage()));
        }
    }

    @Override
    public void failed(UpnpResponse responseStatus)
    {
        if (log.isWarnEnabled())
        {
            log.warn(String.format("failed : %s", responseStatus.getResponseDetails()));
        }
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("ended : %s", reason.toString()));
        }
    }

    @Override
    public void eventsMissed(int numberOfMissedEvents)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("events missed : %d", numberOfMissedEvents));
        }
    }

    @Override
    public void established()
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("established."));
        }
    }

    /**
     * This method receives all published events.
     */
    @Override
    public void eventReceived(String key, StateVariableValue<RemoteService> stateVar)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("event received."));
        }
    }

    /**
     * This method is called, when all attributes of the event are processed. 
     */
    @Override
    public void eventProcessed()
    {
        if (log.isDebugEnabled())
        {
            log.debug("finished processing event attributes.");
        }
    }

    //
    //    Service specific event callbacks 
    // =============================================================================================================================================================================
    public void inputLabelSupportChange(Boolean value)
    {
        stateVariable.InputLabelSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "InputLabelSupport", value));
        }
    }
    
    public void spotifyEnableChange(Boolean value)
    {
        stateVariable.SpotifyEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SpotifyEnable", value));
        }
    }
    
    public void newBitDepthChange(String value)
    {
        stateVariable.NewBitDepth = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "NewBitDepth", value));
        }
    }
    
    public void brightnessChange(String value)
    {
        stateVariable.Brightness = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Brightness", value));
        }
    }
    
    public void portChange(Long value)
    {
        stateVariable.Port = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Port", value));
        }
    }
    
    public void firmwareDownloadProgressChange(Integer value)
    {
        stateVariable.FirmwareDownloadProgress = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "FirmwareDownloadProgress", value));
        }
    }
    
    public void defaultRadioSupportChange(Boolean value)
    {
        stateVariable.DefaultRadioSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "DefaultRadioSupport", value));
        }
    }
    
    public void bitDepthChange(String value)
    {
        stateVariable.BitDepth = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "BitDepth", value));
        }
    }
    
    public void aboutStringChange(String value)
    {
        stateVariable.AboutString = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AboutString", value));
        }
    }
    
    public void serverEnabledChange(Boolean value)
    {
        stateVariable.ServerEnabled = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ServerEnabled", value));
        }
    }
    
    public void spotifyVerChange(String value)
    {
        stateVariable.SpotifyVer = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SpotifyVer", value));
        }
    }
    
    public void formatConversionChange(String value)
    {
        stateVariable.FormatConversion = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "FormatConversion", value));
        }
    }
    
    public void langIDChange(Long value)
    {
        stateVariable.LangID = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LangID", value));
        }
    }
    
    public void uSBSPDIFModeChange(Boolean value)
    {
        stateVariable.USBSPDIFMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "USBSPDIFMode", value));
        }
    }
    
    public void tidalAccessExpiryChange(Long value)
    {
        stateVariable.TidalAccessExpiry = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalAccessExpiry", value));
        }
    }
    
    public void maxVolumeChange(Long value)
    {
        stateVariable.MaxVolume = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MaxVolume", value));
        }
    }
    
    public void invertPhaseChange(Boolean value)
    {
        stateVariable.InvertPhase = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "InvertPhase", value));
        }
    }
    
    public void customCodeChange(String value)
    {
        stateVariable.CustomCode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CustomCode", value));
        }
    }
    
    public void plexFriendlyNameChange(String value)
    {
        stateVariable.PlexFriendlyName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlexFriendlyName", value));
        }
    }
    
    public void tidalClientIdChange(byte[] value)
    {
        stateVariable.TidalClientId = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalClientId", value));
        }
    }
    
    public void resamplingModeChange(String value)
    {
        stateVariable.ResamplingMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ResamplingMode", value));
        }
    }
    
    public void publicKeyChange(String value)
    {
        stateVariable.PublicKey = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PublicKey", value));
        }
    }
    
    public void tidalUserNameChange(String value)
    {
        stateVariable.TidalUserName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalUserName", value));
        }
    }
    
    public void newSamplingRateChange(String value)
    {
        stateVariable.NewSamplingRate = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "NewSamplingRate", value));
        }
    }
    
    public void outputDeemphasisChange(Boolean value)
    {
        stateVariable.OutputDeemphasis = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "OutputDeemphasis", value));
        }
    }
    
    public void plexSupportChange(Boolean value)
    {
        stateVariable.PlexSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlexSupport", value));
        }
    }
    
    public void qobuzQualityChange(String value)
    {
        stateVariable.QobuzQuality = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "QobuzQuality", value));
        }
    }
    
    public void magicPlayChange(Boolean value)
    {
        stateVariable.MagicPlay = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MagicPlay", value));
        }
    }
    
    public void leedhVolumeSupportChange(Boolean value)
    {
        stateVariable.LeedhVolumeSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LeedhVolumeSupport", value));
        }
    }
    
    public void mQASampleRateChange(Long value)
    {
        stateVariable.MQASampleRate = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MQASampleRate", value));
        }
    }
    
    public void analogBalanceChange(Integer value)
    {
        stateVariable.AnalogBalance = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AnalogBalance", value));
        }
    }
    
    public void firmwareCommandChange(String value)
    {
        stateVariable.FirmwareCommand = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "FirmwareCommand", value));
        }
    }
    
    public void uSFilterDSDChange(String value)
    {
        stateVariable.USFilterDSD = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "USFilterDSD", value));
        }
    }
    
    public void outputBitDepthChange(Long value)
    {
        stateVariable.OutputBitDepth = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "OutputBitDepth", value));
        }
    }
    
    public void autoPlayChange(Boolean value)
    {
        stateVariable.AutoPlay = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AutoPlay", value));
        }
    }
    
    public void analogBalanceSupportChange(Boolean value)
    {
        stateVariable.AnalogBalanceSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AnalogBalanceSupport", value));
        }
    }
    
    public void plexEnableChange(Boolean value)
    {
        stateVariable.PlexEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlexEnable", value));
        }
    }
    
    public void magicAudioVerChange(String value)
    {
        stateVariable.MagicAudioVer = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MagicAudioVer", value));
        }
    }
    
    public void volumeControlChange(String value)
    {
        stateVariable.VolumeControl = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeControl", value));
        }
    }
    
    public void rAATVerChange(String value)
    {
        stateVariable.RAATVer = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RAATVer", value));
        }
    }
    
    public void qPlayEnableChange(Boolean value)
    {
        stateVariable.QPlayEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "QPlayEnable", value));
        }
    }
    
    public void outputInvertPhaseChange(Boolean value)
    {
        stateVariable.OutputInvertPhase = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "OutputInvertPhase", value));
        }
    }
    
    public void networkLEDChange(Boolean value)
    {
        stateVariable.NetworkLED = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "NetworkLED", value));
        }
    }
    
    public void serviceIdChange(String value)
    {
        stateVariable.ServiceId = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ServiceId", value));
        }
    }
    
    public void publicDNSChange(Boolean value)
    {
        stateVariable.PublicDNS = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PublicDNS", value));
        }
    }
    
    public void plexCodeChange(String value)
    {
        stateVariable.PlexCode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlexCode", value));
        }
    }
    
    public void resamplingTagChange(String value)
    {
        stateVariable.ResamplingTag = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ResamplingTag", value));
        }
    }
    
    public void mQAModeChange(String value)
    {
        stateVariable.MQAMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MQAMode", value));
        }
    }
    
    public void networkLEDControlChange(Boolean value)
    {
        stateVariable.NetworkLEDControl = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "NetworkLEDControl", value));
        }
    }
    
    public void screensaverTimeoutChange(Long value)
    {
        stateVariable.ScreensaverTimeout = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ScreensaverTimeout", value));
        }
    }
    
    public void externalClockSupportChange(String value)
    {
        stateVariable.ExternalClockSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ExternalClockSupport", value));
        }
    }
    
    public void plexUsernameChange(String value)
    {
        stateVariable.PlexUsername = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlexUsername", value));
        }
    }
    
    public void screensaverModeChange(Long value)
    {
        stateVariable.ScreensaverMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ScreensaverMode", value));
        }
    }
    
    public void mQACreatorIdChange(Long value)
    {
        stateVariable.MQACreatorId = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MQACreatorId", value));
        }
    }
    
    public void songcastModeChange(Boolean value)
    {
        stateVariable.SongcastMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SongcastMode", value));
        }
    }
    
    public void outputClockSourceChange(String value)
    {
        stateVariable.OutputClockSource = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "OutputClockSource", value));
        }
    }
    
    public void qPlaySupportChange(Boolean value)
    {
        stateVariable.QPlaySupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "QPlaySupport", value));
        }
    }
    
    public void tidalRefreshTokenChange(byte[] value)
    {
        stateVariable.TidalRefreshToken = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalRefreshToken", value));
        }
    }
    
    public void spotifyNormalizationChange(Boolean value)
    {
        stateVariable.SpotifyNormalization = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SpotifyNormalization", value));
        }
    }
    
    public void rAATEnableChange(Boolean value)
    {
        stateVariable.RAATEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RAATEnable", value));
        }
    }
    
    public void playbackClockSourceChange(String value)
    {
        stateVariable.PlaybackClockSource = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlaybackClockSource", value));
        }
    }
    
    public void dAEnableChange(String value)
    {
        stateVariable.DAEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "DAEnable", value));
        }
    }
    
    public void defaultRadioEnableChange(Boolean value)
    {
        stateVariable.DefaultRadioEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "DefaultRadioEnable", value));
        }
    }
    
    public void dSDtoPCM_typeChange(String value)
    {
        stateVariable.DSDtoPCM_type = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "DSDtoPCM_type", value));
        }
    }
    
    public void tidalAccessTokenChange(byte[] value)
    {
        stateVariable.TidalAccessToken = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalAccessToken", value));
        }
    }
    
    public void firmwareResultChange(String value)
    {
        stateVariable.FirmwareResult = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "FirmwareResult", value));
        }
    }
    
    public void mQASupportChange(Boolean value)
    {
        stateVariable.MQASupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MQASupport", value));
        }
    }
    
    public void analogOutLvlChange(String value)
    {
        stateVariable.AnalogOutLvl = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AnalogOutLvl", value));
        }
    }
    
    public void sourceNameChange(String value)
    {
        stateVariable.SourceName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceName", value));
        }
    }
    
    public void tidalConnectEnableChange(Boolean value)
    {
        stateVariable.TidalConnectEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalConnectEnable", value));
        }
    }
    
    public void outputSampleRateChange(Long value)
    {
        stateVariable.OutputSampleRate = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "OutputSampleRate", value));
        }
    }
    
    public void samplingRateChange(String value)
    {
        stateVariable.SamplingRate = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SamplingRate", value));
        }
    }
    
    public void mQAProvenanceChange(String value)
    {
        stateVariable.MQAProvenance = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MQAProvenance", value));
        }
    }
    
    public void songcastSupportChange(Boolean value)
    {
        stateVariable.SongcastSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SongcastSupport", value));
        }
    }
    
    public void sourceIndexChange(Long value)
    {
        stateVariable.SourceIndex = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceIndex", value));
        }
    }
    
    public void fPModeChange(String value)
    {
        stateVariable.FPMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "FPMode", value));
        }
    }
    
    public void fileTypeChange(String value)
    {
        stateVariable.FileType = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "FileType", value));
        }
    }
    
    public void tuneInUserNameChange(String value)
    {
        stateVariable.TuneInUserName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TuneInUserName", value));
        }
    }
    
    public void tidalClientSecretChange(byte[] value)
    {
        stateVariable.TidalClientSecret = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalClientSecret", value));
        }
    }
    
    public void plexEmailChange(String value)
    {
        stateVariable.PlexEmail = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlexEmail", value));
        }
    }
    
    public void volumeControlSupportChange(Boolean value)
    {
        stateVariable.VolumeControlSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeControlSupport", value));
        }
    }
    
    public void deemphasisChange(Boolean value)
    {
        stateVariable.Deemphasis = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Deemphasis", value));
        }
    }
    
    public void tidalConnectSupportChange(Boolean value)
    {
        stateVariable.TidalConnectSupport = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalConnectSupport", value));
        }
    }
    
    public void tidalQualityChange(String value)
    {
        stateVariable.TidalQuality = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TidalQuality", value));
        }
    }
    
    public void leedhVolumeEnableChange(Boolean value)
    {
        stateVariable.LeedhVolumeEnable = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LeedhVolumeEnable", value));
        }
    }
    
    public void mQAAuthenticityChange(String value)
    {
        stateVariable.MQAAuthenticity = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MQAAuthenticity", value));
        }
    }
    
}
