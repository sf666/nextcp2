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
public class GetComponentContextStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetComponentContextStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetComponentContextStatus(Service service, GetComponentContextStatusInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetComponentContextStatus"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ComponentName", input.ComponentName);
    }

    public GetComponentContextStatusOutput executeAction()
    {
        invocation = execute();

        GetComponentContextStatusOutput result = new GetComponentContextStatusOutput();

  		if (invocation.getOutput("ContextStatus").getValue() != null)
  		{
	        result.ContextStatus = invocation.getOutput("ContextStatus").getValue().toString();
  		}
  		else
  		{
	        result.ContextStatus = "";
  		}

        return result;
    }
}
