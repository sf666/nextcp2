package ${packageName};

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class ${className} extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(${className}.class.getName());
    private ActionInvocation<?> invocation;

<#if varInList?has_content>
    public ${className}(Service service, ${className}Input input, ControlPoint cp)
<#else>
    public ${className}(Service service, ControlPoint cp)
</#if>
    {
        super(new ActionInvocation(service.getAction("${className}"), new NextcpClientInfo()), cp);
		
<#list varInList as var>
        getActionInvocation().setInput("${var.name}", UpnpValue.forInput(getActionInvocation(), "${var.name}", input.${var.name}));
</#list>
    }

    <#if varOutList?has_content>
    public ${className}Output executeAction()
    {
        invocation = execute();

        ${className}Output result = new ${className}Output();

<#list varOutList as var>
        result.${var.name} = UpnpValue.${var.outputCoercion}(invocation.getOutput("${var.name}").getValue());
</#list>

        return result;
    }
<#else>
    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
</#if>
}
