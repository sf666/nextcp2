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
public class IsAlbumLiked extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IsAlbumLiked.class.getName());
    private ActionInvocation<?> invocation;

    public IsAlbumLiked(Service service, IsAlbumLikedInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IsAlbumLiked"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("MusicBraizReleaseID", input.MusicBraizReleaseID);
    }

    public IsAlbumLikedOutput executeAction()
    {
        invocation = execute();

        IsAlbumLikedOutput result = new IsAlbumLikedOutput();

        BooleanDatatype data_AlbumLikedValue = new BooleanDatatype();
        result.AlbumLikedValue = data_AlbumLikedValue.valueOf(invocation.getOutput("AlbumLikedValue").getValue().toString());

        return result;
    }
}
