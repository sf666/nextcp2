package nextcp.upnp.modelGen.avopenhomeorg.playlist1;

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
public class PlaylistServiceEventListenerImpl implements IPlaylistServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(PlaylistServiceEventListenerImpl.class.getName());
    private PlaylistServiceStateVariable stateVariable = new PlaylistServiceStateVariable();
    private RemoteDevice device = null;
    
    
	public PlaylistServiceEventListenerImpl(RemoteDevice device) {
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
    public void idArrayChangedChange(Boolean value)
    {
        stateVariable.IdArrayChanged = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "IdArrayChanged", value));
        }
    }
    
    public void relativeChange(Integer value)
    {
        stateVariable.Relative = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Relative", value));
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
    
    public void indexChange(Long value)
    {
        stateVariable.Index = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Index", value));
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
