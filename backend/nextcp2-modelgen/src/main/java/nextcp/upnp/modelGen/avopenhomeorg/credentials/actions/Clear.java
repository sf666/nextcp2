package nextcp.upnp.modelGen.avopenhomeorg.credentials.actions;

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
public class Clear extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Clear.class.getName());
    private ActionInvocation<?> invocation;

    public Clear(Service service, ClearInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Clear")), cp);

        getActionInvocation().setInput("Id", input.Id);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
