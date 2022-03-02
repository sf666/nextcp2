package nextcp.upnp.modelGen.schemasupnporg.aVTransport;

import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.state.StateVariableValue;

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
public class AVTransportServiceEventListenerImpl implements IAVTransportServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(AVTransportService.class.getName());
    private AVTransportServiceStateVariable stateVariable = new AVTransportServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public AVTransportServiceStateVariable getStateVariable()
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
    public void currentMediaCategoryChange(String value)
    {
        stateVariable.CurrentMediaCategory = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentMediaCategory", value));
        }
    }
    
    public void absoluteTimePositionChange(String value)
    {
        stateVariable.AbsoluteTimePosition = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AbsoluteTimePosition", value));
        }
    }
    
    public void currentTrackURIChange(String value)
    {
        stateVariable.CurrentTrackURI = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentTrackURI", value));
        }
    }
    
    public void currentTrackMetaDataChange(String value)
    {
        stateVariable.CurrentTrackMetaData = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentTrackMetaData", value));
        }
    }
    
    public void relativeCounterPositionChange(Integer value)
    {
        stateVariable.RelativeCounterPosition = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RelativeCounterPosition", value));
        }
    }
    
    public void aVTransportURIMetaDataChange(String value)
    {
        stateVariable.AVTransportURIMetaData = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AVTransportURIMetaData", value));
        }
    }
    
    public void transportStatusChange(String value)
    {
        stateVariable.TransportStatus = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TransportStatus", value));
        }
    }
    
    public void transportStateChange(String value)
    {
        stateVariable.TransportState = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TransportState", value));
        }
    }
    
    public void currentTrackChange(Long value)
    {
        stateVariable.CurrentTrack = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentTrack", value));
        }
    }
    
    public void possibleRecordQualityModesChange(String value)
    {
        stateVariable.PossibleRecordQualityModes = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PossibleRecordQualityModes", value));
        }
    }
    
    public void playbackStorageMediumChange(String value)
    {
        stateVariable.PlaybackStorageMedium = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PlaybackStorageMedium", value));
        }
    }
    
    public void nextAVTransportURIMetaDataChange(String value)
    {
        stateVariable.NextAVTransportURIMetaData = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "NextAVTransportURIMetaData", value));
        }
    }
    
    public void numberOfTracksChange(Long value)
    {
        stateVariable.NumberOfTracks = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "NumberOfTracks", value));
        }
    }
    
    public void currentMediaDurationChange(String value)
    {
        stateVariable.CurrentMediaDuration = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentMediaDuration", value));
        }
    }
    
    public void nextAVTransportURIChange(String value)
    {
        stateVariable.NextAVTransportURI = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "NextAVTransportURI", value));
        }
    }
    
    public void recordStorageMediumChange(String value)
    {
        stateVariable.RecordStorageMedium = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RecordStorageMedium", value));
        }
    }
    
    public void aVTransportURIChange(String value)
    {
        stateVariable.AVTransportURI = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AVTransportURI", value));
        }
    }
    
    public void transportPlaySpeedChange(String value)
    {
        stateVariable.TransportPlaySpeed = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TransportPlaySpeed", value));
        }
    }
    
    public void absoluteCounterPositionChange(Integer value)
    {
        stateVariable.AbsoluteCounterPosition = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AbsoluteCounterPosition", value));
        }
    }
    
    public void relativeTimePositionChange(String value)
    {
        stateVariable.RelativeTimePosition = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RelativeTimePosition", value));
        }
    }
    
    public void dRMStateChange(String value)
    {
        stateVariable.DRMState = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "DRMState", value));
        }
    }
    
    public void currentTrackDurationChange(String value)
    {
        stateVariable.CurrentTrackDuration = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentTrackDuration", value));
        }
    }
    
    public void currentPlayModeChange(String value)
    {
        stateVariable.CurrentPlayMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentPlayMode", value));
        }
    }
    
    public void possiblePlaybackStorageMediaChange(String value)
    {
        stateVariable.PossiblePlaybackStorageMedia = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PossiblePlaybackStorageMedia", value));
        }
    }
    
    public void currentRecordQualityModeChange(String value)
    {
        stateVariable.CurrentRecordQualityMode = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentRecordQualityMode", value));
        }
    }
    
    public void recordMediumWriteStatusChange(String value)
    {
        stateVariable.RecordMediumWriteStatus = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RecordMediumWriteStatus", value));
        }
    }
    
    public void currentTransportActionsChange(String value)
    {
        stateVariable.CurrentTransportActions = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CurrentTransportActions", value));
        }
    }
    
    public void possibleRecordStorageMediaChange(String value)
    {
        stateVariable.PossibleRecordStorageMedia = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PossibleRecordStorageMedia", value));
        }
    }
    
    public void lastChangeChange(String value)
    {
        stateVariable.LastChange = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LastChange", value));
        }
    }
    
}
