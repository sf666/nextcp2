package nextcp.upnp.modelGen.avopenhomeorg.pins1.actions;

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
public class InvokeId extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(InvokeId.class.getName());
    private ActionInvocation<?> invocation;

    public InvokeId(Service service, InvokeIdInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("InvokeId"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Id", new UnsignedIntegerFourBytes(input.Id));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
