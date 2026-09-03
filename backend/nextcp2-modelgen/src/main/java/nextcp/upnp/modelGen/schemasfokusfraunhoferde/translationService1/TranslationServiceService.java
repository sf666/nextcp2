package nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1;

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

import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetLanguageIDList;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetLanguageIDListOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslation;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationInput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationList;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationListOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationListInput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetUpdateID;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetUpdateIDOutput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchronously.  
 */
public class TranslationServiceService
{
    private static Logger log = LoggerFactory.getLogger(TranslationServiceService.class.getName());

    private RemoteService translationServiceService = null;

    private UpnpService upnpService = null;

//    private TranslationServiceServiceStateVariable translationServiceServiceStateVariable = new TranslationServiceServiceStateVariable();
    
    private TranslationServiceServiceSubscription subscription = null;
    
    public TranslationServiceService(UpnpService upnpService, RemoteDevice device)
    {
        this(upnpService, device, null);
    }

    /**
     * The listener is attached before the subscription request leaves, because jUPnP publishes the
     * subscription inside protocol.run(): the initial event carrying every state variable can be
     * dispatched while the caller has not yet had a chance to register its listener, and would then
     * be dropped silently. A device only ever learns those values again when one of them changes.
     */
    public TranslationServiceService(UpnpService upnpService, RemoteDevice device, ITranslationServiceServiceEventListener listener)
    {
        this.upnpService = upnpService;
        translationServiceService = device.findService(new ServiceType("schemas-fokus-fraunhofer-de", "TranslationService"));
        if (translationServiceService != null)
        {
	        subscription = new TranslationServiceServiceSubscription(translationServiceService, 600);
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
	
	        log.info(String.format("initialized service 'TranslationService' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
	    }
	    else
	    {
	        log.warn(String.format("initialized service 'TranslationService' failed for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
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

    public void addSubscriptionEventListener(ITranslationServiceServiceEventListener listener)
    {
    	if (subscription != null) {
            subscription.addSubscriptionEventListener(listener);
    	}
    }
    
    public boolean removeSubscriptionEventListener(ITranslationServiceServiceEventListener listener)
    {
    	if (subscription != null) {
    		return subscription.removeSubscriptionEventListener(listener);
    	}
    	return false;
    }    

    public RemoteService getTranslationServiceService()
    {
        return translationServiceService;
    }    


//
// Actions
// =========================================================================
//



    public GetLanguageIDListOutput getLanguageIDList()
    {
        GetLanguageIDList getLanguageIDList = new GetLanguageIDList(translationServiceService,  upnpService.getControlPoint());
        GetLanguageIDListOutput res = getLanguageIDList.executeAction();
        return res;        
    }

    public GetTranslationOutput getTranslation(GetTranslationInput inp)
    {
        GetTranslation getTranslation = new GetTranslation(translationServiceService, inp, upnpService.getControlPoint());
        GetTranslationOutput res = getTranslation.executeAction();
        return res;        
    }

    public GetTranslationListOutput getTranslationList(GetTranslationListInput inp)
    {
        GetTranslationList getTranslationList = new GetTranslationList(translationServiceService, inp, upnpService.getControlPoint());
        GetTranslationListOutput res = getTranslationList.executeAction();
        return res;        
    }

    public GetUpdateIDOutput getUpdateID()
    {
        GetUpdateID getUpdateID = new GetUpdateID(translationServiceService,  upnpService.getControlPoint());
        GetUpdateIDOutput res = getUpdateID.executeAction();
        return res;        
    }
}
