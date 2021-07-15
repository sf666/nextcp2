package nextcp.upnp.modelGen.avopenhomeorg.playlist;

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
 * Generated UPnP EventListener Implementation.  
 */
public class PlaylistServiceEventListenerImpl implements IPlaylistServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(PlaylistService.class.getName());
    private PlaylistServiceStateVariable stateVariable = new PlaylistServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public PlaylistServiceStateVariable getStateVariable()
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
    public void relativeChange(Integer value)
    {
        stateVariable.Relative = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Relative", value));
        }
    }
    
    public void idArrayChangedChange(Boolean value)
    {
        stateVariable.IdArrayChanged = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "IdArrayChanged", value));
        }
    }
    
    public void tracksMaxChange(Long value)
    {
        stateVariable.TracksMax = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TracksMax", value));
        }
    }
    
    public void shuffleChange(Boolean value)
    {
        stateVariable.Shuffle = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Shuffle", value));
        }
    }
    
    public void trackListChange(String value)
    {
        stateVariable.TrackList = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TrackList", value));
        }
    }
    
    public void metadataChange(String value)
    {
        stateVariable.Metadata = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Metadata", value));
        }
    }
    
    public void repeatChange(Boolean value)
    {
        stateVariable.Repeat = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Repeat", value));
        }
    }
    
    public void indexChange(Long value)
    {
        stateVariable.Index = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Index", value));
        }
    }
    
    public void idArrayChange(byte[] value)
    {
        stateVariable.IdArray = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "IdArray", value));
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
    
    public void absoluteChange(Long value)
    {
        stateVariable.Absolute = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Absolute", value));
        }
    }
    
    public void uriChange(String value)
    {
        stateVariable.Uri = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Uri", value));
        }
    }
    
    public void idListChange(String value)
    {
        stateVariable.IdList = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "IdList", value));
        }
    }
    
    public void idArrayTokenChange(Long value)
    {
        stateVariable.IdArrayToken = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "IdArrayToken", value));
        }
    }
    
    public void protocolInfoChange(String value)
    {
        stateVariable.ProtocolInfo = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ProtocolInfo", value));
        }
    }
    
    public void idChange(Long value)
    {
        stateVariable.Id = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Id", value));
        }
    }
    
}
