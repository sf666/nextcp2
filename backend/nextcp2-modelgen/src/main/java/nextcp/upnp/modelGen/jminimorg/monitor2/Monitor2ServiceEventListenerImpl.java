package nextcp.upnp.modelGen.jminimorg.monitor2;

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
public class Monitor2ServiceEventListenerImpl implements IMonitor2ServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(Monitor2Service.class.getName());
    private Monitor2ServiceStateVariable stateVariable = new Monitor2ServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public Monitor2ServiceStateVariable getStateVariable()
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
    public void allContextStatusChange(String value)
    {
        stateVariable.AllContextStatus = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AllContextStatus", value));
        }
    }
    
    public void allComponentStatusChange(String value)
    {
        stateVariable.AllComponentStatus = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "AllComponentStatus", value));
        }
    }
    
    public void propertyUpdatesChange(String value)
    {
        stateVariable.PropertyUpdates = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PropertyUpdates", value));
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
    
}
