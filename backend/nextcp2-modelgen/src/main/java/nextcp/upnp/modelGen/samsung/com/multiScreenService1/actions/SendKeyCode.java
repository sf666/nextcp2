package nextcp.upnp.modelGen.samsung.com.multiScreenService1.actions;

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
