package ${packageName};

import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.gena.RemoteGENASubscription;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.state.StateVariableValue;
import org.jupnp.model.types.UnsignedVariableInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

/**
 * Last Change : 08.09.2025
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Template: serviceSubscription.ftl
 *  
 * Generated UPnP subscription service class.  
 */
public class ${className}Subscription extends RemoteGENASubscription
{
    private static final Logger log = LoggerFactory.getLogger(${className}Subscription.class.getName());

    private List<I${className}EventListener> eventListener = new CopyOnWriteArrayList<>();
        
    protected ${className}Subscription(RemoteService service, int requestedDurationSeconds)
    {
        super(service, requestedDurationSeconds);
    }

    public void addSubscriptionEventListener(I${className}EventListener listener)
    {
        eventListener.add(listener);
    }
    
    public boolean removeSubscriptionEventListener(I${className}EventListener listener)
    {
        return eventListener.remove(listener);
    }
    
    @Override
    public void invalidMessage(UnsupportedDataException ex)
    {
        log.error("invalid message");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.invalidMessage(ex);
        }
    }

    @Override
    public void failed(UpnpResponse responseStatus)
    {
        log.warn("failed");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.failed(responseStatus);
        }
    }

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        log.debug("ended");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.ended(reason, responseStatus);
        }
    }

    @Override
    public void eventsMissed(int numberOfMissedEvents)
    {
        log.warn("missed events count : " + numberOfMissedEvents);
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.eventsMissed(numberOfMissedEvents);
        }
    }

    @Override
    public void established()
    {
        log.debug("established");
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.established();
        }
    }

    @Override
    public void eventReceived()
    {
        log.debug("eventReceived");
        Map<String, StateVariableValue<RemoteService>> values = getCurrentValues();
        for (StateVariableValue<RemoteService> stateVar : values.values())
        {
            String key = stateVar.getStateVariable().getName();
            try
            {
                switch (key)
                {
<#assign unsignedInteger = ["ui1", "ui2", "ui4"]>
<#list stateVariables as stateVar>
                    case "${stateVar.name}":
<#if unsignedInteger?seq_contains(stateVar.upnpType)>                    
                        ${stateVar.name?uncap_first}Change(((UnsignedVariableInteger) stateVar.getValue()).getValue());
<#elseif "Boolean" == "${stateVar.type}">
                    	try {
                    		${stateVar.name?uncap_first}Change((Boolean) stateVar.getValue());
                    	} catch (Exception e) {
                    		log.warn("[${stateVar.name?uncap_first}] unexpected value : " + stateVar.getValue());
                    		${stateVar.name?uncap_first}Change(Boolean.valueOf(stateVar.getValue().toString()));
						}
<#else>
                        ${stateVar.name?uncap_first}Change((${stateVar.type}) stateVar.getValue());
</#if>
                        break;
</#list>                    
                    default:
                        log.warn("unknown state variable : " + key);
                }
            }
            catch (ClassCastException e)
            {
                log.error("illegal cast. Please checke code generator.", e);
            }
                            
            for (ISubscriptionEventListener listener : eventListener)
            {
                listener.eventReceived(key, stateVar);
            }
        }        
        for (ISubscriptionEventListener listener : eventListener)
        {
            listener.eventProcessed();
        }
    }
<#list stateVariables as stateVar>

    private void ${stateVar.name?uncap_first}Change(${stateVar.type} value)
    {
        for (I${className}EventListener listener : eventListener)
        {
            listener.${stateVar.name?uncap_first}Change(value);
        }
    }    
</#list>                    
}
