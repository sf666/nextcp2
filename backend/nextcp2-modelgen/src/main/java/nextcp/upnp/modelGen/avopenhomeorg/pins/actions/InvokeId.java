package nextcp.upnp.modelGen.avopenhomeorg.pins.actions;

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
public class InvokeId extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(InvokeId.class.getName());
    private ActionInvocation<?> invocation;

    public InvokeId(Service service, InvokeIdInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("InvokeId")), cp);

        getActionInvocation().setInput("Id", new UnsignedIntegerFourBytes(input.Id));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
