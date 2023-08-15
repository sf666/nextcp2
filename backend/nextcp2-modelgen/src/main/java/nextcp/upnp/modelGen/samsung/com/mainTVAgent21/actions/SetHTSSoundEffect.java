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
public class SetHTSSoundEffect extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetHTSSoundEffect.class.getName());
    private ActionInvocation<?> invocation;

    public SetHTSSoundEffect(Service service, SetHTSSoundEffectInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetHTSSoundEffect"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("SoundEffect", input.SoundEffect);
    }

    public SetHTSSoundEffectOutput executeAction()
    {
        invocation = execute();

        SetHTSSoundEffectOutput result = new SetHTSSoundEffectOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}

        return result;
    }
}
