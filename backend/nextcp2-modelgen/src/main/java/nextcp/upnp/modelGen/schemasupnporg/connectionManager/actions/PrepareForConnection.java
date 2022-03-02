package nextcp.upnp.modelGen.schemasupnporg.connectionManager.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class PrepareForConnection extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(PrepareForConnection.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

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
