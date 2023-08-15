package nextcp.upnp.modelGen.avopenhomeorg.oAuth1.actions;

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
public class GetJobStatus extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetJobStatus.class.getName());
    private ActionInvocation<?> invocation;

    public GetJobStatus(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetJobStatus"), new NextcpClientInfo()), cp);

    }

    public GetJobStatusOutput executeAction()
    {
        invocation = execute();

        GetJobStatusOutput result = new GetJobStatusOutput();

  		if (invocation.getOutput("JobStatusJson").getValue() != null)
  		{
	        result.JobStatusJson = invocation.getOutput("JobStatusJson").getValue().toString();
  		}
  		else
  		{
	        result.JobStatusJson = "";
  		}

        return result;
    }
}
