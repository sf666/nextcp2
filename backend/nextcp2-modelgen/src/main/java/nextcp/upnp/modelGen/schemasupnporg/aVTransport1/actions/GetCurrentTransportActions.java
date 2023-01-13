package nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions;

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
public class GetCurrentTransportActions extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentTransportActions.class.getName());
    private ActionInvocation<?> invocation;

    public GetCurrentTransportActions(Service service, GetCurrentTransportActionsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentTransportActions"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetCurrentTransportActionsOutput executeAction()
    {
        invocation = execute();

        GetCurrentTransportActionsOutput result = new GetCurrentTransportActionsOutput();

  		if (invocation.getOutput("Actions").getValue() != null)
  		{
	        result.Actions = invocation.getOutput("Actions").getValue().toString();
  		}
  		else
  		{
	        result.Actions = "";
  		}

        return result;
    }
}
