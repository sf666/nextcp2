package nextcp.upnp.modelGen.avopenhomeorg.credentials1.actions;

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
public class Set extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Set.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public Set(Service service, SetInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Set"), new NextcpClientInfo()), cp);
		
        if (input.Id != null) {
	        getActionInvocation().setInput("Id", input.Id);
		} else {
    	    getActionInvocation().setInput("Id", null);
		}
        if (input.UserName != null) {
	        getActionInvocation().setInput("UserName", input.UserName);
		} else {
    	    getActionInvocation().setInput("UserName", null);
		}
        if (input.Password != null) {
    	    getActionInvocation().setInput("Password", b64.getString(input.Password));
		} else {
    	    getActionInvocation().setInput("Password", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
