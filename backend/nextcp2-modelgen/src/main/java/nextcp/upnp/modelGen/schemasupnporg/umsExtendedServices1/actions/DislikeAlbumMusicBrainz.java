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
public class DislikeAlbumMusicBrainz extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(DislikeAlbumMusicBrainz.class.getName());
    private ActionInvocation<?> invocation;

    public DislikeAlbumMusicBrainz(Service service, DislikeAlbumMusicBrainzInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("DislikeAlbumMusicBrainz"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("MusicBrainzReleaseID", input.MusicBrainzReleaseID);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
