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

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class SetScreensaver extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetScreensaver.class.getName());
    private ActionInvocation<?> invocation;

    public SetScreensaver(Service service, SetScreensaverInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetScreensaver"), new NextcpClientInfo()), cp);
		
        if (input.Mode != null) {
    	    getActionInvocation().setInput("Mode", new UnsignedIntegerFourBytes(input.Mode));
		} else {
    	    getActionInvocation().setInput("Mode", null);
		}
        if (input.Timeout != null) {
    	    getActionInvocation().setInput("Timeout", new UnsignedIntegerFourBytes(input.Timeout));
		} else {
    	    getActionInvocation().setInput("Timeout", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
