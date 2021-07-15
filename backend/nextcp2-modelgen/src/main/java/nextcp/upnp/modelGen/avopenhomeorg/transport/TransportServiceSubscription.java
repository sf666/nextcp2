package nextcp.upnp.modelGen.avopenhomeorg.transport;

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
public class TransportServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(TransportServiceSubscription.class.getName());

    private List<ITransportServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected TransportServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(ITransportServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(ITransportServiceEventListener listener)
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
                    case "Modes":
                        modesChange((String) stateVar.getValue());
                        break;
                    case "StreamId":
                        streamIdChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Shuffle":
                        shuffleChange((Boolean) stateVar.getValue());
                        break;
                    case "Repeat":
                        repeatChange((Boolean) stateVar.getValue());
                        break;
                    case "CanSkipNext":
                        canSkipNextChange((Boolean) stateVar.getValue());
                        break;
                    case "CanShuffle":
                        canShuffleChange((Boolean) stateVar.getValue());
                        break;
                    case "TransportState":
                        transportStateChange((String) stateVar.getValue());
                        break;
                    case "CanRepeat":
                        canRepeatChange((Boolean) stateVar.getValue());
                        break;
                    case "CanPause":
                        canPauseChange((Boolean) stateVar.getValue());
                        break;
                    case "CanSeek":
                        canSeekChange((Boolean) stateVar.getValue());
                        break;
                    case "CanSkipPrevious":
                        canSkipPreviousChange((Boolean) stateVar.getValue());
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

    private void modesChange(String value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.modesChange(value);
        }
    }    

    private void streamIdChange(Long value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.streamIdChange(value);
        }
    }    

    private void shuffleChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.shuffleChange(value);
        }
    }    

    private void repeatChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.repeatChange(value);
        }
    }    

    private void canSkipNextChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.canSkipNextChange(value);
        }
    }    

    private void canShuffleChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.canShuffleChange(value);
        }
    }    

    private void transportStateChange(String value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.transportStateChange(value);
        }
    }    

    private void canRepeatChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.canRepeatChange(value);
        }
    }    

    private void canPauseChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.canPauseChange(value);
        }
    }    

    private void canSeekChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.canSeekChange(value);
        }
    }    

    private void canSkipPreviousChange(Boolean value)
    {
        for (ITransportServiceEventListener listener : eventListener)
        {
            listener.canSkipPreviousChange(value);
        }
    }    
}
