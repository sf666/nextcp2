package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1;

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
public class X_MS_MediaReceiverRegistrarServiceEventListenerImpl implements IX_MS_MediaReceiverRegistrarServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(X_MS_MediaReceiverRegistrarServiceEventListenerImpl.class.getName());
    private X_MS_MediaReceiverRegistrarServiceStateVariable stateVariable = new X_MS_MediaReceiverRegistrarServiceStateVariable();
    private RemoteDevice device = null;
    
    
	public X_MS_MediaReceiverRegistrarServiceEventListenerImpl(RemoteDevice device) {
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
    public X_MS_MediaReceiverRegistrarServiceStateVariable getStateVariable()
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
    public void validationRevokedUpdateIDChange(Long value)
    {
        stateVariable.ValidationRevokedUpdateID = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ValidationRevokedUpdateID", value));
        }
    }
    
    public void validationSucceededUpdateIDChange(Long value)
    {
        stateVariable.ValidationSucceededUpdateID = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ValidationSucceededUpdateID", value));
        }
    }
    
    public void authorizationGrantedUpdateIDChange(Long value)
    {
        stateVariable.AuthorizationGrantedUpdateID = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AuthorizationGrantedUpdateID", value));
        }
    }
    
    public void authorizationDeniedUpdateIDChange(Long value)
    {
        stateVariable.AuthorizationDeniedUpdateID = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AuthorizationDeniedUpdateID", value));
        }
    }
    
}
