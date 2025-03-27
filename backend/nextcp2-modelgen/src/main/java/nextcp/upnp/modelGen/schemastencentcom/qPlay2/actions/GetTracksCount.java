package nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions;

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
public class GetTracksCount extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetTracksCount.class.getName());
    private ActionInvocation<?> invocation;

    public GetTracksCount(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetTracksCount"), new NextcpClientInfo()), cp);

    }

    public GetTracksCountOutput executeAction()
    {
        invocation = execute();

        GetTracksCountOutput result = new GetTracksCountOutput();

  		if (invocation.getOutput("NrTracks").getValue() != null)
  		{
	        result.NrTracks = invocation.getOutput("NrTracks").getValue().toString();
  		}
  		else
  		{
	        result.NrTracks = "";
  		}

        return result;
    }
}
