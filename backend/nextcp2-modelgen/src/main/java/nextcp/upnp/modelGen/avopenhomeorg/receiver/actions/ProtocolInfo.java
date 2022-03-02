package nextcp.upnp.modelGen.avopenhomeorg.receiver.actions;

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
public class ProtocolInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ProtocolInfo.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

    public ProtocolInfo(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ProtocolInfo"), new NextcpClientInfo()), cp);

    }

    public ProtocolInfoOutput executeAction()
    {
        invocation = execute();

        ProtocolInfoOutput result = new ProtocolInfoOutput();

  		if (invocation.getOutput("Value").getValue() != null)
  		{
	        result.Value = invocation.getOutput("Value").getValue().toString();
  		}
  		else
  		{
	        result.Value = "";
  		}

        return result;
    }
}
