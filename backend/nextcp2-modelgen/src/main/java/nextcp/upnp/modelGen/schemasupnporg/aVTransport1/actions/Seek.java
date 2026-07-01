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
public class Seek extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Seek.class.getName());
    private ActionInvocation<?> invocation;

    public Seek(Service service, SeekInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Seek"), new NextcpClientInfo()), cp);
		
        if (input.InstanceID != null) {
    	    getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
		} else {
    	    getActionInvocation().setInput("InstanceID", null);
		}
        if (input.Unit != null) {
	        getActionInvocation().setInput("Unit", input.Unit);
		} else {
    	    getActionInvocation().setInput("Unit", null);
		}
        if (input.Target != null) {
	        getActionInvocation().setInput("Target", input.Target);
		} else {
    	    getActionInvocation().setInput("Target", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
