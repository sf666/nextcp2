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
public class GetCurrentMainTVChannel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentMainTVChannel.class.getName());
    private ActionInvocation<?> invocation;

    public GetCurrentMainTVChannel(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentMainTVChannel"), new NextcpClientInfo()), cp);

    }

    public GetCurrentMainTVChannelOutput executeAction()
    {
        invocation = execute();

        GetCurrentMainTVChannelOutput result = new GetCurrentMainTVChannelOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("CurrentChannel").getValue() != null)
  		{
	        result.CurrentChannel = invocation.getOutput("CurrentChannel").getValue().toString();
  		}
  		else
  		{
	        result.CurrentChannel = "";
  		}

        return result;
    }
}
