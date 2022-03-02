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
public class GetCurrentConnectionInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentConnectionInfo.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public GetCurrentConnectionInfo(Service service, GetCurrentConnectionInfoInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentConnectionInfo"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("ConnectionID", new IntegerDatatype(input.ConnectionID));
    }

    public GetCurrentConnectionInfoOutput executeAction()
    {
        invocation = execute();

        GetCurrentConnectionInfoOutput result = new GetCurrentConnectionInfoOutput();

        result.RcsID = Integer.valueOf(invocation.getOutput("RcsID").getValue().toString());
        result.AVTransportID = Integer.valueOf(invocation.getOutput("AVTransportID").getValue().toString());
  		if (invocation.getOutput("ProtocolInfo").getValue() != null)
  		{
	        result.ProtocolInfo = invocation.getOutput("ProtocolInfo").getValue().toString();
  		}
  		else
  		{
	        result.ProtocolInfo = "";
  		}
  		if (invocation.getOutput("PeerConnectionManager").getValue() != null)
  		{
	        result.PeerConnectionManager = invocation.getOutput("PeerConnectionManager").getValue().toString();
  		}
  		else
  		{
	        result.PeerConnectionManager = "";
  		}
        result.PeerConnectionID = Integer.valueOf(invocation.getOutput("PeerConnectionID").getValue().toString());
  		if (invocation.getOutput("Direction").getValue() != null)
  		{
	        result.Direction = invocation.getOutput("Direction").getValue().toString();
  		}
  		else
  		{
	        result.Direction = "";
  		}
  		if (invocation.getOutput("Status").getValue() != null)
  		{
	        result.Status = invocation.getOutput("Status").getValue().toString();
  		}
  		else
  		{
	        result.Status = "";
  		}

        return result;
    }
}
