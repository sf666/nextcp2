package nextcp.upnp.modelGen.samsung.com.mainTVAgent21.actions;

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
public class GetHTSSpeakerConfig extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHTSSpeakerConfig.class.getName());
    private ActionInvocation<?> invocation;

    public GetHTSSpeakerConfig(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHTSSpeakerConfig"), new NextcpClientInfo()), cp);

    }

    public GetHTSSpeakerConfigOutput executeAction()
    {
        invocation = execute();

        GetHTSSpeakerConfigOutput result = new GetHTSSpeakerConfigOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
        result.SpeakerChannel = ((UnsignedIntegerFourBytes) invocation.getOutput("SpeakerChannel").getValue()).getValue();
        result.SpeakerLFE = ((UnsignedIntegerFourBytes) invocation.getOutput("SpeakerLFE").getValue()).getValue();

        return result;
    }
}
