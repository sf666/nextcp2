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
public class GetResamplingMode extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetResamplingMode.class.getName());
    private ActionInvocation<?> invocation;

    public GetResamplingMode(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetResamplingMode"), new NextcpClientInfo()), cp);

    }

    public GetResamplingModeOutput executeAction()
    {
        invocation = execute();

        GetResamplingModeOutput result = new GetResamplingModeOutput();

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
