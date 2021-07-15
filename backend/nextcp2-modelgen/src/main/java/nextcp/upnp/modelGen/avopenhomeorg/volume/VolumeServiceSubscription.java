package nextcp.upnp.modelGen.avopenhomeorg.volume;

import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.UnsignedVariableInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP subscription service class.  
 */
public class VolumeServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(VolumeServiceSubscription.class.getName());

    private List<IVolumeServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected VolumeServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IVolumeServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IVolumeServiceEventListener listener)
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
        log.warn("ended");
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
                    case "VolumeSteps":
                        volumeStepsChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "FadeMax":
                        fadeMaxChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "VolumeLimit":
                        volumeLimitChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Volume":
                        volumeChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "BalanceMax":
                        balanceMaxChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "VolumeMilliDbPerStep":
                        volumeMilliDbPerStepChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "VolumeMax":
                        volumeMaxChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Mute":
                        muteChange((Boolean) stateVar.getValue());
                        break;
                    case "Balance":
                        balanceChange((Integer) stateVar.getValue());
                        break;
                    case "Fade":
                        fadeChange((Integer) stateVar.getValue());
                        break;
                    case "VolumeUnity":
                        volumeUnityChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
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

    private void volumeStepsChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.volumeStepsChange(value);
        }
    }    

    private void fadeMaxChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.fadeMaxChange(value);
        }
    }    

    private void volumeLimitChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.volumeLimitChange(value);
        }
    }    

    private void volumeChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.volumeChange(value);
        }
    }    

    private void balanceMaxChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.balanceMaxChange(value);
        }
    }    

    private void volumeMilliDbPerStepChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.volumeMilliDbPerStepChange(value);
        }
    }    

    private void volumeMaxChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.volumeMaxChange(value);
        }
    }    

    private void muteChange(Boolean value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.muteChange(value);
        }
    }    

    private void balanceChange(Integer value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.balanceChange(value);
        }
    }    

    private void fadeChange(Integer value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.fadeChange(value);
        }
    }    

    private void volumeUnityChange(Long value)
    {
        for (IVolumeServiceEventListener listener : eventListener)
        {
            listener.volumeUnityChange(value);
        }
    }    
}
