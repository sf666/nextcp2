package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class SetControl4Enable extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetControl4Enable.class.getName());
    private ActionInvocation<?> invocation;

    public SetControl4Enable(Service service, SetControl4EnableInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetControl4Enable"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Value", UpnpValue.forInput(getActionInvocation(), "Value", input.Value));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
