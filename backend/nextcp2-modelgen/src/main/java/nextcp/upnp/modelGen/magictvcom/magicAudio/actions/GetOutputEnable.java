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
public class GetOutputEnable extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetOutputEnable.class.getName());
    private ActionInvocation<?> invocation;

    public GetOutputEnable(Service service, GetOutputEnableInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetOutputEnable"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Port", new UnsignedIntegerFourBytes(input.Port));
    }

    public GetOutputEnableOutput executeAction()
    {
        invocation = execute();

        GetOutputEnableOutput result = new GetOutputEnableOutput();

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
