package ${packageName};

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

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
<#if varInList?filter(x -> x.upnpDataType == "(Base64Datatype)")?size != 0>
  	private Base64Datatype b64 = new Base64Datatype();
</#if>

<#if varInList?has_content>
    public ${className}(Service service, ${className}Input input, ControlPoint cp)
<#else>
    public ${className}(Service service, ControlPoint cp)
</#if>
    {
        super(new ActionInvocation(service.getAction("${className}"), new NextcpClientInfo()), cp);

<#list varInList as var>
  <#if var.upnpDataType == "(StringDatatype)">
        getActionInvocation().setInput("${var.name}", input.${var.name});
  <#elseif var.upnpDataType == "(UnsignedIntegerFourBytesDatatype)">
        getActionInvocation().setInput("${var.name}", new UnsignedIntegerFourBytes(input.${var.name}));
  <#elseif var.upnpDataType == "(IntegerDatatype)">
        getActionInvocation().setInput("${var.name}", new IntegerDatatype(input.${var.name}));
  <#elseif var.upnpDataType == "(Base64Datatype)">
        getActionInvocation().setInput("${var.name}", b64.getString(input.${var.name}));
  <#elseif var.upnpDataType == "(BooleanDatatype)">
        getActionInvocation().setInput("${var.name}", input.${var.name});
  <#else>
        throw new RuntimeException("${var.upnpDataType}");
  </#if>        
</#list>
    }

    <#if varOutList?has_content>
    <#assign longList = ['(UnsignedIntegerFourBytesDatatype)', 'UnsignedIntegerTwoBytesDatatype', 'UnsignedIntegerOneByteDatatype']>
    public ${className}Output executeAction()
    {
        invocation = execute();

        ${className}Output result = new ${className}Output();

<#list varOutList as var>
  <#if var.upnpDataType == "(StringDatatype)">
  		if (invocation.getOutput("${var.name}").getValue() != null)
  		{
	        result.${var.name} = invocation.getOutput("${var.name}").getValue().toString();
  		}
  		else
  		{
	        result.${var.name} = "";
  		}
  <#elseif var.upnpDataType == "(UnsignedIntegerFourBytesDatatype)">
        result.${var.name} = ((UnsignedIntegerFourBytes) invocation.getOutput("${var.name}").getValue()).getValue();
  <#elseif var.upnpDataType == "(UnsignedIntegerTwoBytesDatatype)">
        result.${var.name} = ((UnsignedIntegerTwoBytes) invocation.getOutput("${var.name}").getValue()).getValue();
  <#elseif var.upnpDataType == "(UnsignedIntegerOneByteDatatype)">
        result.${var.name} = ((UnsignedIntegerOneByte) invocation.getOutput("${var.name}").getValue()).getValue();
  <#elseif var.upnpDataType == "(Base64Datatype)">
        result.${var.name} = (byte[]) invocation.getOutput("${var.name}").getValue();
  <#elseif var.upnpDataType == "(IntegerDatatype)">
        result.${var.name} = Integer.valueOf(invocation.getOutput("${var.name}").getValue().toString());
  <#elseif var.upnpDataType == "(BooleanDatatype)">
        BooleanDatatype data_${var.name} = new BooleanDatatype();
        result.${var.name} = data_${var.name}.valueOf(invocation.getOutput("${var.name}").getValue().toString());
  <#else>
        throw new RuntimeException("${var.upnpDataType}");
  </#if>
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
