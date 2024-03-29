package nextcp.upnp.modelGen.schemasupnporg.connectionManager2.actions;

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
public class PrepareForConnection extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(PrepareForConnection.class.getName());
    private ActionInvocation<?> invocation;

    public PrepareForConnection(Service service, PrepareForConnectionInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("PrepareForConnection"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("RemoteProtocolInfo", input.RemoteProtocolInfo);
        getActionInvocation().setInput("PeerConnectionManager", input.PeerConnectionManager);
        getActionInvocation().setInput("PeerConnectionID", new IntegerDatatype(input.PeerConnectionID));
        getActionInvocation().setInput("Direction", input.Direction);
    }

    public PrepareForConnectionOutput executeAction()
    {
        invocation = execute();

        PrepareForConnectionOutput result = new PrepareForConnectionOutput();

        result.ConnectionID = Integer.valueOf(invocation.getOutput("ConnectionID").getValue().toString());
        result.AVTransportID = Integer.valueOf(invocation.getOutput("AVTransportID").getValue().toString());
        result.RcsID = Integer.valueOf(invocation.getOutput("RcsID").getValue().toString());

        return result;
    }
}
