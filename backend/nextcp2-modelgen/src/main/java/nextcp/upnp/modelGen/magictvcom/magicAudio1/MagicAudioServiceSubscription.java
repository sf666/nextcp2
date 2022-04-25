package nextcp.upnp.modelGen.magictvcom.magicAudio1;

import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.UnsignedVariableInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

/**
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
        log.warn("ended");
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
                    case "SpotifyEnable":
                        spotifyEnableChange((Boolean) stateVar.getValue());
                        break;
                    case "NewBitDepth":
                        newBitDepthChange((String) stateVar.getValue());
                        break;
                    case "Brightness":
                        brightnessChange((String) stateVar.getValue());
                        break;
                    case "Port":
                        portChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "FirmwareDownloadProgress":
                        firmwareDownloadProgressChange((Integer) stateVar.getValue());
                        break;
                    case "BitDepth":
                        bitDepthChange((String) stateVar.getValue());
                        break;
                    case "AboutString":
                        aboutStringChange((String) stateVar.getValue());
                        break;
                    case "ServerEnabled":
                        serverEnabledChange((Boolean) stateVar.getValue());
                        break;
                    case "SpotifyVer":
                        spotifyVerChange((String) stateVar.getValue());
                        break;
                    case "FormatConversion":
                        formatConversionChange((String) stateVar.getValue());
                        break;
                    case "LangID":
                        langIDChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "TidalAccessExpiry":
                        tidalAccessExpiryChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "MaxVolume":
                        maxVolumeChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "InvertPhase":
                        invertPhaseChange((Boolean) stateVar.getValue());
                        break;
                    case "CustomCode":
                        customCodeChange((String) stateVar.getValue());
                        break;
                    case "TidalClientId":
                        tidalClientIdChange((byte[]) stateVar.getValue());
                        break;
                    case "ResamplingMode":
                        resamplingModeChange((String) stateVar.getValue());
                        break;
                    case "PublicKey":
                        publicKeyChange((String) stateVar.getValue());
                        break;
                    case "TidalUserName":
                        tidalUserNameChange((String) stateVar.getValue());
                        break;
                    case "NewSamplingRate":
                        newSamplingRateChange((String) stateVar.getValue());
                        break;
                    case "OutputDeemphasis":
                        outputDeemphasisChange((Boolean) stateVar.getValue());
                        break;
                    case "QobuzQuality":
                        qobuzQualityChange((String) stateVar.getValue());
                        break;
                    case "MagicPlay":
                        magicPlayChange((Boolean) stateVar.getValue());
                        break;
                    case "LeedhVolumeSupport":
                        leedhVolumeSupportChange((Boolean) stateVar.getValue());
                        break;
                    case "MQASampleRate":
                        mQASampleRateChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "FirmwareCommand":
                        firmwareCommandChange((String) stateVar.getValue());
                        break;
                    case "USFilterDSD":
                        uSFilterDSDChange((String) stateVar.getValue());
                        break;
                    case "OutputBitDepth":
                        outputBitDepthChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "AutoPlay":
                        autoPlayChange((Boolean) stateVar.getValue());
                        break;
                    case "MagicAudioVer":
                        magicAudioVerChange((String) stateVar.getValue());
                        break;
                    case "VolumeControl":
                        volumeControlChange((String) stateVar.getValue());
                        break;
                    case "RAATVer":
                        rAATVerChange((String) stateVar.getValue());
                        break;
                    case "OutputInvertPhase":
                        outputInvertPhaseChange((Boolean) stateVar.getValue());
                        break;
                    case "NetworkLED":
                        networkLEDChange((Boolean) stateVar.getValue());
                        break;
                    case "ServiceId":
                        serviceIdChange((String) stateVar.getValue());
                        break;
                    case "ResamplingTag":
                        resamplingTagChange((String) stateVar.getValue());
                        break;
                    case "MQAMode":
                        mQAModeChange((String) stateVar.getValue());
                        break;
                    case "NetworkLEDControl":
                        networkLEDControlChange((Boolean) stateVar.getValue());
                        break;
                    case "ScreensaverTimeout":
                        screensaverTimeoutChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ExternalClockSupport":
                        externalClockSupportChange((String) stateVar.getValue());
                        break;
                    case "ScreensaverMode":
                        screensaverModeChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "MQACreatorId":
                        mQACreatorIdChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "SongcastMode":
                        songcastModeChange((Boolean) stateVar.getValue());
                        break;
                    case "OutputClockSource":
                        outputClockSourceChange((String) stateVar.getValue());
                        break;
                    case "TidalRefreshToken":
                        tidalRefreshTokenChange((byte[]) stateVar.getValue());
                        break;
                    case "RAATEnable":
                        rAATEnableChange((Boolean) stateVar.getValue());
                        break;
                    case "PlaybackClockSource":
                        playbackClockSourceChange((String) stateVar.getValue());
                        break;
                    case "DAEnable":
                        dAEnableChange((String) stateVar.getValue());
                        break;
                    case "DSDtoPCM_type":
                        dSDtoPCM_typeChange((String) stateVar.getValue());
                        break;
                    case "TidalAccessToken":
                        tidalAccessTokenChange((byte[]) stateVar.getValue());
                        break;
                    case "FirmwareResult":
                        firmwareResultChange((String) stateVar.getValue());
                        break;
                    case "MQASupport":
                        mQASupportChange((Boolean) stateVar.getValue());
                        break;
                    case "AnalogOutLvl":
                        analogOutLvlChange((String) stateVar.getValue());
                        break;
                    case "TidalConnectEnable":
                        tidalConnectEnableChange((Boolean) stateVar.getValue());
                        break;
                    case "OutputSampleRate":
                        outputSampleRateChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "SamplingRate":
                        samplingRateChange((String) stateVar.getValue());
                        break;
                    case "MQAProvenance":
                        mQAProvenanceChange((String) stateVar.getValue());
                        break;
                    case "SongcastSupport":
                        songcastSupportChange((Boolean) stateVar.getValue());
                        break;
                    case "FileType":
                        fileTypeChange((String) stateVar.getValue());
                        break;
                    case "TuneInUserName":
                        tuneInUserNameChange((String) stateVar.getValue());
                        break;
                    case "TidalClientSecret":
                        tidalClientSecretChange((byte[]) stateVar.getValue());
                        break;
                    case "VolumeControlSupport":
                        volumeControlSupportChange((Boolean) stateVar.getValue());
                        break;
                    case "Deemphasis":
                        deemphasisChange((Boolean) stateVar.getValue());
                        break;
                    case "TidalConnectSupport":
                        tidalConnectSupportChange((Boolean) stateVar.getValue());
                        break;
                    case "TidalQuality":
                        tidalQualityChange((String) stateVar.getValue());
                        break;
                    case "LeedhVolumeEnable":
                        leedhVolumeEnableChange((Boolean) stateVar.getValue());
                        break;
                    case "MQAAuthenticity":
                        mQAAuthenticityChange((String) stateVar.getValue());
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

    private void spotifyEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.spotifyEnableChange(value);
        }
    }    

    private void newBitDepthChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.newBitDepthChange(value);
        }
    }    

    private void brightnessChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.brightnessChange(value);
        }
    }    

    private void portChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.portChange(value);
        }
    }    

    private void firmwareDownloadProgressChange(Integer value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.firmwareDownloadProgressChange(value);
        }
    }    

    private void bitDepthChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.bitDepthChange(value);
        }
    }    

    private void aboutStringChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.aboutStringChange(value);
        }
    }    

    private void serverEnabledChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.serverEnabledChange(value);
        }
    }    

    private void spotifyVerChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.spotifyVerChange(value);
        }
    }    

    private void formatConversionChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.formatConversionChange(value);
        }
    }    

    private void langIDChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.langIDChange(value);
        }
    }    

    private void tidalAccessExpiryChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalAccessExpiryChange(value);
        }
    }    

    private void maxVolumeChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.maxVolumeChange(value);
        }
    }    

    private void invertPhaseChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.invertPhaseChange(value);
        }
    }    

    private void customCodeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.customCodeChange(value);
        }
    }    

    private void tidalClientIdChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalClientIdChange(value);
        }
    }    

    private void resamplingModeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.resamplingModeChange(value);
        }
    }    

    private void publicKeyChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.publicKeyChange(value);
        }
    }    

    private void tidalUserNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalUserNameChange(value);
        }
    }    

    private void newSamplingRateChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.newSamplingRateChange(value);
        }
    }    

    private void outputDeemphasisChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputDeemphasisChange(value);
        }
    }    

    private void qobuzQualityChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.qobuzQualityChange(value);
        }
    }    

    private void magicPlayChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.magicPlayChange(value);
        }
    }    

    private void leedhVolumeSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.leedhVolumeSupportChange(value);
        }
    }    

    private void mQASampleRateChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQASampleRateChange(value);
        }
    }    

    private void firmwareCommandChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.firmwareCommandChange(value);
        }
    }    

    private void uSFilterDSDChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.uSFilterDSDChange(value);
        }
    }    

    private void outputBitDepthChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputBitDepthChange(value);
        }
    }    

    private void autoPlayChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.autoPlayChange(value);
        }
    }    

    private void magicAudioVerChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.magicAudioVerChange(value);
        }
    }    

    private void volumeControlChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.volumeControlChange(value);
        }
    }    

    private void rAATVerChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.rAATVerChange(value);
        }
    }    

    private void outputInvertPhaseChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputInvertPhaseChange(value);
        }
    }    

    private void networkLEDChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.networkLEDChange(value);
        }
    }    

    private void serviceIdChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.serviceIdChange(value);
        }
    }    

    private void resamplingTagChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.resamplingTagChange(value);
        }
    }    

    private void mQAModeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQAModeChange(value);
        }
    }    

    private void networkLEDControlChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.networkLEDControlChange(value);
        }
    }    

    private void screensaverTimeoutChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.screensaverTimeoutChange(value);
        }
    }    

    private void externalClockSupportChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.externalClockSupportChange(value);
        }
    }    

    private void screensaverModeChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.screensaverModeChange(value);
        }
    }    

    private void mQACreatorIdChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQACreatorIdChange(value);
        }
    }    

    private void songcastModeChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.songcastModeChange(value);
        }
    }    

    private void outputClockSourceChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputClockSourceChange(value);
        }
    }    

    private void tidalRefreshTokenChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalRefreshTokenChange(value);
        }
    }    

    private void rAATEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.rAATEnableChange(value);
        }
    }    

    private void playbackClockSourceChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.playbackClockSourceChange(value);
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

    private void tidalAccessTokenChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalAccessTokenChange(value);
        }
    }    

    private void firmwareResultChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.firmwareResultChange(value);
        }
    }    

    private void mQASupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQASupportChange(value);
        }
    }    

    private void analogOutLvlChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.analogOutLvlChange(value);
        }
    }    

    private void tidalConnectEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalConnectEnableChange(value);
        }
    }    

    private void outputSampleRateChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.outputSampleRateChange(value);
        }
    }    

    private void samplingRateChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.samplingRateChange(value);
        }
    }    

    private void mQAProvenanceChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQAProvenanceChange(value);
        }
    }    

    private void songcastSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.songcastSupportChange(value);
        }
    }    

    private void fileTypeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.fileTypeChange(value);
        }
    }    

    private void tuneInUserNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tuneInUserNameChange(value);
        }
    }    

    private void tidalClientSecretChange(byte[] value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.tidalClientSecretChange(value);
        }
    }    

    private void volumeControlSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.volumeControlSupportChange(value);
        }
    }    

    private void deemphasisChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.deemphasisChange(value);
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

    private void leedhVolumeEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.leedhVolumeEnableChange(value);
        }
    }    

    private void mQAAuthenticityChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.mQAAuthenticityChange(value);
        }
    }    
}
