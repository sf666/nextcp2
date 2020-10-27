package nextcp.upnp.modelGen.avopenhomeorg.sender;

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
public class SenderServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(SenderServiceSubscription.class.getName());

    private List<ISenderServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected SenderServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(ISenderServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(ISenderServiceEventListener listener)
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
                    case "Status":
                        statusChange((String) stateVar.getValue());
                        break;
                    case "PresentationUrl":
                        presentationUrlChange((String) stateVar.getValue());
                        break;
                    case "Attributes":
                        attributesChange((String) stateVar.getValue());
                        break;
                    case "Metadata":
                        metadataChange((String) stateVar.getValue());
                        break;
                    case "Audio":
                        audioChange((Boolean) stateVar.getValue());
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

    private void statusChange(String value)
    {
        for (ISenderServiceEventListener listener : eventListener)
        {
            listener.statusChange(value);
        }
    }    

    private void presentationUrlChange(String value)
    {
        for (ISenderServiceEventListener listener : eventListener)
        {
            listener.presentationUrlChange(value);
        }
    }    

    private void attributesChange(String value)
    {
        for (ISenderServiceEventListener listener : eventListener)
        {
            listener.attributesChange(value);
        }
    }    

    private void metadataChange(String value)
    {
        for (ISenderServiceEventListener listener : eventListener)
        {
            listener.metadataChange(value);
        }
    }    

    private void audioChange(Boolean value)
    {
        for (ISenderServiceEventListener listener : eventListener)
        {
            listener.audioChange(value);
        }
    }    
}
