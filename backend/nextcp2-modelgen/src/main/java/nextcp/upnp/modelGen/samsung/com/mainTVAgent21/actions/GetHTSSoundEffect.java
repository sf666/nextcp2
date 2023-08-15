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
public class GetHTSSoundEffect extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHTSSoundEffect.class.getName());
    private ActionInvocation<?> invocation;

    public GetHTSSoundEffect(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHTSSoundEffect"), new NextcpClientInfo()), cp);

    }

    public GetHTSSoundEffectOutput executeAction()
    {
        invocation = execute();

        GetHTSSoundEffectOutput result = new GetHTSSoundEffectOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
  		if (invocation.getOutput("SoundEffect").getValue() != null)
  		{
	        result.SoundEffect = invocation.getOutput("SoundEffect").getValue().toString();
  		}
  		else
  		{
	        result.SoundEffect = "";
  		}
  		if (invocation.getOutput("SoundEffectList").getValue() != null)
  		{
	        result.SoundEffectList = invocation.getOutput("SoundEffectList").getValue().toString();
  		}
  		else
  		{
	        result.SoundEffectList = "";
  		}

        return result;
    }
}
