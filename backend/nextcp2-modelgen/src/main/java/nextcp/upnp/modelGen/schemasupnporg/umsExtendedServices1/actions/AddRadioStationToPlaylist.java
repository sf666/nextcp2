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
public class AddRadioStationToPlaylist extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(AddRadioStationToPlaylist.class.getName());
    private ActionInvocation<?> invocation;

    public AddRadioStationToPlaylist(Service service, AddRadioStationToPlaylistInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("AddRadioStationToPlaylist"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("ObjectID", UpnpValue.forInput(getActionInvocation(), "ObjectID", input.ObjectID));
        getActionInvocation().setInput("StationUuid", UpnpValue.forInput(getActionInvocation(), "StationUuid", input.StationUuid));
        getActionInvocation().setInput("Title", UpnpValue.forInput(getActionInvocation(), "Title", input.Title));
    }

    public AddRadioStationToPlaylistOutput executeAction()
    {
        invocation = execute();

        AddRadioStationToPlaylistOutput result = new AddRadioStationToPlaylistOutput();

        result.Result = UpnpValue.toTextOrEmpty(invocation.getOutput("Result").getValue());

        return result;
    }
}
