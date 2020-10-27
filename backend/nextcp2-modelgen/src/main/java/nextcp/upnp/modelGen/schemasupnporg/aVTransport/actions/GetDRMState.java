package nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.GenActionException;
import nextcp.upnp.ActionCallback;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class GetDRMState extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetDRMState.class.getName());
    private ActionInvocation<?> invocation;

    public GetDRMState(Service service, GetDRMStateInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetDRMState")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
    }

    public GetDRMStateOutput executeAction()
    {
        invocation = execute();

        GetDRMStateOutput result = new GetDRMStateOutput();

  		if (invocation.getOutput("CurrentDRMState").getValue() != null)
  		{
	        result.CurrentDRMState = invocation.getOutput("CurrentDRMState").getValue().toString();
  		}
  		else
  		{
	        result.CurrentDRMState = "";
  		}

        return result;
    }
}
