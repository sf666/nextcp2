package ${packageName};

import org.fourthline.cling.UpnpService;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.protocol.ProtocolCreationException;
import org.fourthline.cling.protocol.sync.SendingSubscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ISubscriptionEventListener;

<#list importClasses as impClass>
import ${impClass};
</#list>


/**
 *
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN.
 *
 * Generated UPnP Service class for calling Actions synchroniously.  
 */
public class ${className}
{
    private static Logger log = LoggerFactory.getLogger(${className}.class.getName());

    private RemoteService ${className?uncap_first} = null;

    private UpnpService upnpService = null;

    private ${stateVariableClassName} ${stateVariableClassName?uncap_first} = new ${stateVariableClassName}();
    
    private ${className}Subscription subscription = null;
    
    public ${className}(UpnpService upnpService, RemoteDevice device)
    {
        this.upnpService = upnpService;
        ${className?uncap_first} = device.findService(new ServiceType("${upnpSchema}", "${upnpService}"));
        subscription = new ${className}Subscription(${className?uncap_first}, 600);
        try
        {
            SendingSubscribe protocol = upnpService.getControlPoint().getProtocolFactory().createSendingSubscribe(subscription);
            protocol.run();
        }
        catch (ProtocolCreationException ex)
        {
            log.error("Event subscription", ex);
        }

        log.info(String.format("initialized service '${upnpService}' for device %s [%s]", device.getIdentity().getUdn(), device.getDetails().getFriendlyName()));
    }
    
    public void addSubscriptionEventListener(I${className}EventListener listener)
    {
        subscription.addSubscriptionEventListener(listener);
    }
    
    public boolean removeSubscriptionEventListener(I${className}EventListener listener)
    {
        return subscription.removeSubscriptionEventListener(listener);
    }    

    public RemoteService get${className}()
    {
        return ${className?uncap_first};
    }    

<#list actionNames as action>

    <#assign out = action + "Output">
    <#assign inp = action + "Input">
    public <#if outputClasses?seq_contains(out)>${out}<#else>void</#if> ${action?uncap_first}(<#if inputClasses?seq_contains(inp)>${inp} inp</#if>)
    {
        ${action} ${action?uncap_first} = new ${action}(${className?uncap_first}, <#if inputClasses?seq_contains(inp)>inp,</#if> upnpService.getControlPoint());
        <#if outputClasses?seq_contains(out)>
        ${action}Output res = ${action?uncap_first}.executeAction();
        return res;        
        <#else>
        ${action?uncap_first}.executeAction();
        </#if>
    }
</#list>
}
