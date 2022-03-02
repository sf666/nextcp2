package nextcp.upnp.modelGen.schemasfokusfraunhoferde.translationService;

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface ITranslationServiceServiceEventListener extends ISubscriptionEventListener 
{
    public void languageIDListChange(String value);
    
    public void updateIDChange(String value);
    
}
