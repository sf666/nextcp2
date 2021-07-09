package nextcp.upnp.modelGen.avopenhomeorg.transport;

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
public class TransportServiceEventListenerImpl implements ITransportServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(TransportService.class.getName());
    private TransportServiceStateVariable stateVariable = new TransportServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public TransportServiceStateVariable getStateVariable()
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
    public void modesChange(String value)
    {
        stateVariable.Modes = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Modes", value));
        }
    }
    
    public void streamIdChange(Long value)
    {
        stateVariable.StreamId = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "StreamId", value));
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
    
    public void repeatChange(Boolean value)
    {
        stateVariable.Repeat = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Repeat", value));
        }
    }
    
    public void canSkipNextChange(Boolean value)
    {
        stateVariable.CanSkipNext = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CanSkipNext", value));
        }
    }
    
    public void canShuffleChange(Boolean value)
    {
        stateVariable.CanShuffle = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CanShuffle", value));
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
    
    public void canRepeatChange(Boolean value)
    {
        stateVariable.CanRepeat = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CanRepeat", value));
        }
    }
    
    public void canPauseChange(Boolean value)
    {
        stateVariable.CanPause = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CanPause", value));
        }
    }
    
    public void canSeekChange(Boolean value)
    {
        stateVariable.CanSeek = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CanSeek", value));
        }
    }
    
    public void canSkipPreviousChange(Boolean value)
    {
        stateVariable.CanSkipPrevious = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CanSkipPrevious", value));
        }
    }
    
}
