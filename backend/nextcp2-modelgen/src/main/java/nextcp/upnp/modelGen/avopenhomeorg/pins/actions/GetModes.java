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
public class GetModes extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetModes.class.getName());
    private ActionInvocation<?> invocation;

    public GetModes(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetModes"), new NextcpClientInfo()), cp);

    }

    public GetModesOutput executeAction()
    {
        invocation = execute();

        GetModesOutput result = new GetModesOutput();

  		if (invocation.getOutput("Modes").getValue() != null)
  		{
	        result.Modes = invocation.getOutput("Modes").getValue().toString();
  		}
  		else
  		{
	        result.Modes = "";
  		}

        return result;
    }
}
