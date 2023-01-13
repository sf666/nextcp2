package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar1.actions;

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
public class IsValidated extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IsValidated.class.getName());
    private ActionInvocation<?> invocation;

    public IsValidated(Service service, IsValidatedInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IsValidated"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("DeviceID", input.DeviceID);
    }

    public IsValidatedOutput executeAction()
    {
        invocation = execute();

        IsValidatedOutput result = new IsValidatedOutput();

        result.Result = Integer.valueOf(invocation.getOutput("Result").getValue().toString());

        return result;
    }
}
