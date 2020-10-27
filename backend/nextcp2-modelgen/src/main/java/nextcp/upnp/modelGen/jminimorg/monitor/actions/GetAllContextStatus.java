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
public class GetAllContextStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAllContextStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetAllContextStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAllContextStatus")), cp);

    }

    public GetAllContextStatusOutput executeAction()
    {
        invocation = execute();

        GetAllContextStatusOutput result = new GetAllContextStatusOutput();

  		if (invocation.getOutput("ContextStatusList").getValue() != null)
  		{
	        result.ContextStatusList = invocation.getOutput("ContextStatusList").getValue().toString();
  		}
  		else
  		{
	        result.ContextStatusList = "";
  		}

        return result;
    }
}
