package nextcp.upnp.modelGen.schemasupnporg.renderingControl2;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
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
public class RenderingControlServiceEventListenerImpl implements IRenderingControlServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(RenderingControlServiceEventListenerImpl.class.getName());
    private RenderingControlServiceStateVariable stateVariable = new RenderingControlServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public RenderingControlServiceStateVariable getStateVariable()
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
    public void loudnessChange(Boolean value)
    {
        stateVariable.Loudness = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Loudness", value));
        }
    }
    
    public void brightnessChange(Long value)
    {
        stateVariable.Brightness = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Brightness", value));
        }
    }
    
    public void greenVideoBlackLevelChange(Long value)
    {
        stateVariable.GreenVideoBlackLevel = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "GreenVideoBlackLevel", value));
        }
    }
    
    public void colorTemperatureChange(Long value)
    {
        stateVariable.ColorTemperature = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ColorTemperature", value));
        }
    }
    
    public void horizontalKeystoneChange(Integer value)
    {
        stateVariable.HorizontalKeystone = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "HorizontalKeystone", value));
        }
    }
    
    public void blueVideoGainChange(Long value)
    {
        stateVariable.BlueVideoGain = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "BlueVideoGain", value));
        }
    }
    
    public void redVideoGainChange(Long value)
    {
        stateVariable.RedVideoGain = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RedVideoGain", value));
        }
    }
    
    public void verticalKeystoneChange(Integer value)
    {
        stateVariable.VerticalKeystone = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VerticalKeystone", value));
        }
    }
    
    public void redVideoBlackLevelChange(Long value)
    {
        stateVariable.RedVideoBlackLevel = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "RedVideoBlackLevel", value));
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
    
    public void volumeDBChange(Integer value)
    {
        stateVariable.VolumeDB = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "VolumeDB", value));
        }
    }
    
    public void blueVideoBlackLevelChange(Long value)
    {
        stateVariable.BlueVideoBlackLevel = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "BlueVideoBlackLevel", value));
        }
    }
    
    public void presetNameListChange(String value)
    {
        stateVariable.PresetNameList = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "PresetNameList", value));
        }
    }
    
    public void contrastChange(Long value)
    {
        stateVariable.Contrast = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Contrast", value));
        }
    }
    
    public void greenVideoGainChange(Long value)
    {
        stateVariable.GreenVideoGain = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "GreenVideoGain", value));
        }
    }
    
    public void sharpnessChange(Long value)
    {
        stateVariable.Sharpness = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Sharpness", value));
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
    
    public void lastChangeChange(String value)
    {
        stateVariable.LastChange = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "LastChange", value));
        }
    }
    
}
