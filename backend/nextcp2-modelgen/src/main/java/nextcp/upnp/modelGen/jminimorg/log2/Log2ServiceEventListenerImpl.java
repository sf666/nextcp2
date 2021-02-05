package nextcp.upnp.modelGen.jminimorg.log2;

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
public class Log2ServiceEventListenerImpl implements ILog2ServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(Log2Service.class.getName());
    private Log2ServiceStateVariable stateVariable = new Log2ServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public Log2ServiceStateVariable getStateVariable()
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
    public void logStartChange(Long value)
    {
        stateVariable.LogStart = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LogStart", value));
        }
    }
    
    public void logDataLengthChange(Long value)
    {
        stateVariable.LogDataLength = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LogDataLength", value));
        }
    }
    
    public void logLevelChange(String value)
    {
        stateVariable.LogLevel = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LogLevel", value));
        }
    }
    
}
