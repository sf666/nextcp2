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
public class StopRuntime extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StopRuntime.class.getName());
    private ActionInvocation<?> invocation;

    public StopRuntime(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StopRuntime"), new NextcpClientInfo()), cp);

    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
