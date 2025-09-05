package nextcp.upnp.modelGen.schemasupnporg.aVTransport1;

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
 * Last Change : 05.09.2025
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceSubscription.ftl
 *  
 * Generated UPnP subscription service class.  
 */
public class AVTransportServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(AVTransportServiceSubscription.class.getName());

    private List<IAVTransportServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected AVTransportServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IAVTransportServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IAVTransportServiceEventListener listener)
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
                    case "AbsoluteTimePosition":
                        absoluteTimePositionChange((String) stateVar.getValue());
                        break;
                    case "CurrentTrackURI":
                        currentTrackURIChange((String) stateVar.getValue());
                        break;
                    case "CurrentTrackMetaData":
                        currentTrackMetaDataChange((String) stateVar.getValue());
                        break;
                    case "RelativeCounterPosition":
                        relativeCounterPositionChange((Integer) stateVar.getValue());
                        break;
                    case "TransportStatus":
                        transportStatusChange((String) stateVar.getValue());
                        break;
                    case "AVTransportURIMetaData":
                        aVTransportURIMetaDataChange((String) stateVar.getValue());
                        break;
                    case "TransportState":
                        transportStateChange((String) stateVar.getValue());
                        break;
                    case "CurrentTrack":
                        currentTrackChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "PlaybackStorageMedium":
                        playbackStorageMediumChange((String) stateVar.getValue());
                        break;
                    case "PossibleRecordQualityModes":
                        possibleRecordQualityModesChange((String) stateVar.getValue());
                        break;
                    case "NextAVTransportURIMetaData":
                        nextAVTransportURIMetaDataChange((String) stateVar.getValue());
                        break;
                    case "NumberOfTracks":
                        numberOfTracksChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "CurrentMediaDuration":
                        currentMediaDurationChange((String) stateVar.getValue());
                        break;
                    case "NextAVTransportURI":
                        nextAVTransportURIChange((String) stateVar.getValue());
                        break;
                    case "RecordStorageMedium":
                        recordStorageMediumChange((String) stateVar.getValue());
                        break;
                    case "AVTransportURI":
                        aVTransportURIChange((String) stateVar.getValue());
                        break;
                    case "TransportPlaySpeed":
                        transportPlaySpeedChange((String) stateVar.getValue());
                        break;
                    case "AbsoluteCounterPosition":
                        absoluteCounterPositionChange((Integer) stateVar.getValue());
                        break;
                    case "RelativeTimePosition":
                        relativeTimePositionChange((String) stateVar.getValue());
                        break;
                    case "CurrentPlayMode":
                        currentPlayModeChange((String) stateVar.getValue());
                        break;
                    case "CurrentTrackDuration":
                        currentTrackDurationChange((String) stateVar.getValue());
                        break;
                    case "PossiblePlaybackStorageMedia":
                        possiblePlaybackStorageMediaChange((String) stateVar.getValue());
                        break;
                    case "CurrentRecordQualityMode":
                        currentRecordQualityModeChange((String) stateVar.getValue());
                        break;
                    case "RecordMediumWriteStatus":
                        recordMediumWriteStatusChange((String) stateVar.getValue());
                        break;
                    case "CurrentTransportActions":
                        currentTransportActionsChange((String) stateVar.getValue());
                        break;
                    case "PossibleRecordStorageMedia":
                        possibleRecordStorageMediaChange((String) stateVar.getValue());
                        break;
                    case "LastChange":
                        lastChangeChange((String) stateVar.getValue());
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

    private void absoluteTimePositionChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.absoluteTimePositionChange(value);
        }
    }    

    private void currentTrackURIChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentTrackURIChange(value);
        }
    }    

    private void currentTrackMetaDataChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentTrackMetaDataChange(value);
        }
    }    

    private void relativeCounterPositionChange(Integer value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.relativeCounterPositionChange(value);
        }
    }    

    private void transportStatusChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.transportStatusChange(value);
        }
    }    

    private void aVTransportURIMetaDataChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.aVTransportURIMetaDataChange(value);
        }
    }    

    private void transportStateChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.transportStateChange(value);
        }
    }    

    private void currentTrackChange(Long value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentTrackChange(value);
        }
    }    

    private void playbackStorageMediumChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.playbackStorageMediumChange(value);
        }
    }    

    private void possibleRecordQualityModesChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.possibleRecordQualityModesChange(value);
        }
    }    

    private void nextAVTransportURIMetaDataChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.nextAVTransportURIMetaDataChange(value);
        }
    }    

    private void numberOfTracksChange(Long value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.numberOfTracksChange(value);
        }
    }    

    private void currentMediaDurationChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentMediaDurationChange(value);
        }
    }    

    private void nextAVTransportURIChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.nextAVTransportURIChange(value);
        }
    }    

    private void recordStorageMediumChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.recordStorageMediumChange(value);
        }
    }    

    private void aVTransportURIChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.aVTransportURIChange(value);
        }
    }    

    private void transportPlaySpeedChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.transportPlaySpeedChange(value);
        }
    }    

    private void absoluteCounterPositionChange(Integer value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.absoluteCounterPositionChange(value);
        }
    }    

    private void relativeTimePositionChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.relativeTimePositionChange(value);
        }
    }    

    private void currentPlayModeChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentPlayModeChange(value);
        }
    }    

    private void currentTrackDurationChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentTrackDurationChange(value);
        }
    }    

    private void possiblePlaybackStorageMediaChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.possiblePlaybackStorageMediaChange(value);
        }
    }    

    private void currentRecordQualityModeChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentRecordQualityModeChange(value);
        }
    }    

    private void recordMediumWriteStatusChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.recordMediumWriteStatusChange(value);
        }
    }    

    private void currentTransportActionsChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.currentTransportActionsChange(value);
        }
    }    

    private void possibleRecordStorageMediaChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.possibleRecordStorageMediaChange(value);
        }
    }    

    private void lastChangeChange(String value)
    {
        for (IAVTransportServiceEventListener listener : eventListener)
        {
            listener.lastChangeChange(value);
        }
    }    
}
