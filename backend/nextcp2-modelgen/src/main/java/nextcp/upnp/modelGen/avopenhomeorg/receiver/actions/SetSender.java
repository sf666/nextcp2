package nextcp.upnp.modelGen.avopenhomeorg.receiver.actions;

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
public class SetSender extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetSender.class.getName());
    private ActionInvocation<?> invocation;

    public SetSender(Service service, SetSenderInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetSender")), cp);

        getActionInvocation().setInput("Uri", input.Uri);
        getActionInvocation().setInput("Metadata", input.Metadata);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
