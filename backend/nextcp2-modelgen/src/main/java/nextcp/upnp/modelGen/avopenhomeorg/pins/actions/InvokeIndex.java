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
public class InvokeIndex extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(InvokeIndex.class.getName());
    private ActionInvocation<?> invocation;

    public InvokeIndex(Service service, InvokeIndexInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("InvokeIndex")), cp);

        getActionInvocation().setInput("Index", new UnsignedIntegerFourBytes(input.Index));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
