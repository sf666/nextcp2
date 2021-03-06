package nextcp.upnp.modelGen.jminimorg.log2.actions;

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
public class GetLogLevel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetLogLevel.class.getName());
    private ActionInvocation<?> invocation;

    public GetLogLevel(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetLogLevel"), new NextcpClientInfo()), cp);

    }

    public GetLogLevelOutput executeAction()
    {
        invocation = execute();

        GetLogLevelOutput result = new GetLogLevelOutput();

  		if (invocation.getOutput("Level").getValue() != null)
  		{
	        result.Level = invocation.getOutput("Level").getValue().toString();
  		}
  		else
  		{
	        result.Level = "";
  		}

        return result;
    }
}
