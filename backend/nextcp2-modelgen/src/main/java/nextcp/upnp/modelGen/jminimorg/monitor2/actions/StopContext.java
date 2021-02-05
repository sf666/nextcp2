package nextcp.upnp.modelGen.jminimorg.monitor2.actions;

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
public class StopContext extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StopContext.class.getName());
    private ActionInvocation<?> invocation;

    public StopContext(Service service, StopContextInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StopContext"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ContextName", input.ContextName);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
