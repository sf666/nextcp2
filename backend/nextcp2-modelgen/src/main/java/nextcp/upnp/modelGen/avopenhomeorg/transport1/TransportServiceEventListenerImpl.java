package nextcp.upnp.modelGen.avopenhomeorg.transport1;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.meta.RemoteDevice;
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
public class TransportServiceEventListenerImpl implements ITransportServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(TransportServiceEventListenerImpl.class.getName());
    private TransportServiceStateVariable stateVariable = new TransportServiceStateVariable();
    private RemoteDevice device = null;
    
    
	public TransportServiceEventListenerImpl(RemoteDevice device) {
		this.device = device;
	}
    
	private String getFriendlyName() {
        return device.getDetails().getFriendlyName();
	}
    
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
            log.info(String.format("[%s] invalidMessage : %s", getFriendlyName(), ex.getMessage()));
        }
    }

    @Override
    public void failed(UpnpResponse responseStatus)
    {
        if (log.isWarnEnabled())
        {
        	if (responseStatus != null) {
                log.warn(String.format("[%s] failed : %s", getFriendlyName(), responseStatus.getResponseDetails()));
        	} else {
                log.warn(String.format("[%s] failed with responseStatus NULL", getFriendlyName()));
        	}
        }
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        if (log.isInfoEnabled())
        {
        	String reasonStr = reason != null ? reason.toString() : "NULL";
        	String responseStatusStr = responseStatus != null ? responseStatus.toString() : "NULL";
            log.info(String.format("[%s] ended. reason : %s. UpnpResponse : %s", getFriendlyName(), reasonStr, responseStatusStr));
        }
    }

    @Override
    public void eventsMissed(int numberOfMissedEvents)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("[%s] events missed : %d", getFriendlyName(), numberOfMissedEvents));
        }
    }

    @Override
    public void established()
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("[%s] established.", getFriendlyName()));
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
            log.debug(String.format("[%s] event received.", getFriendlyName()));
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
            log.debug(String.format("[%s] finished processing event attributes.", getFriendlyName()));
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
    
    public void transportStateChange(String value)
    {
        stateVariable.TransportState = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TransportState", value));
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
