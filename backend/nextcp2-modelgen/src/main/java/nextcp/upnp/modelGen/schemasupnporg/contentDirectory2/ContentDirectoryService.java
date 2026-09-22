package nextcp.upnp.modelGen.schemasupnporg.contentDirectory2;

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

import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.Browse;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.BrowseOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetFeatureList;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetFeatureListOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSearchCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSearchCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSortCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSortCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSortExtensionCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSortExtensionCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSystemUpdateID;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.GetSystemUpdateIDOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.Search;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.SearchOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.SearchInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.X_GetFeatureList;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory2.actions.X_GetFeatureListOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class ContentDirectoryService
{
    private static Logger log = LoggerFactory.getLogger(ContentDirectoryService.class.getName());

    private RemoteService contentDirectoryService = null;

    private UpnpService upnpService = null;

//    private ContentDirectoryServiceStateVariable contentDirectoryServiceStateVariable = new ContentDirectoryServiceStateVariable();
    
    private ContentDirectoryServiceSubscription subscription = null;
    
    public ContentDirectoryService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public ContentDirectoryService(UpnpService upnpService, RemoteDevice device, IContentDirectoryServiceEventListener listener)
    {
        this.upnpService = upnpService;
        contentDirectoryService = device.findService(new ServiceType("schemas-upnp-org", "ContentDirectory"));
        if (contentDirectoryService != null)
        {
	        subscription = new ContentDirectoryServiceSubscription(contentDirectoryService, 600);
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
	
	        log.info(String.format("initialized service 'ContentDirectory' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'ContentDirectory' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(IContentDirectoryServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(IContentDirectoryServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getContentDirectoryService()
    {
        return contentDirectoryService;
    }    

    /** Whether the device announces this action - most of a service is optional. */
    public boolean hasAction(String actionName)
    {
        return contentDirectoryService != null && contentDirectoryService.getAction(actionName) != null;
    }


//
// Actions
// =========================================================================
//



    public BrowseOutput browse(BrowseInput inp)
    {
        if (!hasAction("Browse"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Browse of service ContentDirectory");
        }
        Browse browse = new Browse(contentDirectoryService, inp, upnpService.getControlPoint());
        BrowseOutput res = browse.executeAction();
        return res;        
    }

    public GetFeatureListOutput getFeatureList()
    {
        if (!hasAction("GetFeatureList"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetFeatureList of service ContentDirectory");
        }
        GetFeatureList getFeatureList = new GetFeatureList(contentDirectoryService,  upnpService.getControlPoint());
        GetFeatureListOutput res = getFeatureList.executeAction();
        return res;        
    }

    public GetSearchCapabilitiesOutput getSearchCapabilities()
    {
        if (!hasAction("GetSearchCapabilities"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSearchCapabilities of service ContentDirectory");
        }
        GetSearchCapabilities getSearchCapabilities = new GetSearchCapabilities(contentDirectoryService,  upnpService.getControlPoint());
        GetSearchCapabilitiesOutput res = getSearchCapabilities.executeAction();
        return res;        
    }

    public GetSortCapabilitiesOutput getSortCapabilities()
    {
        if (!hasAction("GetSortCapabilities"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSortCapabilities of service ContentDirectory");
        }
        GetSortCapabilities getSortCapabilities = new GetSortCapabilities(contentDirectoryService,  upnpService.getControlPoint());
        GetSortCapabilitiesOutput res = getSortCapabilities.executeAction();
        return res;        
    }

    public GetSortExtensionCapabilitiesOutput getSortExtensionCapabilities()
    {
        if (!hasAction("GetSortExtensionCapabilities"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSortExtensionCapabilities of service ContentDirectory");
        }
        GetSortExtensionCapabilities getSortExtensionCapabilities = new GetSortExtensionCapabilities(contentDirectoryService,  upnpService.getControlPoint());
        GetSortExtensionCapabilitiesOutput res = getSortExtensionCapabilities.executeAction();
        return res;        
    }

    public GetSystemUpdateIDOutput getSystemUpdateID()
    {
        if (!hasAction("GetSystemUpdateID"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action GetSystemUpdateID of service ContentDirectory");
        }
        GetSystemUpdateID getSystemUpdateID = new GetSystemUpdateID(contentDirectoryService,  upnpService.getControlPoint());
        GetSystemUpdateIDOutput res = getSystemUpdateID.executeAction();
        return res;        
    }

    public SearchOutput search(SearchInput inp)
    {
        if (!hasAction("Search"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action Search of service ContentDirectory");
        }
        Search search = new Search(contentDirectoryService, inp, upnpService.getControlPoint());
        SearchOutput res = search.executeAction();
        return res;        
    }

    public X_GetFeatureListOutput x_GetFeatureList()
    {
        if (!hasAction("X_GetFeatureList"))
        {
            throw new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action X_GetFeatureList of service ContentDirectory");
        }
        X_GetFeatureList x_GetFeatureList = new X_GetFeatureList(contentDirectoryService,  upnpService.getControlPoint());
        X_GetFeatureListOutput res = x_GetFeatureList.executeAction();
        return res;        
    }
}
