package nextcp.upnp.modelGen.avopenhomeorg.volume3;

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
public class VolumeServiceEventListenerImpl implements IVolumeServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(VolumeServiceEventListenerImpl.class.getName());
    private VolumeServiceStateVariable stateVariable = new VolumeServiceStateVariable();
    private RemoteDevice device = null;
    
    
	public VolumeServiceEventListenerImpl(RemoteDevice device) {
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
    public VolumeServiceStateVariable getStateVariable()
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
    public void volumeStepsChange(Long value)
    {
        stateVariable.VolumeSteps = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeSteps", value));
        }
    }
    
    public void fadeMaxChange(Long value)
    {
        stateVariable.FadeMax = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "FadeMax", value));
        }
    }
    
    public void volumeLimitChange(Long value)
    {
        stateVariable.VolumeLimit = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeLimit", value));
        }
    }
    
    public void volumeChange(Long value)
    {
        stateVariable.Volume = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Volume", value));
        }
    }
    
    public void balanceMaxChange(Long value)
    {
        stateVariable.BalanceMax = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "BalanceMax", value));
        }
    }
    
    public void volumeMilliDbPerStepChange(Long value)
    {
        stateVariable.VolumeMilliDbPerStep = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeMilliDbPerStep", value));
        }
    }
    
    public void volumeMaxChange(Long value)
    {
        stateVariable.VolumeMax = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeMax", value));
        }
    }
    
    public void muteChange(Boolean value)
    {
        stateVariable.Mute = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Mute", value));
        }
    }
    
    public void balanceChange(Integer value)
    {
        stateVariable.Balance = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Balance", value));
        }
    }
    
    public void fadeChange(Integer value)
    {
        stateVariable.Fade = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Fade", value));
        }
    }
    
    public void volumeUnityChange(Long value)
    {
        stateVariable.VolumeUnity = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeUnity", value));
        }
    }
    
}
