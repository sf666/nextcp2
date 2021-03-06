package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar;

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
public class X_MS_MediaReceiverRegistrarServiceEventListenerImpl implements IX_MS_MediaReceiverRegistrarServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(X_MS_MediaReceiverRegistrarService.class.getName());
    private X_MS_MediaReceiverRegistrarServiceStateVariable stateVariable = new X_MS_MediaReceiverRegistrarServiceStateVariable();

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
    
    public void authorizationDeniedUpdateIDChange(Long value)
    {
        stateVariable.AuthorizationDeniedUpdateID = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AuthorizationDeniedUpdateID", value));
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
    
}
