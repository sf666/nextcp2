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
public class SetAVTransportURI extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetAVTransportURI.class.getName());
    private ActionInvocation<?> invocation;

    public SetAVTransportURI(Service service, SetAVTransportURIInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetAVTransportURI"), new NextcpClientInfo()), cp);
		
        if (input.InstanceID != null) {
    	    getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
		} else {
    	    getActionInvocation().setInput("InstanceID", null);
		}
        if (input.CurrentURI != null) {
	        getActionInvocation().setInput("CurrentURI", input.CurrentURI);
		} else {
    	    getActionInvocation().setInput("CurrentURI", null);
		}
        if (input.CurrentURIMetaData != null) {
	        getActionInvocation().setInput("CurrentURIMetaData", input.CurrentURIMetaData);
		} else {
    	    getActionInvocation().setInput("CurrentURIMetaData", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
