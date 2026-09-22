package nextcp.upnp.modelGen.avopenhomeorg.product1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingRenewal;
import org.jupnp.protocol.sync.SendingSubscribe;
import org.jupnp.protocol.sync.SendingUnsubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Attributes;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.AttributesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Manufacturer;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.ManufacturerOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Model;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.ModelOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Product;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.ProductOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetProductRoom;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetProductRoomInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndex;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndexInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndexByName;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndexByNameInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetStandby;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetStandbyInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Source;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceCount;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceCountOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceIndex;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceIndexOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXml;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXmlOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXmlChangeCount;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXmlChangeCountOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Standby;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.StandbyOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class ProductService
{
    private static Logger log = LoggerFactory.getLogger(ProductService.class.getName());

    private RemoteService productService = null;

    private UpnpService upnpService = null;

//    private ProductServiceStateVariable productServiceStateVariable = new ProductServiceStateVariable();
    
    private ProductServiceSubscription subscription = null;
    
    public ProductService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public ProductService(UpnpService upnpService, RemoteDevice device, IProductServiceEventListener listener)
    {
        this.upnpService = upnpService;
        productService = device.findService(new ServiceType("av-openhome-org", "Product"));
        if (productService != null)
        {
	        subscription = new ProductServiceSubscription(productService, 600);
	        if (listener != null)
	        {
	            subscription.addSubscriptionEventListener(listener);
	        }
	        try
	        {
	            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
	            protocol.run();
	        }
	        catch (ProtocolCreationException ex)
	        {
	            log.error("Event subscription", ex);
	        }
	
	        log.info(String.format("initialized service 'Product' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'Product' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
    }

    public void unsubscribeService(UpnpService upnpService, RemoteDevice device)
    {
        SendingUnsubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingUnsubscribe(subscription);
        protocol.run();
    }

    public void renewService(UpnpService upnpService, RemoteDevice device)
    {
        SendingRenewal protocol = upnpService.getControlPoint().getProtocolFactory().createSendingRenewal(subscription);
        protocol.run();
    }

    public void addSubscriptionEventListener(IProductServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IProductServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getProductService()
    {
        return productService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return productService != null && productService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public AttributesOutput attributes()
    {
        if (!hasAction("Attributes"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Attributes of service Product");
        }
        Attributes attributes = new Attributes(productService,  upnpService.getControlPoint());
        AttributesOutput res = attributes.executeAction();
        return res;        
    }

    public ManufacturerOutput manufacturer()
    {
        if (!hasAction("Manufacturer"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Manufacturer of service Product");
        }
        Manufacturer manufacturer = new Manufacturer(productService,  upnpService.getControlPoint());
        ManufacturerOutput res = manufacturer.executeAction();
        return res;        
    }

    public ModelOutput model()
    {
        if (!hasAction("Model"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Model of service Product");
        }
        Model model = new Model(productService,  upnpService.getControlPoint());
        ModelOutput res = model.executeAction();
        return res;        
    }

    public ProductOutput product()
    {
        if (!hasAction("Product"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Product of service Product");
        }
        Product product = new Product(productService,  upnpService.getControlPoint());
        ProductOutput res = product.executeAction();
        return res;        
    }

    public void setProductRoom(SetProductRoomInput inp)
    {
        if (!hasAction("SetProductRoom"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetProductRoom of service Product");
        }
        SetProductRoom setProductRoom = new SetProductRoom(productService, inp, upnpService.getControlPoint());
        setProductRoom.executeAction();
    }

    public void setSourceIndex(SetSourceIndexInput inp)
    {
        if (!hasAction("SetSourceIndex"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetSourceIndex of service Product");
        }
        SetSourceIndex setSourceIndex = new SetSourceIndex(productService, inp, upnpService.getControlPoint());
        setSourceIndex.executeAction();
    }

    public void setSourceIndexByName(SetSourceIndexByNameInput inp)
    {
        if (!hasAction("SetSourceIndexByName"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetSourceIndexByName of service Product");
        }
        SetSourceIndexByName setSourceIndexByName = new SetSourceIndexByName(productService, inp, upnpService.getControlPoint());
        setSourceIndexByName.executeAction();
    }

    public void setStandby(SetStandbyInput inp)
    {
        if (!hasAction("SetStandby"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SetStandby of service Product");
        }
        SetStandby setStandby = new SetStandby(productService, inp, upnpService.getControlPoint());
        setStandby.executeAction();
    }

    public SourceOutput source(SourceInput inp)
    {
        if (!hasAction("Source"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Source of service Product");
        }
        Source source = new Source(productService, inp, upnpService.getControlPoint());
        SourceOutput res = source.executeAction();
        return res;        
    }

    public SourceCountOutput sourceCount()
    {
        if (!hasAction("SourceCount"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SourceCount of service Product");
        }
        SourceCount sourceCount = new SourceCount(productService,  upnpService.getControlPoint());
        SourceCountOutput res = sourceCount.executeAction();
        return res;        
    }

    public SourceIndexOutput sourceIndex()
    {
        if (!hasAction("SourceIndex"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SourceIndex of service Product");
        }
        SourceIndex sourceIndex = new SourceIndex(productService,  upnpService.getControlPoint());
        SourceIndexOutput res = sourceIndex.executeAction();
        return res;        
    }

    public SourceXmlOutput sourceXml()
    {
        if (!hasAction("SourceXml"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SourceXml of service Product");
        }
        SourceXml sourceXml = new SourceXml(productService,  upnpService.getControlPoint());
        SourceXmlOutput res = sourceXml.executeAction();
        return res;        
    }

    public SourceXmlChangeCountOutput sourceXmlChangeCount()
    {
        if (!hasAction("SourceXmlChangeCount"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SourceXmlChangeCount of service Product");
        }
        SourceXmlChangeCount sourceXmlChangeCount = new SourceXmlChangeCount(productService,  upnpService.getControlPoint());
        SourceXmlChangeCountOutput res = sourceXmlChangeCount.executeAction();
        return res;        
    }

    public StandbyOutput standby()
    {
        if (!hasAction("Standby"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Standby of service Product");
        }
        Standby standby = new Standby(productService,  upnpService.getControlPoint());
        StandbyOutput res = standby.executeAction();
        return res;        
    }
}
