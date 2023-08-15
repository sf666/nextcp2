package nextcp.upnp.modelGen.avopenhomeorg.playlist1;

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
public class PlaylistServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(PlaylistServiceSubscription.class.getName());

    private List<IPlaylistServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected PlaylistServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IPlaylistServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IPlaylistServiceEventListener listener)
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
                    case "IdArrayChanged":
                        idArrayChangedChange((Boolean) stateVar.getValue());
                        break;
                    case "Relative":
                        relativeChange((Integer) stateVar.getValue());
                        break;
                    case "TracksMax":
                        tracksMaxChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Shuffle":
                        shuffleChange((Boolean) stateVar.getValue());
                        break;
                    case "TrackList":
                        trackListChange((String) stateVar.getValue());
                        break;
                    case "Metadata":
                        metadataChange((String) stateVar.getValue());
                        break;
                    case "Repeat":
                        repeatChange((Boolean) stateVar.getValue());
                        break;
                    case "Index":
                        indexChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
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
                    case "Id":
                        idChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
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

    private void idArrayChangedChange(Boolean value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.idArrayChangedChange(value);
        }
    }    

    private void relativeChange(Integer value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.relativeChange(value);
        }
    }    

    private void tracksMaxChange(Long value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.tracksMaxChange(value);
        }
    }    

    private void shuffleChange(Boolean value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.shuffleChange(value);
        }
    }    

    private void trackListChange(String value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.trackListChange(value);
        }
    }    

    private void metadataChange(String value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.metadataChange(value);
        }
    }    

    private void repeatChange(Boolean value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.repeatChange(value);
        }
    }    

    private void indexChange(Long value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.indexChange(value);
        }
    }    

    private void idArrayChange(byte[] value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.idArrayChange(value);
        }
    }    

    private void transportStateChange(String value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.transportStateChange(value);
        }
    }    

    private void absoluteChange(Long value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.absoluteChange(value);
        }
    }    

    private void uriChange(String value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.uriChange(value);
        }
    }    

    private void idListChange(String value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.idListChange(value);
        }
    }    

    private void idArrayTokenChange(Long value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.idArrayTokenChange(value);
        }
    }    

    private void protocolInfoChange(String value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.protocolInfoChange(value);
        }
    }    

    private void idChange(Long value)
    {
        for (IPlaylistServiceEventListener listener : eventListener)
        {
            listener.idChange(value);
        }
    }    
}
