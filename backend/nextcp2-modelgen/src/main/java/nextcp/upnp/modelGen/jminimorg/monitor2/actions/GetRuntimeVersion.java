package nextcp.upnp.modelGen.jminimorg.monitor2.actions;

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
public class GetRuntimeVersion extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRuntimeVersion.class.getName());
    private ActionInvocation<?> invocation;

    public GetRuntimeVersion(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRuntimeVersion"), new NextcpClientInfo()), cp);

    }

    public GetRuntimeVersionOutput executeAction()
    {
        invocation = execute();

        GetRuntimeVersionOutput result = new GetRuntimeVersionOutput();

  		if (invocation.getOutput("RuntimeVersion").getValue() != null)
  		{
	        result.RuntimeVersion = invocation.getOutput("RuntimeVersion").getValue().toString();
  		}
  		else
  		{
	        result.RuntimeVersion = "";
  		}

        return result;
    }
}
