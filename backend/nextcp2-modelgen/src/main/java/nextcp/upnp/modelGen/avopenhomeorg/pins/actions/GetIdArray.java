package nextcp.upnp.modelGen.avopenhomeorg.pins.actions;

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
public class GetIdArray extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetIdArray.class.getName());
    private ActionInvocation<?> invocation;

    public GetIdArray(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetIdArray"), new NextcpClientInfo()), cp);

    }

    public GetIdArrayOutput executeAction()
    {
        invocation = execute();

        GetIdArrayOutput result = new GetIdArrayOutput();

  		if (invocation.getOutput("IdArray").getValue() != null)
  		{
	        result.IdArray = invocation.getOutput("IdArray").getValue().toString();
  		}
  		else
  		{
	        result.IdArray = "";
  		}

        return result;
    }
}
