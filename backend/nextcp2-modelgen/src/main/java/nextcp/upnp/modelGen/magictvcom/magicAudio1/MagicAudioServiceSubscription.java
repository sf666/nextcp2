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
                    case "InputLabelSupport":
                    	try {
                    		inputLabelSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[inputLabelSupport] unexpected value : " + stateVar.getValue());
                    		inputLabelSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "SpotifyEnable":
                    	try {
                    		spotifyEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[spotifyEnable] unexpected value : " + stateVar.getValue());
                    		spotifyEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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
                    case "AppDisplayMessageId":
                        appDisplayMessageIdChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "FirmwareDownloadProgress":
                        firmwareDownloadProgressChange((Integer) stateVar.getValue());
                        break;
                    case "DefaultRadioSupport":
                    	try {
                    		defaultRadioSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[defaultRadioSupport] unexpected value : " + stateVar.getValue());
                    		defaultRadioSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "BitDepth":
                        bitDepthChange((String) stateVar.getValue());
                        break;
                    case "AboutString":
                        aboutStringChange((String) stateVar.getValue());
                        break;
                    case "ServerEnabled":
                    	try {
                    		serverEnabledChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[serverEnabled] unexpected value : " + stateVar.getValue());
                    		serverEnabledChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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
                    case "USBSPDIFMode":
                    	try {
                    		uSBSPDIFModeChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[uSBSPDIFMode] unexpected value : " + stateVar.getValue());
                    		uSBSPDIFModeChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "TidalAccessExpiry":
                        tidalAccessExpiryChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "MaxVolume":
                        maxVolumeChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "InvertPhase":
                    	try {
                    		invertPhaseChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[invertPhase] unexpected value : " + stateVar.getValue());
                    		invertPhaseChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "CustomCode":
                        customCodeChange((String) stateVar.getValue());
                        break;
                    case "PlexFriendlyName":
                        plexFriendlyNameChange((String) stateVar.getValue());
                        break;
                    case "QobuzConnectEnable":
                    	try {
                    		qobuzConnectEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[qobuzConnectEnable] unexpected value : " + stateVar.getValue());
                    		qobuzConnectEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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
                    	try {
                    		outputDeemphasisChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[outputDeemphasis] unexpected value : " + stateVar.getValue());
                    		outputDeemphasisChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "PlexSupport":
                    	try {
                    		plexSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[plexSupport] unexpected value : " + stateVar.getValue());
                    		plexSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "QobuzQuality":
                        qobuzQualityChange((String) stateVar.getValue());
                        break;
                    case "MagicPlay":
                    	try {
                    		magicPlayChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[magicPlay] unexpected value : " + stateVar.getValue());
                    		magicPlayChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "LeedhVolumeSupport":
                    	try {
                    		leedhVolumeSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[leedhVolumeSupport] unexpected value : " + stateVar.getValue());
                    		leedhVolumeSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "MQASampleRate":
                        mQASampleRateChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "AnalogBalance":
                        analogBalanceChange((Integer) stateVar.getValue());
                        break;
                    case "KKBOXSupport":
                    	try {
                    		kKBOXSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[kKBOXSupport] unexpected value : " + stateVar.getValue());
                    		kKBOXSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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
                    	try {
                    		autoPlayChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[autoPlay] unexpected value : " + stateVar.getValue());
                    		autoPlayChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "AnalogBalanceSupport":
                    	try {
                    		analogBalanceSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[analogBalanceSupport] unexpected value : " + stateVar.getValue());
                    		analogBalanceSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "PlexEnable":
                    	try {
                    		plexEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[plexEnable] unexpected value : " + stateVar.getValue());
                    		plexEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "AmazonMusicSupport":
                    	try {
                    		amazonMusicSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[amazonMusicSupport] unexpected value : " + stateVar.getValue());
                    		amazonMusicSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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
                    	try {
                    		outputInvertPhaseChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[outputInvertPhase] unexpected value : " + stateVar.getValue());
                    		outputInvertPhaseChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "NetworkLED":
                    	try {
                    		networkLEDChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[networkLED] unexpected value : " + stateVar.getValue());
                    		networkLEDChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "ServiceId":
                        serviceIdChange((String) stateVar.getValue());
                        break;
                    case "PublicDNS":
                    	try {
                    		publicDNSChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[publicDNS] unexpected value : " + stateVar.getValue());
                    		publicDNSChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "PlexCode":
                        plexCodeChange((String) stateVar.getValue());
                        break;
                    case "AppDisplayMessageTag":
                        appDisplayMessageTagChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ResamplingTag":
                        resamplingTagChange((String) stateVar.getValue());
                        break;
                    case "MQAMode":
                        mQAModeChange((String) stateVar.getValue());
                        break;
                    case "NetworkLEDControl":
                    	try {
                    		networkLEDControlChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[networkLEDControl] unexpected value : " + stateVar.getValue());
                    		networkLEDControlChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "ScreensaverTimeout":
                        screensaverTimeoutChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ExternalClockSupport":
                        externalClockSupportChange((String) stateVar.getValue());
                        break;
                    case "PlexUsername":
                        plexUsernameChange((String) stateVar.getValue());
                        break;
                    case "ScreensaverMode":
                        screensaverModeChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "MQACreatorId":
                        mQACreatorIdChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "SongcastMode":
                    	try {
                    		songcastModeChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[songcastMode] unexpected value : " + stateVar.getValue());
                    		songcastModeChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "OutputClockSource":
                        outputClockSourceChange((String) stateVar.getValue());
                        break;
                    case "TidalRefreshToken":
                        tidalRefreshTokenChange((byte[]) stateVar.getValue());
                        break;
                    case "SpotifyNormalization":
                    	try {
                    		spotifyNormalizationChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[spotifyNormalization] unexpected value : " + stateVar.getValue());
                    		spotifyNormalizationChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "RAATEnable":
                    	try {
                    		rAATEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[rAATEnable] unexpected value : " + stateVar.getValue());
                    		rAATEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "PlaybackClockSource":
                        playbackClockSourceChange((String) stateVar.getValue());
                        break;
                    case "DAEnable":
                        dAEnableChange((String) stateVar.getValue());
                        break;
                    case "DefaultRadioEnable":
                    	try {
                    		defaultRadioEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[defaultRadioEnable] unexpected value : " + stateVar.getValue());
                    		defaultRadioEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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
                        mQASupportChange((String) stateVar.getValue());
                        break;
                    case "AnalogOutLvl":
                        analogOutLvlChange((String) stateVar.getValue());
                        break;
                    case "SourceName":
                        sourceNameChange((String) stateVar.getValue());
                        break;
                    case "AmazonMusicEnable":
                    	try {
                    		amazonMusicEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[amazonMusicEnable] unexpected value : " + stateVar.getValue());
                    		amazonMusicEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "KKBOXEnable":
                    	try {
                    		kKBOXEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[kKBOXEnable] unexpected value : " + stateVar.getValue());
                    		kKBOXEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "QobuzConnectSupport":
                    	try {
                    		qobuzConnectSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[qobuzConnectSupport] unexpected value : " + stateVar.getValue());
                    		qobuzConnectSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "TidalConnectEnable":
                    	try {
                    		tidalConnectEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[tidalConnectEnable] unexpected value : " + stateVar.getValue());
                    		tidalConnectEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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
                    	try {
                    		songcastSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[songcastSupport] unexpected value : " + stateVar.getValue());
                    		songcastSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "SourceIndex":
                        sourceIndexChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "FPMode":
                        fPModeChange((String) stateVar.getValue());
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
                    case "PlexEmail":
                        plexEmailChange((String) stateVar.getValue());
                        break;
                    case "VolumeControlSupport":
                    	try {
                    		volumeControlSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[volumeControlSupport] unexpected value : " + stateVar.getValue());
                    		volumeControlSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "Deemphasis":
                    	try {
                    		deemphasisChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[deemphasis] unexpected value : " + stateVar.getValue());
                    		deemphasisChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "TidalConnectSupport":
                    	try {
                    		tidalConnectSupportChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[tidalConnectSupport] unexpected value : " + stateVar.getValue());
                    		tidalConnectSupportChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "AppDisplayMessageString":
                        appDisplayMessageStringChange((String) stateVar.getValue());
                        break;
                    case "TidalQuality":
                        tidalQualityChange((String) stateVar.getValue());
                        break;
                    case "LeedhVolumeEnable":
                    	try {
                    		leedhVolumeEnableChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[leedhVolumeEnable] unexpected value : " + stateVar.getValue());
                    		leedhVolumeEnableChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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

    private void inputLabelSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.inputLabelSupportChange(value);
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

    private void appDisplayMessageIdChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.appDisplayMessageIdChange(value);
        }
    }    

    private void firmwareDownloadProgressChange(Integer value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.firmwareDownloadProgressChange(value);
        }
    }    

    private void defaultRadioSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.defaultRadioSupportChange(value);
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

    private void uSBSPDIFModeChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.uSBSPDIFModeChange(value);
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

    private void plexFriendlyNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexFriendlyNameChange(value);
        }
    }    

    private void qobuzConnectEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.qobuzConnectEnableChange(value);
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

    private void plexSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexSupportChange(value);
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

    private void analogBalanceChange(Integer value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.analogBalanceChange(value);
        }
    }    

    private void kKBOXSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.kKBOXSupportChange(value);
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

    private void analogBalanceSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.analogBalanceSupportChange(value);
        }
    }    

    private void plexEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexEnableChange(value);
        }
    }    

    private void amazonMusicSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.amazonMusicSupportChange(value);
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

    private void publicDNSChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.publicDNSChange(value);
        }
    }    

    private void plexCodeChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexCodeChange(value);
        }
    }    

    private void appDisplayMessageTagChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.appDisplayMessageTagChange(value);
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

    private void plexUsernameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexUsernameChange(value);
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

    private void spotifyNormalizationChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.spotifyNormalizationChange(value);
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

    private void defaultRadioEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.defaultRadioEnableChange(value);
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

    private void mQASupportChange(String value)
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

    private void sourceNameChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.sourceNameChange(value);
        }
    }    

    private void amazonMusicEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.amazonMusicEnableChange(value);
        }
    }    

    private void kKBOXEnableChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.kKBOXEnableChange(value);
        }
    }    

    private void qobuzConnectSupportChange(Boolean value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.qobuzConnectSupportChange(value);
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

    private void sourceIndexChange(Long value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.sourceIndexChange(value);
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

    private void plexEmailChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.plexEmailChange(value);
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

    private void appDisplayMessageStringChange(String value)
    {
        for (IMagicAudioServiceEventListener listener : eventListener)
        {
            listener.appDisplayMessageStringChange(value);
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
