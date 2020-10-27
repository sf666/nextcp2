package nextcp.upnp.modelGen.schemasupnporg.aVTransport.actions;

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
public class Seek extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Seek.class.getName());
    private ActionInvocation<?> invocation;

    public Seek(Service service, SeekInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Seek")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Unit", input.Unit);
        getActionInvocation().setInput("Target", input.Target);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
