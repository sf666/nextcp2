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
public class GetAnalogOutLvl extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetAnalogOutLvl.class.getName());
    private ActionInvocation<?> invocation;

    public GetAnalogOutLvl(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetAnalogOutLvl"), new NextcpClientInfo()), cp);

    }

    public GetAnalogOutLvlOutput executeAction()
    {
        invocation = execute();

        GetAnalogOutLvlOutput result = new GetAnalogOutLvlOutput();

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
