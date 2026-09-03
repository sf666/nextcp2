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
public class GetCurrentConnectionInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentConnectionInfo.class.getName());
    private ActionInvocation<?> invocation;

    public GetCurrentConnectionInfo(Service service, GetCurrentConnectionInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentConnectionInfo"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("ConnectionID", UpnpValue.forInput(getActionInvocation(), "ConnectionID", input.ConnectionID));
    }

    public GetCurrentConnectionInfoOutput executeAction()
    {
        invocation = execute();

        GetCurrentConnectionInfoOutput result = new GetCurrentConnectionInfoOutput();

        result.AVTransportID = UpnpValue.toInteger(invocation.getOutput("AVTransportID").getValue());
        result.Direction = UpnpValue.toTextOrEmpty(invocation.getOutput("Direction").getValue());
        result.PeerConnectionID = UpnpValue.toInteger(invocation.getOutput("PeerConnectionID").getValue());
        result.PeerConnectionManager = UpnpValue.toTextOrEmpty(invocation.getOutput("PeerConnectionManager").getValue());
        result.ProtocolInfo = UpnpValue.toTextOrEmpty(invocation.getOutput("ProtocolInfo").getValue());
        result.RcsID = UpnpValue.toInteger(invocation.getOutput("RcsID").getValue());
        result.Status = UpnpValue.toTextOrEmpty(invocation.getOutput("Status").getValue());

        return result;
    }
}
