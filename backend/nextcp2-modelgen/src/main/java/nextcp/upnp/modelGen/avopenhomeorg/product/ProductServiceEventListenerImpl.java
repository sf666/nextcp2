package nextcp.upnp.modelGen.avopenhomeorg.product;

import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.state.StateVariableValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventImpl.ftl
 *  
 * Generated UPnP EventListener Implementation.  
 */
public class ProductServiceEventListenerImpl implements IProductServiceEventListener 
{
    private static Logger log = LoggerFactory.getLogger(ProductService.class.getName());
    private ProductServiceStateVariable stateVariable = new ProductServiceStateVariable();

    /**
     * Access to state variable
     * 
     * @return state variable
     */
    public ProductServiceStateVariable getStateVariable()
    {
        return stateVariable;
    }

    //
    // Generic event callbacks
    // =============================================================================================================================================================================

    @Override
    public void invalidMessage(UnsupportedDataException ex)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("invalidMessage : %s", ex.getMessage()));
        }
    }

    @Override
    public void failed(UpnpResponse responseStatus)
    {
        if (log.isWarnEnabled())
        {
            log.warn(String.format("failed : %s", responseStatus.getResponseDetails()));
        }
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("ended : %s", reason.toString()));
        }
    }

    @Override
    public void eventsMissed(int numberOfMissedEvents)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("events missed : %d", numberOfMissedEvents));
        }
    }

    @Override
    public void established()
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("established."));
        }
    }

    /**
     * This method receives all published events.
     */
    @Override
    public void eventReceived(String key, StateVariableValue<RemoteService> stateVar)
    {
        if (log.isDebugEnabled())
        {
            log.debug(String.format("event received."));
        }
    }

    /**
     * This method is called, when all attributes of the event are processed. 
     */
    @Override
    public void eventProcessed()
    {
        if (log.isDebugEnabled())
        {
            log.debug("finished processing event attributes.");
        }
    }

    //
    //    Service specific event callbacks 
    // =============================================================================================================================================================================
    public void manufacturerUrlChange(String value)
    {
        stateVariable.ManufacturerUrl = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ManufacturerUrl", value));
        }
    }
    
    public void productInfoChange(String value)
    {
        stateVariable.ProductInfo = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ProductInfo", value));
        }
    }
    
    public void modelImageUriChange(String value)
    {
        stateVariable.ModelImageUri = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ModelImageUri", value));
        }
    }
    
    public void modelInfoChange(String value)
    {
        stateVariable.ModelInfo = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ModelInfo", value));
        }
    }
    
    public void manufacturerNameChange(String value)
    {
        stateVariable.ManufacturerName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ManufacturerName", value));
        }
    }
    
    public void modelNameChange(String value)
    {
        stateVariable.ModelName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ModelName", value));
        }
    }
    
    public void sourceVisibleChange(Boolean value)
    {
        stateVariable.SourceVisible = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceVisible", value));
        }
    }
    
    public void manufacturerInfoChange(String value)
    {
        stateVariable.ManufacturerInfo = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ManufacturerInfo", value));
        }
    }
    
    public void productNameChange(String value)
    {
        stateVariable.ProductName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ProductName", value));
        }
    }
    
    public void sourceCountChange(Long value)
    {
        stateVariable.SourceCount = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceCount", value));
        }
    }
    
    public void sourceTypeChange(String value)
    {
        stateVariable.SourceType = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceType", value));
        }
    }
    
    public void attributesChange(String value)
    {
        stateVariable.Attributes = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Attributes", value));
        }
    }
    
    public void sourceNameChange(String value)
    {
        stateVariable.SourceName = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceName", value));
        }
    }
    
    public void sourceXmlChange(String value)
    {
        stateVariable.SourceXml = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceXml", value));
        }
    }
    
    public void productRoomChange(String value)
    {
        stateVariable.ProductRoom = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ProductRoom", value));
        }
    }
    
    public void standbyChange(Boolean value)
    {
        stateVariable.Standby = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Standby", value));
        }
    }
    
    public void productUrlChange(String value)
    {
        stateVariable.ProductUrl = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ProductUrl", value));
        }
    }
    
    public void manufacturerImageUriChange(String value)
    {
        stateVariable.ManufacturerImageUri = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ManufacturerImageUri", value));
        }
    }
    
    public void sourceIndexChange(Long value)
    {
        stateVariable.SourceIndex = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceIndex", value));
        }
    }
    
    public void modelUrlChange(String value)
    {
        stateVariable.ModelUrl = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ModelUrl", value));
        }
    }
    
    public void productImageUriChange(String value)
    {
        stateVariable.ProductImageUri = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ProductImageUri", value));
        }
    }
    
    public void sourceXmlChangeCountChange(Long value)
    {
        stateVariable.SourceXmlChangeCount = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceXmlChangeCount", value));
        }
    }
    
}
