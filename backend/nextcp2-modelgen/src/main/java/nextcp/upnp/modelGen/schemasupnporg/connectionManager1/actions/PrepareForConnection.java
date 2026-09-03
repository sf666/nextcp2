package nextcp.upnp.modelGen.schemasupnporg.connectionManager1.actions;

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
public class PrepareForConnection extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(PrepareForConnection.class.getName());
    private ActionInvocation<?> invocation;

    public PrepareForConnection(Service service, PrepareForConnectionInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("PrepareForConnection"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Direction", UpnpValue.forInput(getActionInvocation(), "Direction", input.Direction));
        getActionInvocation().setInput("PeerConnectionID", UpnpValue.forInput(getActionInvocation(), "PeerConnectionID", input.PeerConnectionID));
        getActionInvocation().setInput("PeerConnectionManager", UpnpValue.forInput(getActionInvocation(), "PeerConnectionManager", input.PeerConnectionManager));
        getActionInvocation().setInput("RemoteProtocolInfo", UpnpValue.forInput(getActionInvocation(), "RemoteProtocolInfo", input.RemoteProtocolInfo));
    }

    public PrepareForConnectionOutput executeAction()
    {
        invocation = execute();

        PrepareForConnectionOutput result = new PrepareForConnectionOutput();

        result.AVTransportID = UpnpValue.toInteger(invocation.getOutput("AVTransportID").getValue());
        result.ConnectionID = UpnpValue.toInteger(invocation.getOutput("ConnectionID").getValue());
        result.RcsID = UpnpValue.toInteger(invocation.getOutput("RcsID").getValue());

        return result;
    }
}
