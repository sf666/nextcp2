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
public class Play extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Play.class.getName());
    private ActionInvocation<?> invocation;

    public Play(Service service, PlayInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Play")), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Speed", input.Speed);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
