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
import nextcp.upnp.UpnpValue;

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
		
        getActionInvocation().setInput("PlaylistId", UpnpValue.forInput(getActionInvocation(), "PlaylistId", input.PlaylistId));
    }

    public GetPlaylistNowPlayingOutput executeAction()
    {
        invocation = execute();

        GetPlaylistNowPlayingOutput result = new GetPlaylistNowPlayingOutput();

        result.NowPlaying = UpnpValue.toTextOrEmpty(invocation.getOutput("NowPlaying").getValue());

        return result;
    }
}
