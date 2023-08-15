package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

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
public class DeleteSchedule extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(DeleteSchedule.class.getName());
    private ActionInvocation<?> invocation;

    public DeleteSchedule(Service service, DeleteScheduleInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("DeleteSchedule"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("UID", input.UID);
    }

    public DeleteScheduleOutput executeAction()
    {
        invocation = execute();

        DeleteScheduleOutput result = new DeleteScheduleOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}

        return result;
    }
}
