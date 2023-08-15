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
public class ChangeSchedule extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ChangeSchedule.class.getName());
    private ActionInvocation<?> invocation;

    public ChangeSchedule(Service service, ChangeScheduleInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ChangeSchedule"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ReservationType", input.ReservationType);
        getActionInvocation().setInput("RemindInfo", input.RemindInfo);
    }

    public ChangeScheduleOutput executeAction()
    {
        invocation = execute();

        ChangeScheduleOutput result = new ChangeScheduleOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("ConflictRemindInfo").getValue() != null)
  		{
	        result.ConflictRemindInfo = invocation.getOutput("ConflictRemindInfo").getValue().toString();
  		}
  		else
  		{
	        result.ConflictRemindInfo = "";
  		}
  		if (invocation.getOutput("ConflictRemindInfoURL").getValue() != null)
  		{
	        result.ConflictRemindInfoURL = invocation.getOutput("ConflictRemindInfoURL").getValue().toString();
  		}
  		else
  		{
	        result.ConflictRemindInfoURL = "";
  		}

        return result;
    }
}
