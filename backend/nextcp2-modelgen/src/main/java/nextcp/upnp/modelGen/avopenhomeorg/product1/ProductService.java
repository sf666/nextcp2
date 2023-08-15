package nextcp.upnp.modelGen.avopenhomeorg.product1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceCount;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceCountOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Attributes;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.AttributesOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Product;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.ProductOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXml;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXmlOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndexByName;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndexByNameInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndex;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetSourceIndexInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Standby;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.StandbyOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Source;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceIndex;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceIndexOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetStandby;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SetStandbyInput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Manufacturer;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.ManufacturerOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.Model;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.ModelOutput;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXmlChangeCount;
import nextcp.upnp.modelGen.avopenhomeorg.product1.actions.SourceXmlChangeCountOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class ProductService
{
    private static Logger log = LoggerFactory.getLogger(ProductService.class.getName());

    private RemoteService productService = null;

    private UpnpService upnpService = null;

    private ProductServiceStateVariable productServiceStateVariable = new ProductServiceStateVariable();
    
    private ProductServiceSubscription subscription = null;
    
    public ProductService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        productService = device.findService(new ServiceType("av-openhome-org", "Product"));
        if (productService != null)
        {
	        subscription = new ProductServiceSubscription(productService, 600);
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
    
    public void addSubscriptionEventListener(IProductServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IProductServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getProductService()
    {
        return productService;
    }    


    public SourceCountOutput sourceCount()
    {
        SourceCount sourceCount = new SourceCount(productService,  upnpService.getControlPoint());
        SourceCountOutput res = sourceCount.executeAction();
        return res;        
    }

    public AttributesOutput attributes()
    {
        Attributes attributes = new Attributes(productService,  upnpService.getControlPoint());
        AttributesOutput res = attributes.executeAction();
        return res;        
    }

    public ProductOutput product()
    {
        Product product = new Product(productService,  upnpService.getControlPoint());
        ProductOutput res = product.executeAction();
        return res;        
    }

    public SourceXmlOutput sourceXml()
    {
        SourceXml sourceXml = new SourceXml(productService,  upnpService.getControlPoint());
        SourceXmlOutput res = sourceXml.executeAction();
        return res;        
    }

    public void setSourceIndexByName(SetSourceIndexByNameInput inp)
    {
        SetSourceIndexByName setSourceIndexByName = new SetSourceIndexByName(productService, inp, upnpService.getControlPoint());
        setSourceIndexByName.executeAction();
    }

    public void setSourceIndex(SetSourceIndexInput inp)
    {
        SetSourceIndex setSourceIndex = new SetSourceIndex(productService, inp, upnpService.getControlPoint());
        setSourceIndex.executeAction();
    }

    public StandbyOutput standby()
    {
        Standby standby = new Standby(productService,  upnpService.getControlPoint());
        StandbyOutput res = standby.executeAction();
        return res;        
    }

    public SourceOutput source(SourceInput inp)
    {
        Source source = new Source(productService, inp, upnpService.getControlPoint());
        SourceOutput res = source.executeAction();
        return res;        
    }

    public SourceIndexOutput sourceIndex()
    {
        SourceIndex sourceIndex = new SourceIndex(productService,  upnpService.getControlPoint());
        SourceIndexOutput res = sourceIndex.executeAction();
        return res;        
    }

    public void setStandby(SetStandbyInput inp)
    {
        SetStandby setStandby = new SetStandby(productService, inp, upnpService.getControlPoint());
        setStandby.executeAction();
    }

    public ManufacturerOutput manufacturer()
    {
        Manufacturer manufacturer = new Manufacturer(productService,  upnpService.getControlPoint());
        ManufacturerOutput res = manufacturer.executeAction();
        return res;        
    }

    public ModelOutput model()
    {
        Model model = new Model(productService,  upnpService.getControlPoint());
        ModelOutput res = model.executeAction();
        return res;        
    }

    public SourceXmlChangeCountOutput sourceXmlChangeCount()
    {
        SourceXmlChangeCount sourceXmlChangeCount = new SourceXmlChangeCount(productService,  upnpService.getControlPoint());
        SourceXmlChangeCountOutput res = sourceXmlChangeCount.executeAction();
        return res;        
    }
}
