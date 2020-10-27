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
public class GetErrorInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetErrorInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetErrorInfo(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetErrorInfo")), cp);

    }

    public GetErrorInfoOutput executeAction()
    {
        invocation = execute();

        GetErrorInfoOutput result = new GetErrorInfoOutput();

  		if (invocation.getOutput("ErrorInfo").getValue() != null)
  		{
	        result.ErrorInfo = invocation.getOutput("ErrorInfo").getValue().toString();
  		}
  		else
  		{
	        result.ErrorInfo = "";
  		}

        return result;
    }
}
