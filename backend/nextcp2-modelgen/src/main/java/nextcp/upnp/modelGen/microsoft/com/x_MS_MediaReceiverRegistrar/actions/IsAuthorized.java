package nextcp.upnp.modelGen.microsoft.com.x_MS_MediaReceiverRegistrar.actions;

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
public class IsAuthorized extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IsAuthorized.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

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
