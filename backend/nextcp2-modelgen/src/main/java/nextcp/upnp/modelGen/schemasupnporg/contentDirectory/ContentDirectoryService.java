package nextcp.upnp.modelGen.schemasupnporg.contentDirectory;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.Browse;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.BrowseInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.GetSearchCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.GetSearchCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.GetSortCapabilities;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.GetSortCapabilitiesOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.Search;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.SearchOutput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.SearchInput;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.GetSystemUpdateID;
import nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions.GetSystemUpdateIDOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class ContentDirectoryService
{
    private static Logger log = LoggerFactory.getLogger(ContentDirectoryService.class.getName());

    private RemoteService contentDirectoryService = null;

    private UpnpService upnpService = null;

    private ContentDirectoryServiceStateVariable contentDirectoryServiceStateVariable = new ContentDirectoryServiceStateVariable();
    
    private ContentDirectoryServiceSubscription subscription = null;
    
    public ContentDirectoryService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        contentDirectoryService = device.findService(new ServiceType("schemas-upnp-org", "ContentDirectory"));
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
    
    public void addSubscriptionEventListener(IContentDirectoryServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(IContentDirectoryServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getContentDirectoryService()
    {
        return contentDirectoryService;
    }    


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

    public GetSortCapabilitiesOutput getSortCapabilities()
    {
        GetSortCapabilities getSortCapabilities = new GetSortCapabilities(contentDirectoryService,  upnpService.getControlPoint());
        GetSortCapabilitiesOutput res = getSortCapabilities.executeAction();
        return res;        
    }

    public SearchOutput search(SearchInput inp)
    {
        Search search = new Search(contentDirectoryService, inp, upnpService.getControlPoint());
        SearchOutput res = search.executeAction();
        return res;        
    }

    public GetSystemUpdateIDOutput getSystemUpdateID()
    {
        GetSystemUpdateID getSystemUpdateID = new GetSystemUpdateID(contentDirectoryService,  upnpService.getControlPoint());
        GetSystemUpdateIDOutput res = getSystemUpdateID.executeAction();
        return res;        
    }
}
