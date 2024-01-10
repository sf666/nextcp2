package nextcp.upnp.modelGen.avopenhomeorg.product2;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.state.StateVariableValue;

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
    private static Logger log = LoggerFactory.getLogger(ProductServiceEventListenerImpl.class.getName());
    private ProductServiceStateVariable stateVariable = new ProductServiceStateVariable();
    private RemoteDevice device = null;
    
    
	public ProductServiceEventListenerImpl(RemoteDevice device) {
		this.device = device;
	}
    
	private String getFriendlyName() {
        return device.getDetails().getFriendlyName();
	}
    
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
            log.info(String.format("[%s] invalidMessage : %s", getFriendlyName(), ex.getMessage()));
        }
    }

    @Override
    public void failed(UpnpResponse responseStatus)
    {
        if (log.isWarnEnabled())
        {
        	if (responseStatus != null) {
                log.warn(String.format("[%s] failed : %s", getFriendlyName(), responseStatus.getResponseDetails()));
        	} else {
                log.warn(String.format("[%s] failed with responseStatus NULL", getFriendlyName()));
        	}
        }
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        if (log.isInfoEnabled())
        {
        	String reasonStr = reason != null ? reason.toString() : "NULL";
        	String responseStatusStr = responseStatus != null ? responseStatus.toString() : "NULL";
            log.info(String.format("[%s] ended. reason : %s. UpnpResponse : %s", getFriendlyName(), reasonStr, responseStatusStr));
        }
    }

    @Override
    public void eventsMissed(int numberOfMissedEvents)
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("[%s] events missed : %d", getFriendlyName(), numberOfMissedEvents));
        }
    }

    @Override
    public void established()
    {
        if (log.isInfoEnabled())
        {
            log.info(String.format("[%s] established.", getFriendlyName()));
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
            log.debug(String.format("[%s] event received.", getFriendlyName()));
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
            log.debug(String.format("[%s] finished processing event attributes.", getFriendlyName()));
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
    
    public void attributesChange(String value)
    {
        stateVariable.Attributes = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "Attributes", value));
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
    
    public void productInfoChange(String value)
    {
        stateVariable.ProductInfo = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ProductInfo", value));
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
    
    public void sourceXmlChange(String value)
    {
        stateVariable.SourceXml = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceXml", value));
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
    
    public void manufacturerImageUriChange(String value)
    {
        stateVariable.ManufacturerImageUri = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "ManufacturerImageUri", value));
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
    
    public void sourceIndexChange(Long value)
    {
        stateVariable.SourceIndex = value;
        if (log.isDebugEnabled())
        {
            log.debug(String.format("StateVariable : %s: %s", "SourceIndex", value));
        }
    }
    
}
