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
public class GetHTSAllSpeakerDistance extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetHTSAllSpeakerDistance.class.getName());
    private ActionInvocation<?> invocation;

    public GetHTSAllSpeakerDistance(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetHTSAllSpeakerDistance"), new NextcpClientInfo()), cp);

    }

    public GetHTSAllSpeakerDistanceOutput executeAction()
    {
        invocation = execute();

        GetHTSAllSpeakerDistanceOutput result = new GetHTSAllSpeakerDistanceOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}
        result.MaxDistance = ((UnsignedIntegerFourBytes) invocation.getOutput("MaxDistance").getValue()).getValue();
  		if (invocation.getOutput("AllSpeakerDistance").getValue() != null)
  		{
	        result.AllSpeakerDistance = invocation.getOutput("AllSpeakerDistance").getValue().toString();
  		}
  		else
  		{
	        result.AllSpeakerDistance = "";
  		}

        return result;
    }
}
