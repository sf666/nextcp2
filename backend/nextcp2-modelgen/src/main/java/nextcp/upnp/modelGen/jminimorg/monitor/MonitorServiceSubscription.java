package nextcp.upnp.modelGen.jminimorg.monitor;

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
public class MonitorServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(MonitorServiceSubscription.class.getName());

    private List<IMonitorServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected MonitorServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IMonitorServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IMonitorServiceEventListener listener)
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
                    case "AllContextStatus":
                        allContextStatusChange((String) stateVar.getValue());
                        break;
                    case "AllComponentStatus":
                        allComponentStatusChange((String) stateVar.getValue());
                        break;
                    case "PropertyUpdates":
                        propertyUpdatesChange((String) stateVar.getValue());
                        break;
                    case "LogDataLength":
                        logDataLengthChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
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
            for (ISubscriptionEventListener listener : eventListener)
            {
                listener.eventProcessed();
            }
        }        
    }

    private void allContextStatusChange(String value)
    {
        for (IMonitorServiceEventListener listener : eventListener)
        {
            listener.allContextStatusChange(value);
        }
    }    

    private void allComponentStatusChange(String value)
    {
        for (IMonitorServiceEventListener listener : eventListener)
        {
            listener.allComponentStatusChange(value);
        }
    }    

    private void propertyUpdatesChange(String value)
    {
        for (IMonitorServiceEventListener listener : eventListener)
        {
            listener.propertyUpdatesChange(value);
        }
    }    

    private void logDataLengthChange(Long value)
    {
        for (IMonitorServiceEventListener listener : eventListener)
        {
            listener.logDataLengthChange(value);
        }
    }    
}
