package nextcp.upnp.modelGen.avopenhomeorg.info1;

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
                    case "DetailsCount":
                        detailsCountChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "MetatextCount":
                        metatextCountChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "SampleRate":
                        sampleRateChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Metadata":
                        metadataChange((String) stateVar.getValue());
                        break;
                    case "Duration":
                        durationChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "TrackCount":
                        trackCountChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "BitRate":
                        bitRateChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Uri":
                        uriChange((String) stateVar.getValue());
                        break;
                    case "BitDepth":
                        bitDepthChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "Lossless":
                        losslessChange((Boolean) stateVar.getValue());
                        break;
                    case "CodecName":
                        codecNameChange((String) stateVar.getValue());
                        break;
                    case "Metatext":
                        metatextChange((String) stateVar.getValue());
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

    private void detailsCountChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.detailsCountChange(value);
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

    private void metadataChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.metadataChange(value);
        }
    }    

    private void durationChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.durationChange(value);
        }
    }    

    private void trackCountChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.trackCountChange(value);
        }
    }    

    private void bitRateChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.bitRateChange(value);
        }
    }    

    private void uriChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.uriChange(value);
        }
    }    

    private void bitDepthChange(Long value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.bitDepthChange(value);
        }
    }    

    private void losslessChange(Boolean value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.losslessChange(value);
        }
    }    

    private void codecNameChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.codecNameChange(value);
        }
    }    

    private void metatextChange(String value)
    {
        for (IInfoServiceEventListener listener : eventListener)
        {
            listener.metatextChange(value);
        }
    }    
}
