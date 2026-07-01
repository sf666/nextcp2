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
public class SetNextAVTransportURI extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetNextAVTransportURI.class.getName());
    private ActionInvocation<?> invocation;

    public SetNextAVTransportURI(Service service, SetNextAVTransportURIInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetNextAVTransportURI"), new NextcpClientInfo()), cp);
		
        if (input.InstanceID != null) {
    	    getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
		} else {
    	    getActionInvocation().setInput("InstanceID", null);
		}
        if (input.NextURI != null) {
	        getActionInvocation().setInput("NextURI", input.NextURI);
		} else {
    	    getActionInvocation().setInput("NextURI", null);
		}
        if (input.NextURIMetaData != null) {
	        getActionInvocation().setInput("NextURIMetaData", input.NextURIMetaData);
		} else {
    	    getActionInvocation().setInput("NextURIMetaData", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
