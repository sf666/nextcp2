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
public class Set extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Set.class.getName());
    private ActionInvocation<?> invocation;

    public Set(Service service, SetInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Set")), cp);

        getActionInvocation().setInput("Id", input.Id);
        getActionInvocation().setInput("UserName", input.UserName);
        throw new RuntimeException("(Base64Datatype)");
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
