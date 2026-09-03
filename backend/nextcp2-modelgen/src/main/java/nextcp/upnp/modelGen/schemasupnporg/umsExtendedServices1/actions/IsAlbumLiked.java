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
public class IsAlbumLiked extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IsAlbumLiked.class.getName());
    private ActionInvocation<?> invocation;

    public IsAlbumLiked(Service service, IsAlbumLikedInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IsAlbumLiked"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("DiscogsId", UpnpValue.forInput(getActionInvocation(), "DiscogsId", input.DiscogsId));
        getActionInvocation().setInput("MusicBrainzId", UpnpValue.forInput(getActionInvocation(), "MusicBrainzId", input.MusicBrainzId));
    }

    public IsAlbumLikedOutput executeAction()
    {
        invocation = execute();

        IsAlbumLikedOutput result = new IsAlbumLikedOutput();

        result.AlbumLikedValue = UpnpValue.toBoolean(invocation.getOutput("AlbumLikedValue").getValue());

        return result;
    }
}
