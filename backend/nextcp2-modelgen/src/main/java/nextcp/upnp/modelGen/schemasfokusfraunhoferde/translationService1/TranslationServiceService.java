package nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1;

import org.jupnp.UpnpService;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.protocol.ProtocolCreationException;
import org.jupnp.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationList;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationListOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationListInput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetUpdateID;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetUpdateIDOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetLanguageIDList;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetLanguageIDListOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslation;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService1.actions.GetTranslationInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: service.ftl
 * 
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class TranslationServiceService
{
    private static Logger log = LoggerFactory.getLogger(TranslationServiceService.class.getName());

    private RemoteService translationServiceService = null;

    private UpnpService upnpService = null;

    private TranslationServiceServiceStateVariable translationServiceServiceStateVariable = new TranslationServiceServiceStateVariable();
    
    private TranslationServiceServiceSubscription subscription = null;
    
    public TranslationServiceService(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        translationServiceService = device.findService(new ServiceType("schemas-fokus-fraunhofer-de", "TranslationService"));
        if (translationServiceService != null)
        {
	        subscription = new TranslationServiceServiceSubscription(translationServiceService, 600);
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
    
    public void addSubscriptionEventListener(ITranslationServiceServiceEventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(ITranslationServiceServiceEventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService getTranslationServiceService()
    {
        return translationServiceService;
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
}
