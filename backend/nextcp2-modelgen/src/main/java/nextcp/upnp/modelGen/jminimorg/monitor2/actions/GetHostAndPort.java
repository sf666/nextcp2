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
public class GetHostAndPort extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHostAndPort.class.getName());
    private ActionInvocation<?> invocation;

    public GetHostAndPort(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHostAndPort"), new NextcpClientInfo()), cp);

    }

    public GetHostAndPortOutput executeAction()
    {
        invocation = execute();

        GetHostAndPortOutput result = new GetHostAndPortOutput();

  		if (invocation.getOutput("HostAndPort").getValue() != null)
  		{
	        result.HostAndPort = invocation.getOutput("HostAndPort").getValue().toString();
  		}
  		else
  		{
	        result.HostAndPort = "";
  		}

        return result;
    }
}
