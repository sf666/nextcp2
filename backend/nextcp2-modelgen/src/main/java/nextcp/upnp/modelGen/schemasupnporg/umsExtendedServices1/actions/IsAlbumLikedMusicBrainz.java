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
public class IsAlbumLikedMusicBrainz extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IsAlbumLikedMusicBrainz.class.getName());
    private ActionInvocation<?> invocation;

    public IsAlbumLikedMusicBrainz(Service service, IsAlbumLikedMusicBrainzInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IsAlbumLikedMusicBrainz"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("MusicBrainzReleaseID", input.MusicBrainzReleaseID);
    }

    public IsAlbumLikedMusicBrainzOutput executeAction()
    {
        invocation = execute();

        IsAlbumLikedMusicBrainzOutput result = new IsAlbumLikedMusicBrainzOutput();

        BooleanDatatype data_AlbumLikedValue = new BooleanDatatype();
        result.AlbumLikedValue = data_AlbumLikedValue.valueOf(invocation.getOutput("AlbumLikedValue").getValue().toString());

        return result;
    }
}
