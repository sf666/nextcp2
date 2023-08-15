package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1;

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
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceSubscription.ftl
 *  
 * Generated UPnP subscription service class.  
 */
public class X_MS_MediaReceiverRegistrarServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(X_MS_MediaReceiverRegistrarServiceSubscription.class.getName());

    private List<IX_MS_MediaReceiverRegistrarServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected X_MS_MediaReceiverRegistrarServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IX_MS_MediaReceiverRegistrarServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IX_MS_MediaReceiverRegistrarServiceEventListener listener)
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
                    case "ValidationRevokedUpdateID":
                        validationRevokedUpdateIDChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ValidationSucceededUpdateID":
                        validationSucceededUpdateIDChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "AuthorizationDeniedUpdateID":
                        authorizationDeniedUpdateIDChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "AuthorizationGrantedUpdateID":
                        authorizationGrantedUpdateIDChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
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

    private void validationRevokedUpdateIDChange(Long value)
    {
        for (IX_MS_MediaReceiverRegistrarServiceEventListener listener : eventListener)
        {
            listener.validationRevokedUpdateIDChange(value);
        }
    }    

    private void validationSucceededUpdateIDChange(Long value)
    {
        for (IX_MS_MediaReceiverRegistrarServiceEventListener listener : eventListener)
        {
            listener.validationSucceededUpdateIDChange(value);
        }
    }    

    private void authorizationDeniedUpdateIDChange(Long value)
    {
        for (IX_MS_MediaReceiverRegistrarServiceEventListener listener : eventListener)
        {
            listener.authorizationDeniedUpdateIDChange(value);
        }
    }    

    private void authorizationGrantedUpdateIDChange(Long value)
    {
        for (IX_MS_MediaReceiverRegistrarServiceEventListener listener : eventListener)
        {
            listener.authorizationGrantedUpdateIDChange(value);
        }
    }    
}
