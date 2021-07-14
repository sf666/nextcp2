package nextcp.upnp.modelGen.dialmultiscreenorg.dial.actions;

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
public class SendKeyCode extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SendKeyCode.class.getName());
    private ActionInvocation<?> invocation;

    public SendKeyCode(Service service, SendKeyCodeInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SendKeyCode"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("KeyCode", new UnsignedIntegerFourBytes(input.KeyCode));
        getActionInvocation().setInput("KeyDescription", input.KeyDescription);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
