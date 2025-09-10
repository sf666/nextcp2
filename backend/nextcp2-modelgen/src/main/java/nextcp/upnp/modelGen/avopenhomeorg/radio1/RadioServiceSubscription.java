package nextcp.upnp.modelGen.avopenhomeorg.radio1;

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
public class RadioServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(RadioServiceSubscription.class.getName());

    private List<IRadioServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected RadioServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IRadioServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IRadioServiceEventListener listener)
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
                    case "Relative":
                        relativeChange((Integer) stateVar.getValue());
                        break;
                    case "IdArrayChanged":
                    	try {
                    		idArrayChangedChange((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[idArrayChanged] unexpected value : " + stateVar.getValue());
                    		idArrayChangedChange(Boolean.valueOf(stateVar.getValue().toString()));
						}
                        break;
                    case "Metadata":
                        metadataChange((String) stateVar.getValue());
                        break;
                    case "IdArray":
                        idArrayChange((byte[]) stateVar.getValue());
                        break;
                    case "TransportState":
                        transportStateChange((String) stateVar.getValue());
                        break;
                    case "Absolute":
                        absoluteChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Uri":
                        uriChange((String) stateVar.getValue());
                        break;
                    case "IdList":
                        idListChange((String) stateVar.getValue());
                        break;
                    case "IdArrayToken":
                        idArrayTokenChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ProtocolInfo":
                        protocolInfoChange((String) stateVar.getValue());
                        break;
                    case "ChannelsMax":
                        channelsMaxChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Id":
                        idChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ChannelList":
                        channelListChange((String) stateVar.getValue());
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

    private void relativeChange(Integer value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.relativeChange(value);
        }
    }    

    private void idArrayChangedChange(Boolean value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.idArrayChangedChange(value);
        }
    }    

    private void metadataChange(String value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.metadataChange(value);
        }
    }    

    private void idArrayChange(byte[] value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.idArrayChange(value);
        }
    }    

    private void transportStateChange(String value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.transportStateChange(value);
        }
    }    

    private void absoluteChange(Long value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.absoluteChange(value);
        }
    }    

    private void uriChange(String value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.uriChange(value);
        }
    }    

    private void idListChange(String value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.idListChange(value);
        }
    }    

    private void idArrayTokenChange(Long value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.idArrayTokenChange(value);
        }
    }    

    private void protocolInfoChange(String value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.protocolInfoChange(value);
        }
    }    

    private void channelsMaxChange(Long value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.channelsMaxChange(value);
        }
    }    

    private void idChange(Long value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.idChange(value);
        }
    }    

    private void channelListChange(String value)
    {
        for (IRadioServiceEventListener listener : eventListener)
        {
            listener.channelListChange(value);
        }
    }    
}
