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
public class IsAuthorized extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IsAuthorized.class.getName());
    private ActionInvocation<?> invocation;

    public IsAuthorized(Service service, IsAuthorizedInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IsAuthorized"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("DeviceID", input.DeviceID);
    }

    public IsAuthorizedOutput executeAction()
    {
        invocation = execute();

        IsAuthorizedOutput result = new IsAuthorizedOutput();

        result.Result = Integer.valueOf(invocation.getOutput("Result").getValue().toString());

        return result;
    }
}
