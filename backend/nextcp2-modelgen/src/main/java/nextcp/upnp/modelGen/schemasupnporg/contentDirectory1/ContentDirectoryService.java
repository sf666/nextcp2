package nextcp.upnp.modelGen.schemasupnporg.contentDirectory1;

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

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.Browse;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.GetSearchCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.GetSearchCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.DestroyObject;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.DestroyObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.UpdateObject;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.UpdateObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.GetSortCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.GetSortCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.X_GetFeatureList;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.X_GetFeatureListOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReference;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateReferenceInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.Search;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.SearchOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.SearchInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.X_SetBookmark;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.X_SetBookmarkInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObject;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.CreateObjectInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.GetSystemUpdateID;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions.GetSystemUpdateIDOutput;


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
        this.upnpService = upnpService;
        contentDirectoryService = device.findService(new ServiceType("schemas-upnp-org", "ContentDirectory"));
        if (contentDirectoryService != null)
        {
	        subscription = new ContentDirectoryServiceSubscription(contentDirectoryService, 600);
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


//
// Actions
// =========================================================================
//



    public BrowseOutput browse(BrowseInput inp)
    {
        Browse browse = new Browse(contentDirectoryService, inp, upnpService.getControlPoint());
        BrowseOutput res = browse.executeAction();
        return res;        
    }

    public GetSearchCapabilitiesOutput getSearchCapabilities()
    {
        GetSearchCapabilities getSearchCapabilities = new GetSearchCapabilities(contentDirectoryService,  upnpService.getControlPoint());
        GetSearchCapabilitiesOutput res = getSearchCapabilities.executeAction();
        return res;        
    }

    public void destroyObject(DestroyObjectInput inp)
    {
        DestroyObject destroyObject = new DestroyObject(contentDirectoryService, inp, upnpService.getControlPoint());
        destroyObject.executeAction();
    }

    public void updateObject(UpdateObjectInput inp)
    {
        UpdateObject updateObject = new UpdateObject(contentDirectoryService, inp, upnpService.getControlPoint());
        updateObject.executeAction();
    }

    public GetSortCapabilitiesOutput getSortCapabilities()
    {
        GetSortCapabilities getSortCapabilities = new GetSortCapabilities(contentDirectoryService,  upnpService.getControlPoint());
        GetSortCapabilitiesOutput res = getSortCapabilities.executeAction();
        return res;        
    }

    public X_GetFeatureListOutput x_GetFeatureList()
    {
        X_GetFeatureList x_GetFeatureList = new X_GetFeatureList(contentDirectoryService,  upnpService.getControlPoint());
        X_GetFeatureListOutput res = x_GetFeatureList.executeAction();
        return res;        
    }

    public CreateReferenceOutput createReference(CreateReferenceInput inp)
    {
        CreateReference createReference = new CreateReference(contentDirectoryService, inp, upnpService.getControlPoint());
        CreateReferenceOutput res = createReference.executeAction();
        return res;        
    }

    public SearchOutput search(SearchInput inp)
    {
        Search search = new Search(contentDirectoryService, inp, upnpService.getControlPoint());
        SearchOutput res = search.executeAction();
        return res;        
    }

    public void x_SetBookmark(X_SetBookmarkInput inp)
    {
        X_SetBookmark x_SetBookmark = new X_SetBookmark(contentDirectoryService, inp, upnpService.getControlPoint());
        x_SetBookmark.executeAction();
    }

    public CreateObjectOutput createObject(CreateObjectInput inp)
    {
        CreateObject createObject = new CreateObject(contentDirectoryService, inp, upnpService.getControlPoint());
        CreateObjectOutput res = createObject.executeAction();
        return res;        
    }

    public GetSystemUpdateIDOutput getSystemUpdateID()
    {
        GetSystemUpdateID getSystemUpdateID = new GetSystemUpdateID(contentDirectoryService,  upnpService.getControlPoint());
        GetSystemUpdateIDOutput res = getSystemUpdateID.executeAction();
        return res;        
    }
}
