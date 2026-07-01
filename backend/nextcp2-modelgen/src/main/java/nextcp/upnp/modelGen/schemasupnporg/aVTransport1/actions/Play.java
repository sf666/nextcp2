package nextcp.upnp.modelGen.schemasupnporg.aVTransport1.actions;

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
public class Play extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Play.class.getName());
    private ActionInvocation<?> invocation;

    public Play(Service service, PlayInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Play"), new NextcpClientInfo()), cp);
		
        if (input.InstanceID != null) {
    	    getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
		} else {
    	    getActionInvocation().setInput("InstanceID", null);
		}
        if (input.Speed != null) {
	        getActionInvocation().setInput("Speed", input.Speed);
		} else {
    	    getActionInvocation().setInput("Speed", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
