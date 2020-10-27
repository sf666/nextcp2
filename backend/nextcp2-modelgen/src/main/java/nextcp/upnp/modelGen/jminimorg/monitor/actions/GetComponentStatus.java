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
public class GetComponentStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetComponentStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetComponentStatus(Service service, GetComponentStatusInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetComponentStatus")), cp);

        getActionInvocation().setInput("ComponentName", input.ComponentName);
    }

    public GetComponentStatusOutput executeAction()
    {
        invocation = execute();

        GetComponentStatusOutput result = new GetComponentStatusOutput();

  		if (invocation.getOutput("ComponentStatus").getValue() != null)
  		{
	        result.ComponentStatus = invocation.getOutput("ComponentStatus").getValue().toString();
  		}
  		else
  		{
	        result.ComponentStatus = "";
  		}

        return result;
    }
}
