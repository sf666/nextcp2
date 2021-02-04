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
public class GetCurrentConnectionIDs extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetCurrentConnectionIDs.class.getName());
    private ActionInvocation<?> invocation;

    public GetCurrentConnectionIDs(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetCurrentConnectionIDs"), new NextcpClientInfo()), cp);

    }

    public GetCurrentConnectionIDsOutput executeAction()
    {
        invocation = execute();

        GetCurrentConnectionIDsOutput result = new GetCurrentConnectionIDsOutput();

  		if (invocation.getOutput("ConnectionIDs").getValue() != null)
  		{
	        result.ConnectionIDs = invocation.getOutput("ConnectionIDs").getValue().toString();
  		}
  		else
  		{
	        result.ConnectionIDs = "";
  		}

        return result;
    }
}
