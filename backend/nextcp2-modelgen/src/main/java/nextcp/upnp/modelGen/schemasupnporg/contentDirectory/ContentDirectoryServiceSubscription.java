package nextcp.upnp.modelGen.schemasupnporg.contentDirectory;

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
public class ContentDirectoryServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(ContentDirectoryServiceSubscription.class.getName());

    private List<IContentDirectoryServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected ContentDirectoryServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IContentDirectoryServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IContentDirectoryServiceEventListener listener)
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
                    case "TransferIDs":
                        transferIDsChange((String) stateVar.getValue());
                        break;
                    case "SystemUpdateID":
                        systemUpdateIDChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "SortCapabilities":
                        sortCapabilitiesChange((String) stateVar.getValue());
                        break;
                    case "X_FeatureList":
                        x_FeatureListChange((String) stateVar.getValue());
                        break;
                    case "SortExtensionCapabilities":
                        sortExtensionCapabilitiesChange((String) stateVar.getValue());
                        break;
                    case "ContainerUpdateIDs":
                        containerUpdateIDsChange((String) stateVar.getValue());
                        break;
                    case "SearchCapabilities":
                        searchCapabilitiesChange((String) stateVar.getValue());
                        break;
                    case "FeatureList":
                        featureListChange((String) stateVar.getValue());
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

    private void transferIDsChange(String value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.transferIDsChange(value);
        }
    }    

    private void systemUpdateIDChange(Long value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.systemUpdateIDChange(value);
        }
    }    

    private void sortCapabilitiesChange(String value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.sortCapabilitiesChange(value);
        }
    }    

    private void x_FeatureListChange(String value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.x_FeatureListChange(value);
        }
    }    

    private void sortExtensionCapabilitiesChange(String value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.sortExtensionCapabilitiesChange(value);
        }
    }    

    private void containerUpdateIDsChange(String value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.containerUpdateIDsChange(value);
        }
    }    

    private void searchCapabilitiesChange(String value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.searchCapabilitiesChange(value);
        }
    }    

    private void featureListChange(String value)
    {
        for (IContentDirectoryServiceEventListener listener : eventListener)
        {
            listener.featureListChange(value);
        }
    }    
}
