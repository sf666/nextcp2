package nextcp.upnp.modelGen.avopenhomeorg.info1;

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
import nextcp.upnp.UpnpValue;

/**
 * Last Change : 08.09.2025
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceSubscription.ftl
 *  
 * Generated UPnP subscription service class.  
 */
public class InfoServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(InfoServiceSubscription.class.getName());

    private List<IInfoServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected InfoServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IInfoServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IInfoServiceEventListener listener)
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
                    case "BitDepth":
                        bitDepthChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "BitRate":
                        bitRateChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "CodecName":
                        codecNameChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "DetailsCount":
                        detailsCountChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "Duration":
                        durationChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "Lossless":
                        losslessChange(UpnpValue.toBoolean(stateVar.getValue()));
                        break;
                    case "Metadata":
                        metadataChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "Metatext":
                        metatextChange(UpnpValue.toText(stateVar.getValue()));
                        break;
                    case "MetatextCount":
                        metatextCountChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "SampleRate":
                        sampleRateChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "TrackCount":
                        trackCountChange(UpnpValue.toLong(stateVar.getValue()));
                        break;
                    case "Uri":
                        uriChange(UpnpValue.toText(stateVar.getValue()));
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

    private void bitDepthChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.bitDepthChange(value);
        }
    }    

    private void bitRateChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.bitRateChange(value);
        }
    }    

    private void codecNameChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.codecNameChange(value);
        }
    }    

    private void detailsCountChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.detailsCountChange(value);
        }
    }    

    private void durationChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.durationChange(value);
        }
    }    

    private void losslessChange(Boolean value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.losslessChange(value);
        }
    }    

    private void metadataChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.metadataChange(value);
        }
    }    

    private void metatextChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.metatextChange(value);
        }
    }    

    private void metatextCountChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.metatextCountChange(value);
        }
    }    

    private void sampleRateChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.sampleRateChange(value);
        }
    }    

    private void trackCountChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.trackCountChange(value);
        }
    }    

    private void uriChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.uriChange(value);
        }
    }    
}
