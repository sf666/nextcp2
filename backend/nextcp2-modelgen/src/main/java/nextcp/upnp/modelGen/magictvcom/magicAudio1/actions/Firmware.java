package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class Firmware extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Firmware.class.getName());
    private ActionInvocation<?> invocation;

    public Firmware(Service service, FirmwareInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Firmware"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Command", input.Command);
    }

    public FirmwareOutput executeAction()
    {
        invocation = execute();

        FirmwareOutput result = new FirmwareOutput();

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
