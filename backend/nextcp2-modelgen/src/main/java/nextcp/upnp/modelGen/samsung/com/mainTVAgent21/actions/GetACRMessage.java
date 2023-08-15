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
public class GetACRMessage extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetACRMessage.class.getName());
    private ActionInvocation<?> invocation;

    public GetACRMessage(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetACRMessage"), new NextcpClientInfo()), cp);

    }

    public GetACRMessageOutput executeAction()
    {
        invocation = execute();

        GetACRMessageOutput result = new GetACRMessageOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("Message").getValue() != null)
  		{
	        result.Message = invocation.getOutput("Message").getValue().toString();
  		}
  		else
  		{
	        result.Message = "";
  		}

        return result;
    }
}
