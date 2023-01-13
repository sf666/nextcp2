package ${packageName};

import nextcp.upnp.ISubscriptionEventListener;

/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceEventInterface.ftl
 *  
 * Event Listener Interface.  
 */
public interface I${className}EventListener extends ISubscriptionEventListener 
{
<#list stateVariables as stateVar>
    public void ${stateVar.name?uncap_first}Change(${stateVar.type} value);
    
</#list>       
}
