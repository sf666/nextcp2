package nextcp.upnp.modelGen.avopenhomeorg.transport1.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class PlayAs extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(PlayAs.class.getName());
    private ActionInvocation<?> invocation;

    public PlayAs(Service service, PlayAsInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("PlayAs"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Command", UpnpValue.forInput(getActionInvocation(), "Command", input.Command));
        getActionInvocation().setInput("Mode", UpnpValue.forInput(getActionInvocation(), "Mode", input.Mode));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
