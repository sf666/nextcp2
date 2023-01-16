package nextcp.upnp.modelGen.avopenhomeorg.product2.actions;

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
public class SetSourceIndexByName extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetSourceIndexByName.class.getName());
    private ActionInvocation<?> invocation;

    public SetSourceIndexByName(Service service, SetSourceIndexByNameInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetSourceIndexByName"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Value", input.Value);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
