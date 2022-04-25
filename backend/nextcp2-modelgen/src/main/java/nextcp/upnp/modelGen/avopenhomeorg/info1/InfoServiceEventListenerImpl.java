package nextcp.upnp.modelGen.avopenhomeorg.info1;

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
public class InfoServiceEventListenerImpl implements IInfoServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(InfoService.class.getName());
    private InfoServiceStateVariable stateVariable = new InfoServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public InfoServiceStateVariable getStateVariable()
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
    public void detailsCountChange(Long value)
    {
        stateVariable.DetailsCount = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "DetailsCount", value));
        }
    }
    
    public void metatextCountChange(Long value)
    {
        stateVariable.MetatextCount = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "MetatextCount", value));
        }
    }
    
    public void sampleRateChange(Long value)
    {
        stateVariable.SampleRate = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SampleRate", value));
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
    
    public void durationChange(Long value)
    {
        stateVariable.Duration = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Duration", value));
        }
    }
    
    public void bitRateChange(Long value)
    {
        stateVariable.BitRate = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "BitRate", value));
        }
    }
    
    public void trackCountChange(Long value)
    {
        stateVariable.TrackCount = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "TrackCount", value));
        }
    }
    
    public void metatextChange(String value)
    {
        stateVariable.Metatext = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Metatext", value));
        }
    }
    
    public void losslessChange(Boolean value)
    {
        stateVariable.Lossless = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Lossless", value));
        }
    }
    
    public void codecNameChange(String value)
    {
        stateVariable.CodecName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "CodecName", value));
        }
    }
    
    public void bitDepthChange(Long value)
    {
        stateVariable.BitDepth = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "BitDepth", value));
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
    
}
