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
public class GetMaxTracks extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetMaxTracks.class.getName());
    private ActionInvocation<?> invocation;

    public GetMaxTracks(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetMaxTracks"), new NextcpClientInfo()), cp);

    }

    public GetMaxTracksOutput executeAction()
    {
        invocation = execute();

        GetMaxTracksOutput result = new GetMaxTracksOutput();

  		if (invocation.getOutput("MaxTracks").getValue() != null)
  		{
	        result.MaxTracks = invocation.getOutput("MaxTracks").getValue().toString();
  		}
  		else
  		{
	        result.MaxTracks = "";
  		}

        return result;
    }
}
