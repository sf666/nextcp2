package nextcp.upnp.modelGen.schemasupnporg.contentDirectory1.actions;

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
public class UpdateObject extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(UpdateObject.class.getName());
    private ActionInvocation<?> invocation;

    public UpdateObject(Service service, UpdateObjectInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("UpdateObject"), new NextcpClientInfo()), cp);
		
        if (input.ObjectID != null) {
	        getActionInvocation().setInput("ObjectID", input.ObjectID);
		} else {
    	    getActionInvocation().setInput("ObjectID", null);
		}
        if (input.CurrentTagValue != null) {
	        getActionInvocation().setInput("CurrentTagValue", input.CurrentTagValue);
		} else {
    	    getActionInvocation().setInput("CurrentTagValue", null);
		}
        if (input.NewTagValue != null) {
	        getActionInvocation().setInput("NewTagValue", input.NewTagValue);
		} else {
    	    getActionInvocation().setInput("NewTagValue", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
