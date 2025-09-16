package nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1;

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
public class UmsExtendedServicesServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(UmsExtendedServicesServiceSubscription.class.getName());

    private List<IUmsExtendedServicesServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected UmsExtendedServicesServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IUmsExtendedServicesServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IUmsExtendedServicesServiceEventListener listener)
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
                    case "AnonymousDevicesWrite":
                        anonymousDevicesWriteChange((Boolean) stateVar.getValue());
                        break;
                    case "AudioUpdateRating":
                        audioUpdateRatingChange((Boolean) stateVar.getValue());
                        break;
                    case "AudioAddictPass":
                        audioAddictPassChange((String) stateVar.getValue());
                        break;
                    case "AudioLikesVisibleRoot":
                        audioLikesVisibleRootChange((Boolean) stateVar.getValue());
                        break;
                    case "AudioAddictEurope":
                        audioAddictEuropeChange((Boolean) stateVar.getValue());
                        break;
                    case "UpnpCdsWrite":
                        upnpCdsWriteChange((Boolean) stateVar.getValue());
                        break;
                    case "AudioAddictUser":
                        audioAddictUserChange((String) stateVar.getValue());
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

    private void anonymousDevicesWriteChange(Boolean value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.anonymousDevicesWriteChange(value);
        }
    }    

    private void audioUpdateRatingChange(Boolean value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.audioUpdateRatingChange(value);
        }
    }    

    private void audioAddictPassChange(String value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.audioAddictPassChange(value);
        }
    }    

    private void audioLikesVisibleRootChange(Boolean value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.audioLikesVisibleRootChange(value);
        }
    }    

    private void audioAddictEuropeChange(Boolean value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.audioAddictEuropeChange(value);
        }
    }    

    private void upnpCdsWriteChange(Boolean value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.upnpCdsWriteChange(value);
        }
    }    

    private void audioAddictUserChange(String value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.audioAddictUserChange(value);
        }
    }    
}
