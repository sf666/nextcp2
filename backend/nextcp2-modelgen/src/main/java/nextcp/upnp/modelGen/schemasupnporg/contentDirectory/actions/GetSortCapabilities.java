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
 */
public class GetSortCapabilities extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetSortCapabilities.class.getName());
    private ActionInvocation<?> invocation;

    public GetSortCapabilities(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetSortCapabilities"), new NextcpClientInfo()), cp);

    }

    public GetSortCapabilitiesOutput executeAction()
    {
        invocation = execute();

        GetSortCapabilitiesOutput result = new GetSortCapabilitiesOutput();

  		if (invocation.getOutput("SortCaps").getValue() != null)
  		{
	        result.SortCaps = invocation.getOutput("SortCaps").getValue().toString();
  		}
  		else
  		{
	        result.SortCaps = "";
  		}

        return result;
    }
}
