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
public class AddRadioStationToPlaylist extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(AddRadioStationToPlaylist.class.getName());
    private ActionInvocation<?> invocation;

    public AddRadioStationToPlaylist(Service service, AddRadioStationToPlaylistInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("AddRadioStationToPlaylist"), new NextcpClientInfo()), cp);
		
        if (input.ObjectID != null) {
	        getActionInvocation().setInput("ObjectID", input.ObjectID);
		} else {
    	    getActionInvocation().setInput("ObjectID", null);
		}
        if (input.StationUuid != null) {
	        getActionInvocation().setInput("StationUuid", input.StationUuid);
		} else {
    	    getActionInvocation().setInput("StationUuid", null);
		}
        if (input.Title != null) {
	        getActionInvocation().setInput("Title", input.Title);
		} else {
    	    getActionInvocation().setInput("Title", null);
		}
    }

    public AddRadioStationToPlaylistOutput executeAction()
    {
        invocation = execute();

        AddRadioStationToPlaylistOutput result = new AddRadioStationToPlaylistOutput();

  		if (invocation.getOutput("Result").getValue() != null)
  		{
	        result.Result = invocation.getOutput("Result").getValue().toString();
  		}
  		else
  		{
	        result.Result = "";
  		}

        return result;
    }
}
