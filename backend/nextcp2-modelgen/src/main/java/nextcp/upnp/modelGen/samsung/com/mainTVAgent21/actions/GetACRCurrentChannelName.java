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
public class GetACRCurrentChannelName extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetACRCurrentChannelName.class.getName());
    private ActionInvocation<?> invocation;

    public GetACRCurrentChannelName(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetACRCurrentChannelName"), new NextcpClientInfo()), cp);

    }

    public GetACRCurrentChannelNameOutput executeAction()
    {
        invocation = execute();

        GetACRCurrentChannelNameOutput result = new GetACRCurrentChannelNameOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("ChannelName").getValue() != null)
  		{
	        result.ChannelName = invocation.getOutput("ChannelName").getValue().toString();
  		}
  		else
  		{
	        result.ChannelName = "";
  		}

        return result;
    }
}
