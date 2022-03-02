package nextcp.upnp.modelGen.schemasupnporg.contentDirectory.actions;

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
public class GetSortExtensionCapabilities extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSortExtensionCapabilities.class.getName());
    private ActionInvocation<?> invocation;

    public GetSortExtensionCapabilities(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSortExtensionCapabilities"), new NextcpClientInfo()), cp);

    }

    public GetSortExtensionCapabilitiesOutput executeAction()
    {
        invocation = execute();

        GetSortExtensionCapabilitiesOutput result = new GetSortExtensionCapabilitiesOutput();

  		if (invocation.getOutput("SortExtensionCaps").getValue() != null)
  		{
	        result.SortExtensionCaps = invocation.getOutput("SortExtensionCaps").getValue().toString();
  		}
  		else
  		{
	        result.SortExtensionCaps = "";
  		}

        return result;
    }
}
