package nextcp.upnp.modelGen.avopenhomeorg.transport.actions;

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
public class TransportState extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(TransportState.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public TransportState(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("TransportState"), new NextcpClientInfo()), cp);

    }

    public TransportStateOutput executeAction()
    {
        invocation = execute();

        TransportStateOutput result = new TransportStateOutput();

  		if (invocation.getOutput("State").getValue() != null)
  		{
	        result.State = invocation.getOutput("State").getValue().toString();
  		}
  		else
  		{
	        result.State = "";
  		}

        return result;
    }
}
