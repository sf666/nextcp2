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
 * Last Change : 08.09.2025
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
                    	try {
                    		anonymousDevicesWriteChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[anonymousDevicesWrite] unexpected value : " + stateVar.getValue());
                    		anonymousDevicesWriteChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "AudioAddictPass":
                        audioAddictPassChange((String) stateVar.getValue());
                        break;
                    case "AudioUpdateRating":
                    	try {
                    		audioUpdateRatingChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[audioUpdateRating] unexpected value : " + stateVar.getValue());
                    		audioUpdateRatingChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "AudioLikesVisibleRoot":
                    	try {
                    		audioLikesVisibleRootChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[audioLikesVisibleRoot] unexpected value : " + stateVar.getValue());
                    		audioLikesVisibleRootChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "AudioAddictEurope":
                    	try {
                    		audioAddictEuropeChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[audioAddictEurope] unexpected value : " + stateVar.getValue());
                    		audioAddictEuropeChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "UpnpCdsWrite":
                    	try {
                    		upnpCdsWriteChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[upnpCdsWrite] unexpected value : " + stateVar.getValue());
                    		upnpCdsWriteChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
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

    private void audioAddictPassChange(String value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.audioAddictPassChange(value);
        }
    }    

    private void audioUpdateRatingChange(Boolean value)
    {
        for (IUmsExtendedServicesServiceEventListener listener : eventListener)
        {
            listener.audioUpdateRatingChange(value);
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
