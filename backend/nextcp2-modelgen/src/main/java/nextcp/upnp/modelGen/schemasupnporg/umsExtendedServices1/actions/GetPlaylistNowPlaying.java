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
public class GetPlaylistNowPlaying extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(GetPlaylistNowPlaying.class.getName());
    private ActionInvocation<?> invocation;

    public GetPlaylistNowPlaying(Service service, GetPlaylistNowPlayingInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("GetPlaylistNowPlaying"), new NextcpClientInfo()), cp);
		
        if (input.PlaylistId != null) {
    	    getActionInvocation().setInput("PlaylistId", new UnsignedIntegerFourBytes(input.PlaylistId));
		} else {
    	    getActionInvocation().setInput("PlaylistId", null);
		}
    }

    public GetPlaylistNowPlayingOutput executeAction()
    {
        invocation = execute();

        GetPlaylistNowPlayingOutput result = new GetPlaylistNowPlayingOutput();

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
