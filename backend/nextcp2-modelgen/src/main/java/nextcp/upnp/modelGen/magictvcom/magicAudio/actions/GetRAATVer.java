package nextcp.upnp.modelGen.magictvcom.magicAudio.actions;

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
public class GetRAATVer extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetRAATVer.class.getName());
    private ActionInvocation<?> invocation;

    public GetRAATVer(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetRAATVer"), new NextcpClientInfo()), cp);

    }

    public GetRAATVerOutput executeAction()
    {
        invocation = execute();

        GetRAATVerOutput result = new GetRAATVerOutput();

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
