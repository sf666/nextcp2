package nextcp.upnp.modelGen.jminimorg.monitor2.actions;

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
public class GetPropertyUpdates extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPropertyUpdates.class.getName());
    private ActionInvocation<?> invocation;

    public GetPropertyUpdates(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPropertyUpdates"), new NextcpClientInfo()), cp);

    }

    public GetPropertyUpdatesOutput executeAction()
    {
        invocation = execute();

        GetPropertyUpdatesOutput result = new GetPropertyUpdatesOutput();

  		if (invocation.getOutput("PropertyUpdatesList").getValue() != null)
  		{
	        result.PropertyUpdatesList = invocation.getOutput("PropertyUpdatesList").getValue().toString();
  		}
  		else
  		{
	        result.PropertyUpdatesList = "";
  		}

        return result;
    }
}
