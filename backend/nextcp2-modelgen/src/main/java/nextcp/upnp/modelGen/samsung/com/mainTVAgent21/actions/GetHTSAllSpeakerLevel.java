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
public class GetHTSAllSpeakerLevel extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHTSAllSpeakerLevel.class.getName());
    private ActionInvocation<?> invocation;

    public GetHTSAllSpeakerLevel(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHTSAllSpeakerLevel"), new NextcpClientInfo()), cp);

    }

    public GetHTSAllSpeakerLevelOutput executeAction()
    {
        invocation = execute();

        GetHTSAllSpeakerLevelOutput result = new GetHTSAllSpeakerLevelOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
        result.MaxLevel = ((UnsignedIntegerFourBytes) invocation.getOutput("MaxLevel").getValue()).getValue();
  		if (invocation.getOutput("AllSpeakerLevel").getValue() != null)
  		{
	        result.AllSpeakerLevel = invocation.getOutput("AllSpeakerLevel").getValue().toString();
  		}
  		else
  		{
	        result.AllSpeakerLevel = "";
  		}

        return result;
    }
}
