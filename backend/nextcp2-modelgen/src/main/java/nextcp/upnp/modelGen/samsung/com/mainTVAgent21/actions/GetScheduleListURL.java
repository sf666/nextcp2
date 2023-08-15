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
public class GetScheduleListURL extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetScheduleListURL.class.getName());
    private ActionInvocation<?> invocation;

    public GetScheduleListURL(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetScheduleListURL"), new NextcpClientInfo()), cp);

    }

    public GetScheduleListURLOutput executeAction()
    {
        invocation = execute();

        GetScheduleListURLOutput result = new GetScheduleListURLOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("ScheduleListURL").getValue() != null)
  		{
	        result.ScheduleListURL = invocation.getOutput("ScheduleListURL").getValue().toString();
  		}
  		else
  		{
	        result.ScheduleListURL = "";
  		}

        return result;
    }
}
