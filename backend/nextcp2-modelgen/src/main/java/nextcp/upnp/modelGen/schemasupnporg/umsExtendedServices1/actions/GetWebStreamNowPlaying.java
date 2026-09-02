package nextcp.upnp.modelGen.schemasupnporg.umsExtendedServices1.actions;

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
public class GetWebStreamNowPlaying extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetWebStreamNowPlaying.class.getName());
    private ActionInvocation<?> invocation;

    public GetWebStreamNowPlaying(Service service, GetWebStreamNowPlayingInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetWebStreamNowPlaying"), new NextcpClientInfo()), cp);
		
        if (input.ObjectID != null) {
	        getActionInvocation().setInput("ObjectID", input.ObjectID);
		} else {
    	    getActionInvocation().setInput("ObjectID", null);
		}
    }

    public GetWebStreamNowPlayingOutput executeAction()
    {
        invocation = execute();

        GetWebStreamNowPlayingOutput result = new GetWebStreamNowPlayingOutput();

  		if (invocation.getOutput("NowPlaying").getValue() != null)
  		{
	        result.NowPlaying = invocation.getOutput("NowPlaying").getValue().toString();
  		}
  		else
  		{
	        result.NowPlaying = "";
  		}

        return result;
    }
}
