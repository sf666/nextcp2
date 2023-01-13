package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class GetExternalClockSupport extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetExternalClockSupport.class.getName());
    private ActionInvocation<?> invocation;

    public GetExternalClockSupport(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetExternalClockSupport"), new NextcpClientInfo()), cp);

    }

    public GetExternalClockSupportOutput executeAction()
    {
        invocation = execute();

        GetExternalClockSupportOutput result = new GetExternalClockSupportOutput();

  		if (invocation.getOutput("Value").getValue() != null)
  		{
	        result.Value = invocation.getOutput("Value").getValue().toString();
  		}
  		else
  		{
	        result.Value = "";
  		}

        return result;
    }
}
