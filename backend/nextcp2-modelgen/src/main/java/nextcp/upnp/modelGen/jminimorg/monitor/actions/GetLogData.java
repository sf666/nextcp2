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
public class GetLogData extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetLogData.class.getName());
    private ActionInvocation<?> invocation;

    public GetLogData(Service service, GetLogDataInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetLogData")), cp);

        getActionInvocation().setInput("StartPosition", new UnsignedIntegerFourBytes(input.StartPosition));
        getActionInvocation().setInput("RequestedLength", new UnsignedIntegerFourBytes(input.RequestedLength));
    }

    public GetLogDataOutput executeAction()
    {
        invocation = execute();

        GetLogDataOutput result = new GetLogDataOutput();

  		if (invocation.getOutput("LogData").getValue() != null)
  		{
	        result.LogData = invocation.getOutput("LogData").getValue().toString();
  		}
  		else
  		{
	        result.LogData = "";
  		}

        return result;
    }
}
