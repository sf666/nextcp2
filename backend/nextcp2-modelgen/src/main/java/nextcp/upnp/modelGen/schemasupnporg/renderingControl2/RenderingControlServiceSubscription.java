package nextcp.upnp.modelGen.schemasupnporg.renderingControl2;

import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.gena.RemoteGENASubscription;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.state.StateVariableValue;
import org.jupnp.model.types.UnsignedVariableInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

/**
 * Last Change : 05.09.2025
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceSubscription.ftl
 *  
 * Generated UPnP subscription service class.  
 */
public class RenderingControlServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(RenderingControlServiceSubscription.class.getName());

    private List<IRenderingControlServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected RenderingControlServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IRenderingControlServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IRenderingControlServiceEventListener listener)
    {
        return eventListener.remove(listener);
    }
    
    @Override
    public void invalidMessage(UnsupportedDataException ex)
    {
        log.error("invalid message");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.invalidMessage(ex);
        }
    }

    @Override
    public void failed(UpnpResponse responseStatus)
    {
        log.warn("failed");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.failed(responseStatus);
        }
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        log.debug("ended");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.ended(reason, responseStatus);
        }
    }

    @Override
    public void eventsMissed(int numberOfMissedEvents)
    {
        log.warn("missed events count : " + numberOfMissedEvents);
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.eventsMissed(numberOfMissedEvents);
        }
    }

    @Override
    public void established()
    {
        log.debug("established");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.established();
        }
    }

    @Override
    public void eventReceived()
    {
        log.debug("eventReceived");
        Map<String, StateVariableValue<RemoteService>> values = getCurrentValues();
        for (StateVariableValue<RemoteService> stateVar : values.values())
        {
            String key = stateVar.getStateVariable().getName();
            try
            {
                switch (key)
                {
                    case "Loudness":
                        loudnessChange((Boolean) stateVar.getValue());
                        break;
                    case "Brightness":
                        brightnessChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "GreenVideoBlackLevel":
                        greenVideoBlackLevelChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ColorTemperature":
                        colorTemperatureChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "HorizontalKeystone":
                        horizontalKeystoneChange((Integer) stateVar.getValue());
                        break;
                    case "BlueVideoGain":
                        blueVideoGainChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "RedVideoGain":
                        redVideoGainChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "VerticalKeystone":
                        verticalKeystoneChange((Integer) stateVar.getValue());
                        break;
                    case "RedVideoBlackLevel":
                        redVideoBlackLevelChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Mute":
                        muteChange((Boolean) stateVar.getValue());
                        break;
                    case "VolumeDB":
                        volumeDBChange((Integer) stateVar.getValue());
                        break;
                    case "BlueVideoBlackLevel":
                        blueVideoBlackLevelChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "PresetNameList":
                        presetNameListChange((String) stateVar.getValue());
                        break;
                    case "Contrast":
                        contrastChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "GreenVideoGain":
                        greenVideoGainChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Sharpness":
                        sharpnessChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Volume":
                        volumeChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "LastChange":
                        lastChangeChange((String) stateVar.getValue());
                        break;
                    default:
                        log.warn("unknown state variable : " + key);
                }
            }
            catch (ClassCastException e)
            {
                log.error("illegal cast. Please checke code generator.", e);
            }
                            
            for (ISubscriptionEventListener listener : eventListener)
            {
                listener.eventReceived(key, stateVar);
            }
        }        
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.eventProcessed();
        }
    }

    private void loudnessChange(Boolean value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.loudnessChange(value);
        }
    }    

    private void brightnessChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.brightnessChange(value);
        }
    }    

    private void greenVideoBlackLevelChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.greenVideoBlackLevelChange(value);
        }
    }    

    private void colorTemperatureChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.colorTemperatureChange(value);
        }
    }    

    private void horizontalKeystoneChange(Integer value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.horizontalKeystoneChange(value);
        }
    }    

    private void blueVideoGainChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.blueVideoGainChange(value);
        }
    }    

    private void redVideoGainChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.redVideoGainChange(value);
        }
    }    

    private void verticalKeystoneChange(Integer value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.verticalKeystoneChange(value);
        }
    }    

    private void redVideoBlackLevelChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.redVideoBlackLevelChange(value);
        }
    }    

    private void muteChange(Boolean value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.muteChange(value);
        }
    }    

    private void volumeDBChange(Integer value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.volumeDBChange(value);
        }
    }    

    private void blueVideoBlackLevelChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.blueVideoBlackLevelChange(value);
        }
    }    

    private void presetNameListChange(String value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.presetNameListChange(value);
        }
    }    

    private void contrastChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.contrastChange(value);
        }
    }    

    private void greenVideoGainChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.greenVideoGainChange(value);
        }
    }    

    private void sharpnessChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.sharpnessChange(value);
        }
    }    

    private void volumeChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.volumeChange(value);
        }
    }    

    private void lastChangeChange(String value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.lastChangeChange(value);
        }
    }    
}
