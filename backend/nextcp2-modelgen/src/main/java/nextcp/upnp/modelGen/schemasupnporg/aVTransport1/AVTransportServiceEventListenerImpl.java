package nextcp.upnp.modelGen.schemasupnporg.aVTransport1;

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
public class AVTransportServiceEventListenerImpl implements IAVTransportServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(AVTransportServiceEventListenerImpl.class.getName());
    private AVTransportServiceStateVariable stateVariable = new AVTransportServiceStateVariable();
    private RemoteDevice device = null;
    
    
	public AVTransportServiceEventListenerImpl(RemoteDevice device) {
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
    public void lastChangeChange(String value)
    {
        stateVariable.LastChange = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LastChange", value));
        }
    }
    
}
