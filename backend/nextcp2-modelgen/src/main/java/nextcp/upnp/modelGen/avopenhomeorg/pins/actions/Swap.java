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
public class Swap extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Swap.class.getName());
    private ActionInvocation<?> invocation;

    public Swap(Service service, SwapInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Swap")), cp);

        getActionInvocation().setInput("Index1", new UnsignedIntegerFourBytes(input.Index1));
        getActionInvocation().setInput("Index2", new UnsignedIntegerFourBytes(input.Index2));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
