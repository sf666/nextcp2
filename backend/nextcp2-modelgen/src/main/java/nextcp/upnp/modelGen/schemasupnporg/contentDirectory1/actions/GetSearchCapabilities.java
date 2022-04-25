package nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions;

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
public class GetSearchCapabilities extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSearchCapabilities.class.getName());
    private ActionInvocation<?> invocation;

    public GetSearchCapabilities(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSearchCapabilities"), new NextcpClientInfo()), cp);

    }

    public GetSearchCapabilitiesOutput executeAction()
    {
        invocation = execute();

        GetSearchCapabilitiesOutput result = new GetSearchCapabilitiesOutput();

  		if (invocation.getOutput("SearchCaps").getValue() != null)
  		{
	        result.SearchCaps = invocation.getOutput("SearchCaps").getValue().toString();
  		}
  		else
  		{
	        result.SearchCaps = "";
  		}

        return result;
    }
}
