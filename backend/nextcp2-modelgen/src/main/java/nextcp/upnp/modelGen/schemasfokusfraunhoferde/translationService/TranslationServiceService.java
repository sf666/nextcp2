package nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService;

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetTranslationList;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetTranslationListOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetTranslationListInput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetUpdateID;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetUpdateIDOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetLanguageIDList;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetLanguageIDListOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetTranslation;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetTranslationOutput;
import nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService.actions.GetTranslationInput;


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
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
