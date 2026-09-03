package nextcp.upnp.modelGen.magictvcom.magicAudio1;

import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.gena.RemoteGENASubscription;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.state.StateVariableValue;
import org.jupnp.model.types.UnsignedVariableInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;
import nextcp.upnp.UpnpValue;

/**
 * Last Change : 08.09.2025
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceSubscription.ftl
 *  
 * Generated UPnP subscription service class.  
 */
public class MagicAudioServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(MagicAudioServiceSubscription.class.getName());

    private List<IMagicAudioServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected MagicAudioServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IMagicAudioServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IMagicAudioServiceEventListener listener)
    {
        return eventListener.remove(listener);
    }
    
    @Override
    public void invalidMessage(UnsupportedDataException ex)
    {
        log.error("invalid message");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.invalidMessage(ex);
        }
    }

    @Override
    public void failed(UpnpResponse responseStatus)
    {
        log.warn("failed");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.failed(responseStatus);
        }
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        log.debug("ended");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.ended(reason, responseStatus);
        }
    }

    @Override
    public void eventsMissed(int numberOfMissedEvents)
    {
        log.warn("missed events count : " + numberOfMissedEvents);
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.eventsMissed(numberOfMissedEvents);
        }
    }

    @Override
    public void established()
    {
        log.debug("established");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.established();
        }
    }

    @Override
    public void eventReceived()
    {
        log.debug("eventReceived");
        Map<String, StateVariableValue<RemoteService>> values = getCurrentValues();
        for (StateVariableValue<RemoteService> stateVar : values.values())
        {
            String key = stateVar.getStateVariable().getName();
            try
            {
                switch (key)
                {
                    case "AboutString":
                        aboutStringChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "AmazonMusicEnable":
                        amazonMusicEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "AmazonMusicSupport":
                        amazonMusicSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "AnalogBalance":
                        analogBalanceChange(UpnpValue.toInteger(stateVar.getValue()));
                        break;
                    case "AnalogBalanceSupport":
                        analogBalanceSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "AnalogOutLvl":
                        analogOutLvlChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "AppDisplayMessageId":
                        appDisplayMessageIdChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "AppDisplayMessageString":
                        appDisplayMessageStringChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "AppDisplayMessageTag":
                        appDisplayMessageTagChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "AutoPlay":
                        autoPlayChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "BitDepth":
                        bitDepthChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "Brightness":
                        brightnessChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "Control4Enable":
                        control4EnableChange(UpnpValue.toInteger(stateVar.getValue()));
                        break;
                    case "Control4Support":
                        control4SupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "CustomCode":
                        customCodeChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "DAEnable":
                        dAEnableChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "DSDtoPCM_type":
                        dSDtoPCM_typeChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "Deemphasis":
                        deemphasisChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "DefaultRadioEnable":
                        defaultRadioEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "DefaultRadioSupport":
                        defaultRadioSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "ExternalClockSupport":
                        externalClockSupportChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "FPMode":
                        fPModeChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "FileType":
                        fileTypeChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "FirmwareCommand":
                        firmwareCommandChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "FirmwareDownloadProgress":
                        firmwareDownloadProgressChange(UpnpValue.toInteger(stateVar.getValue()));
                        break;
                    case "FirmwareResult":
                        firmwareResultChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "FormatConversion":
                        formatConversionChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "HDMICECEnable":
                        hDMICECEnableChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "HDMICECSupport":
                        hDMICECSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "InputLabelSupport":
                        inputLabelSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "InvertPhase":
                        invertPhaseChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "KKBOXEnable":
                        kKBOXEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "KKBOXSupport":
                        kKBOXSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "LUFS":
                        lUFSChange(UpnpValue.toInteger(stateVar.getValue()));
                        break;
                    case "LUFSSupport":
                        lUFSSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "LangID":
                        langIDChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "LeedhVolumeEnable":
                        leedhVolumeEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "LeedhVolumeSupport":
                        leedhVolumeSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "MQAAuthenticity":
                        mQAAuthenticityChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "MQACreatorId":
                        mQACreatorIdChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "MQAMode":
                        mQAModeChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "MQAProvenance":
                        mQAProvenanceChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "MQASampleRate":
                        mQASampleRateChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "MQASupport":
                        mQASupportChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "MagicAudioVer":
                        magicAudioVerChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "MagicPlay":
                        magicPlayChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "MaxVolume":
                        maxVolumeChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "NetworkLED":
                        networkLEDChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "NetworkLEDControl":
                        networkLEDControlChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "NewBitDepth":
                        newBitDepthChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "NewSamplingRate":
                        newSamplingRateChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "OutputBitDepth":
                        outputBitDepthChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "OutputClockSource":
                        outputClockSourceChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "OutputDeemphasis":
                        outputDeemphasisChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "OutputInvertPhase":
                        outputInvertPhaseChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "OutputSampleRate":
                        outputSampleRateChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "PlaybackClockSource":
                        playbackClockSourceChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "PlexCode":
                        plexCodeChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "PlexEmail":
                        plexEmailChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "PlexEnable":
                        plexEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "PlexFriendlyName":
                        plexFriendlyNameChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "PlexSupport":
                        plexSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "PlexUsername":
                        plexUsernameChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "Port":
                        portChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "PublicDNS":
                        publicDNSChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "PublicKey":
                        publicKeyChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "QobuzConnectEnable":
                        qobuzConnectEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "QobuzConnectSupport":
                        qobuzConnectSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "QobuzQuality":
                        qobuzQualityChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "RAATEnable":
                        rAATEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "RAATVer":
                        rAATVerChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "ReplayGain":
                        replayGainChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "ResamplingMode":
                        resamplingModeChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "ResamplingTag":
                        resamplingTagChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "SamplingRate":
                        samplingRateChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "ScreensaverMode":
                        screensaverModeChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "ScreensaverTimeout":
                        screensaverTimeoutChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "ServerEnabled":
                        serverEnabledChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "ServiceId":
                        serviceIdChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "SongcastMode":
                        songcastModeChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "SongcastSupport":
                        songcastSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "SourceIndex":
                        sourceIndexChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "SourceName":
                        sourceNameChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "SpotifyEnable":
                        spotifyEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "SpotifyNormalization":
                        spotifyNormalizationChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "SpotifyVer":
                        spotifyVerChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "TidalAccessExpiry":
                        tidalAccessExpiryChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "TidalAccessToken":
                        tidalAccessTokenChange(UpnpValue.toBytes(stateVar.getValue()));
                        break;
                    case "TidalClientId":
                        tidalClientIdChange(UpnpValue.toBytes(stateVar.getValue()));
                        break;
                    case "TidalClientSecret":
                        tidalClientSecretChange(UpnpValue.toBytes(stateVar.getValue()));
                        break;
                    case "TidalConnectEnable":
                        tidalConnectEnableChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "TidalConnectSupport":
                        tidalConnectSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "TidalQuality":
                        tidalQualityChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "TidalRefreshToken":
                        tidalRefreshTokenChange(UpnpValue.toBytes(stateVar.getValue()));
                        break;
                    case "TidalUserName":
                        tidalUserNameChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "TuneInUserName":
                        tuneInUserNameChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "USBSPDIFMode":
                        uSBSPDIFModeChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "USFilterDSD":
                        uSFilterDSDChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "VolumeControl":
                        volumeControlChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "VolumeControlSupport":
                        volumeControlSupportChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    default:
                        log.warn("unknown state variable : " + key);
                }
            }
            catch (ClassCastException e)
            {
                log.error("illegal cast. Please checke code generator.", e);
            }
                            
            for (ISubscriptionEventListener listener : eventListener)
            {
                listener.eventReceived(key, stateVar);
            }
        }        
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.eventProcessed();
        }
    }

    private void aboutStringChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.aboutStringChange(value);
        }
    }    

    private void amazonMusicEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.amazonMusicEnableChange(value);
        }
    }    

    private void amazonMusicSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.amazonMusicSupportChange(value);
        }
    }    

    private void analogBalanceChange(Integer value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.analogBalanceChange(value);
        }
    }    

    private void analogBalanceSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.analogBalanceSupportChange(value);
        }
    }    

    private void analogOutLvlChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.analogOutLvlChange(value);
        }
    }    

    private void appDisplayMessageIdChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.appDisplayMessageIdChange(value);
        }
    }    

    private void appDisplayMessageStringChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.appDisplayMessageStringChange(value);
        }
    }    

    private void appDisplayMessageTagChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.appDisplayMessageTagChange(value);
        }
    }    

    private void autoPlayChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.autoPlayChange(value);
        }
    }    

    private void bitDepthChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.bitDepthChange(value);
        }
    }    

    private void brightnessChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.brightnessChange(value);
        }
    }    

    private void control4EnableChange(Integer value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.control4EnableChange(value);
        }
    }    

    private void control4SupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.control4SupportChange(value);
        }
    }    

    private void customCodeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.customCodeChange(value);
        }
    }    

    private void dAEnableChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.dAEnableChange(value);
        }
    }    

    private void dSDtoPCM_typeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.dSDtoPCM_typeChange(value);
        }
    }    

    private void deemphasisChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.deemphasisChange(value);
        }
    }    

    private void defaultRadioEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.defaultRadioEnableChange(value);
        }
    }    

    private void defaultRadioSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.defaultRadioSupportChange(value);
        }
    }    

    private void externalClockSupportChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.externalClockSupportChange(value);
        }
    }    

    private void fPModeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.fPModeChange(value);
        }
    }    

    private void fileTypeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.fileTypeChange(value);
        }
    }    

    private void firmwareCommandChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.firmwareCommandChange(value);
        }
    }    

    private void firmwareDownloadProgressChange(Integer value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.firmwareDownloadProgressChange(value);
        }
    }    

    private void firmwareResultChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.firmwareResultChange(value);
        }
    }    

    private void formatConversionChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.formatConversionChange(value);
        }
    }    

    private void hDMICECEnableChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.hDMICECEnableChange(value);
        }
    }    

    private void hDMICECSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.hDMICECSupportChange(value);
        }
    }    

    private void inputLabelSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.inputLabelSupportChange(value);
        }
    }    

    private void invertPhaseChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.invertPhaseChange(value);
        }
    }    

    private void kKBOXEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.kKBOXEnableChange(value);
        }
    }    

    private void kKBOXSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.kKBOXSupportChange(value);
        }
    }    

    private void lUFSChange(Integer value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.lUFSChange(value);
        }
    }    

    private void lUFSSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.lUFSSupportChange(value);
        }
    }    

    private void langIDChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.langIDChange(value);
        }
    }    

    private void leedhVolumeEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.leedhVolumeEnableChange(value);
        }
    }    

    private void leedhVolumeSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.leedhVolumeSupportChange(value);
        }
    }    

    private void mQAAuthenticityChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQAAuthenticityChange(value);
        }
    }    

    private void mQACreatorIdChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQACreatorIdChange(value);
        }
    }    

    private void mQAModeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQAModeChange(value);
        }
    }    

    private void mQAProvenanceChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQAProvenanceChange(value);
        }
    }    

    private void mQASampleRateChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQASampleRateChange(value);
        }
    }    

    private void mQASupportChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQASupportChange(value);
        }
    }    

    private void magicAudioVerChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.magicAudioVerChange(value);
        }
    }    

    private void magicPlayChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.magicPlayChange(value);
        }
    }    

    private void maxVolumeChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.maxVolumeChange(value);
        }
    }    

    private void networkLEDChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.networkLEDChange(value);
        }
    }    

    private void networkLEDControlChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.networkLEDControlChange(value);
        }
    }    

    private void newBitDepthChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.newBitDepthChange(value);
        }
    }    

    private void newSamplingRateChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.newSamplingRateChange(value);
        }
    }    

    private void outputBitDepthChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputBitDepthChange(value);
        }
    }    

    private void outputClockSourceChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputClockSourceChange(value);
        }
    }    

    private void outputDeemphasisChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputDeemphasisChange(value);
        }
    }    

    private void outputInvertPhaseChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputInvertPhaseChange(value);
        }
    }    

    private void outputSampleRateChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputSampleRateChange(value);
        }
    }    

    private void playbackClockSourceChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.playbackClockSourceChange(value);
        }
    }    

    private void plexCodeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexCodeChange(value);
        }
    }    

    private void plexEmailChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexEmailChange(value);
        }
    }    

    private void plexEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexEnableChange(value);
        }
    }    

    private void plexFriendlyNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexFriendlyNameChange(value);
        }
    }    

    private void plexSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexSupportChange(value);
        }
    }    

    private void plexUsernameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexUsernameChange(value);
        }
    }    

    private void portChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.portChange(value);
        }
    }    

    private void publicDNSChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.publicDNSChange(value);
        }
    }    

    private void publicKeyChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.publicKeyChange(value);
        }
    }    

    private void qobuzConnectEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.qobuzConnectEnableChange(value);
        }
    }    

    private void qobuzConnectSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.qobuzConnectSupportChange(value);
        }
    }    

    private void qobuzQualityChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.qobuzQualityChange(value);
        }
    }    

    private void rAATEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.rAATEnableChange(value);
        }
    }    

    private void rAATVerChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.rAATVerChange(value);
        }
    }    

    private void replayGainChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.replayGainChange(value);
        }
    }    

    private void resamplingModeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.resamplingModeChange(value);
        }
    }    

    private void resamplingTagChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.resamplingTagChange(value);
        }
    }    

    private void samplingRateChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.samplingRateChange(value);
        }
    }    

    private void screensaverModeChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.screensaverModeChange(value);
        }
    }    

    private void screensaverTimeoutChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.screensaverTimeoutChange(value);
        }
    }    

    private void serverEnabledChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.serverEnabledChange(value);
        }
    }    

    private void serviceIdChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.serviceIdChange(value);
        }
    }    

    private void songcastModeChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.songcastModeChange(value);
        }
    }    

    private void songcastSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.songcastSupportChange(value);
        }
    }    

    private void sourceIndexChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.sourceIndexChange(value);
        }
    }    

    private void sourceNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.sourceNameChange(value);
        }
    }    

    private void spotifyEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.spotifyEnableChange(value);
        }
    }    

    private void spotifyNormalizationChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.spotifyNormalizationChange(value);
        }
    }    

    private void spotifyVerChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.spotifyVerChange(value);
        }
    }    

    private void tidalAccessExpiryChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalAccessExpiryChange(value);
        }
    }    

    private void tidalAccessTokenChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalAccessTokenChange(value);
        }
    }    

    private void tidalClientIdChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalClientIdChange(value);
        }
    }    

    private void tidalClientSecretChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalClientSecretChange(value);
        }
    }    

    private void tidalConnectEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalConnectEnableChange(value);
        }
    }    

    private void tidalConnectSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalConnectSupportChange(value);
        }
    }    

    private void tidalQualityChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalQualityChange(value);
        }
    }    

    private void tidalRefreshTokenChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalRefreshTokenChange(value);
        }
    }    

    private void tidalUserNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalUserNameChange(value);
        }
    }    

    private void tuneInUserNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tuneInUserNameChange(value);
        }
    }    

    private void uSBSPDIFModeChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.uSBSPDIFModeChange(value);
        }
    }    

    private void uSFilterDSDChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.uSFilterDSDChange(value);
        }
    }    

    private void volumeControlChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.volumeControlChange(value);
        }
    }    

    private void volumeControlSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.volumeControlSupportChange(value);
        }
    }    
}
