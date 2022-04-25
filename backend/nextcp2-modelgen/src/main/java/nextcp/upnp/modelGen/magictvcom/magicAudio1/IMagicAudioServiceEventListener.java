package nextcp.upnp.modelGen.magictvcom.magicAudio1;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface IMagicAudioServiceEventListener extends ISubscriptionEventListener 
{
    public void spotifyEnableChange(Boolean value);
    
    public void newBitDepthChange(String value);
    
    public void brightnessChange(String value);
    
    public void portChange(Long value);
    
    public void firmwareDownloadProgressChange(Integer value);
    
    public void bitDepthChange(String value);
    
    public void aboutStringChange(String value);
    
    public void serverEnabledChange(Boolean value);
    
    public void spotifyVerChange(String value);
    
    public void formatConversionChange(String value);
    
    public void langIDChange(Long value);
    
    public void tidalAccessExpiryChange(Long value);
    
    public void maxVolumeChange(Long value);
    
    public void invertPhaseChange(Boolean value);
    
    public void customCodeChange(String value);
    
    public void tidalClientIdChange(byte[] value);
    
    public void resamplingModeChange(String value);
    
    public void publicKeyChange(String value);
    
    public void tidalUserNameChange(String value);
    
    public void newSamplingRateChange(String value);
    
    public void outputDeemphasisChange(Boolean value);
    
    public void qobuzQualityChange(String value);
    
    public void magicPlayChange(Boolean value);
    
    public void leedhVolumeSupportChange(Boolean value);
    
    public void mQASampleRateChange(Long value);
    
    public void firmwareCommandChange(String value);
    
    public void uSFilterDSDChange(String value);
    
    public void outputBitDepthChange(Long value);
    
    public void autoPlayChange(Boolean value);
    
    public void magicAudioVerChange(String value);
    
    public void volumeControlChange(String value);
    
    public void rAATVerChange(String value);
    
    public void outputInvertPhaseChange(Boolean value);
    
    public void networkLEDChange(Boolean value);
    
    public void serviceIdChange(String value);
    
    public void resamplingTagChange(String value);
    
    public void mQAModeChange(String value);
    
    public void networkLEDControlChange(Boolean value);
    
    public void screensaverTimeoutChange(Long value);
    
    public void externalClockSupportChange(String value);
    
    public void screensaverModeChange(Long value);
    
    public void mQACreatorIdChange(Long value);
    
    public void songcastModeChange(Boolean value);
    
    public void outputClockSourceChange(String value);
    
    public void tidalRefreshTokenChange(byte[] value);
    
    public void rAATEnableChange(Boolean value);
    
    public void playbackClockSourceChange(String value);
    
    public void dAEnableChange(String value);
    
    public void dSDtoPCM_typeChange(String value);
    
    public void tidalAccessTokenChange(byte[] value);
    
    public void firmwareResultChange(String value);
    
    public void mQASupportChange(Boolean value);
    
    public void analogOutLvlChange(String value);
    
    public void tidalConnectEnableChange(Boolean value);
    
    public void outputSampleRateChange(Long value);
    
    public void samplingRateChange(String value);
    
    public void mQAProvenanceChange(String value);
    
    public void songcastSupportChange(Boolean value);
    
    public void fileTypeChange(String value);
    
    public void tuneInUserNameChange(String value);
    
    public void tidalClientSecretChange(byte[] value);
    
    public void volumeControlSupportChange(Boolean value);
    
    public void deemphasisChange(Boolean value);
    
    public void tidalConnectSupportChange(Boolean value);
    
    public void tidalQualityChange(String value);
    
    public void leedhVolumeEnableChange(Boolean value);
    
    public void mQAAuthenticityChange(String value);
    
}
