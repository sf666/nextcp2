package nextcp.upnp.modelGen.jminimorg.monitor.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class GetContextStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetContextStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetContextStatus(Service service, GetContextStatusInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetContextStatus")), cp);

        getActionInvocation().setInput("ContextName", input.ContextName);
    }

    public GetContextStatusOutput executeAction()
    {
        invocation = execute();

        GetContextStatusOutput result = new GetContextStatusOutput();

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
