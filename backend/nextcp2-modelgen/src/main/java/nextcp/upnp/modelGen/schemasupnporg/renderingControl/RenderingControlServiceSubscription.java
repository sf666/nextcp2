package nextcp.upnp.modelGen.schemasupnporg.renderingControl;

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
                    case "MinVolumeDB":
                        minVolumeDBChange((Integer) stateVar.getValue());
                        break;
                    case "Volume":
                        volumeChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "MaxVolumeDB":
                        maxVolumeDBChange((Integer) stateVar.getValue());
                        break;
                    case "LastChange":
                        lastChangeChange((String) stateVar.getValue());
                        break;
                    case "PresetNameList":
                        presetNameListChange((String) stateVar.getValue());
                        break;
                    case "Mute":
                        muteChange((Boolean) stateVar.getValue());
                        break;
                    case "VolumeDB":
                        volumeDBChange((Integer) stateVar.getValue());
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

    private void minVolumeDBChange(Integer value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.minVolumeDBChange(value);
        }
    }    

    private void volumeChange(Long value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.volumeChange(value);
        }
    }    

    private void maxVolumeDBChange(Integer value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.maxVolumeDBChange(value);
        }
    }    

    private void lastChangeChange(String value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.lastChangeChange(value);
        }
    }    

    private void presetNameListChange(String value)
    {
        for (IRenderingControlServiceEventListener listener : eventListener)
        {
            listener.presetNameListChange(value);
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
}
