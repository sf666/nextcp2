package nextcp.upnp.modelGen.avopenhomeorg.credentials.actions;

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
public class GetPublicKey extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPublicKey.class.getName());
    private ActionInvocation<?> invocation;

    public GetPublicKey(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPublicKey"), new NextcpClientInfo()), cp);

    }

    public GetPublicKeyOutput executeAction()
    {
        invocation = execute();

        GetPublicKeyOutput result = new GetPublicKeyOutput();

  		if (invocation.getOutput("PublicKey").getValue() != null)
  		{
	        result.PublicKey = invocation.getOutput("PublicKey").getValue().toString();
  		}
  		else
  		{
	        result.PublicKey = "";
  		}

        return result;
    }
}
