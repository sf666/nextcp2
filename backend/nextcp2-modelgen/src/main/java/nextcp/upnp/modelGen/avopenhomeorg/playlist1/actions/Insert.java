package nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions;

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
public class Insert extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Insert.class.getName());
    private ActionInvocation<?> invocation;

    public Insert(Service service, InsertInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Insert"), new NextcpClientInfo()), cp);
		
        if (input.AfterId != null) {
    	    getActionInvocation().setInput("AfterId", new UnsignedIntegerFourBytes(input.AfterId));
		} else {
    	    getActionInvocation().setInput("AfterId", null);
		}
        if (input.Uri != null) {
	        getActionInvocation().setInput("Uri", input.Uri);
		} else {
    	    getActionInvocation().setInput("Uri", null);
		}
        if (input.Metadata != null) {
	        getActionInvocation().setInput("Metadata", input.Metadata);
		} else {
    	    getActionInvocation().setInput("Metadata", null);
		}
    }

    public InsertOutput executeAction()
    {
        invocation = execute();

        InsertOutput result = new InsertOutput();

        result.NewId = ((UnsignedIntegerFourBytes) invocation.getOutput("NewId").getValue()).getValue();

        return result;
    }
}
