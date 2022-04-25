package nextcp.upnp.modelGen.avopenhomeorg.product1;

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
public class ProductServiceSubscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(ProductServiceSubscription.class.getName());

    private List<IProductServiceEventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected ProductServiceSubscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(IProductServiceEventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(IProductServiceEventListener listener)
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
                    case "ManufacturerUrl":
                        manufacturerUrlChange((String) stateVar.getValue());
                        break;
                    case "ModelImageUri":
                        modelImageUriChange((String) stateVar.getValue());
                        break;
                    case "ProductInfo":
                        productInfoChange((String) stateVar.getValue());
                        break;
                    case "ModelInfo":
                        modelInfoChange((String) stateVar.getValue());
                        break;
                    case "ManufacturerName":
                        manufacturerNameChange((String) stateVar.getValue());
                        break;
                    case "ManufacturerInfo":
                        manufacturerInfoChange((String) stateVar.getValue());
                        break;
                    case "ModelName":
                        modelNameChange((String) stateVar.getValue());
                        break;
                    case "SourceVisible":
                        sourceVisibleChange((Boolean) stateVar.getValue());
                        break;
                    case "ProductName":
                        productNameChange((String) stateVar.getValue());
                        break;
                    case "SourceCount":
                        sourceCountChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "SourceType":
                        sourceTypeChange((String) stateVar.getValue());
                        break;
                    case "Attributes":
                        attributesChange((String) stateVar.getValue());
                        break;
                    case "SourceName":
                        sourceNameChange((String) stateVar.getValue());
                        break;
                    case "ProductRoom":
                        productRoomChange((String) stateVar.getValue());
                        break;
                    case "SourceXml":
                        sourceXmlChange((String) stateVar.getValue());
                        break;
                    case "Standby":
                        standbyChange((Boolean) stateVar.getValue());
                        break;
                    case "ManufacturerImageUri":
                        manufacturerImageUriChange((String) stateVar.getValue());
                        break;
                    case "ProductUrl":
                        productUrlChange((String) stateVar.getValue());
                        break;
                    case "ModelUrl":
                        modelUrlChange((String) stateVar.getValue());
                        break;
                    case "SourceIndex":
                        sourceIndexChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
                        break;
                    case "ProductImageUri":
                        productImageUriChange((String) stateVar.getValue());
                        break;
                    case "SourceXmlChangeCount":
                        sourceXmlChangeCountChange(((UnsignedVariableInteger) stateVar.getValue()).getValue());
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

    private void manufacturerUrlChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.manufacturerUrlChange(value);
        }
    }    

    private void modelImageUriChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.modelImageUriChange(value);
        }
    }    

    private void productInfoChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.productInfoChange(value);
        }
    }    

    private void modelInfoChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.modelInfoChange(value);
        }
    }    

    private void manufacturerNameChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.manufacturerNameChange(value);
        }
    }    

    private void manufacturerInfoChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.manufacturerInfoChange(value);
        }
    }    

    private void modelNameChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.modelNameChange(value);
        }
    }    

    private void sourceVisibleChange(Boolean value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.sourceVisibleChange(value);
        }
    }    

    private void productNameChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.productNameChange(value);
        }
    }    

    private void sourceCountChange(Long value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.sourceCountChange(value);
        }
    }    

    private void sourceTypeChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.sourceTypeChange(value);
        }
    }    

    private void attributesChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.attributesChange(value);
        }
    }    

    private void sourceNameChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.sourceNameChange(value);
        }
    }    

    private void productRoomChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.productRoomChange(value);
        }
    }    

    private void sourceXmlChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.sourceXmlChange(value);
        }
    }    

    private void standbyChange(Boolean value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.standbyChange(value);
        }
    }    

    private void manufacturerImageUriChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.manufacturerImageUriChange(value);
        }
    }    

    private void productUrlChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.productUrlChange(value);
        }
    }    

    private void modelUrlChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.modelUrlChange(value);
        }
    }    

    private void sourceIndexChange(Long value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.sourceIndexChange(value);
        }
    }    

    private void productImageUriChange(String value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.productImageUriChange(value);
        }
    }    

    private void sourceXmlChangeCountChange(Long value)
    {
        for (IProductServiceEventListener listener : eventListener)
        {
            listener.sourceXmlChangeCountChange(value);
        }
    }    
}
