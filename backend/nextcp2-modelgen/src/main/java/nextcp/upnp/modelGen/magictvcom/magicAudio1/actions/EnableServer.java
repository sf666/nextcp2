package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class EnableServer extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(EnableServer.class.getName());
    private ActionInvocation<?> invocation;

    public EnableServer(Service service, EnableServerInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("EnableServer"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Enable", input.Enable);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
