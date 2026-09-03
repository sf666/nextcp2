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
import nextcp.upnp.UpnpValue;

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
		
        getActionInvocation().setInput("CurrentURI", UpnpValue.forInput(getActionInvocation(), "CurrentURI", input.CurrentURI));
        getActionInvocation().setInput("CurrentURIMetaData", UpnpValue.forInput(getActionInvocation(), "CurrentURIMetaData", input.CurrentURIMetaData));
        getActionInvocation().setInput("InstanceID", UpnpValue.forInput(getActionInvocation(), "InstanceID", input.InstanceID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
