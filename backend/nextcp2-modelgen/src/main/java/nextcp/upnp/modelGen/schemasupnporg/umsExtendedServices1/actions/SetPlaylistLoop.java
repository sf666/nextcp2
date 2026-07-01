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
public class SetPlaylistLoop extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetPlaylistLoop.class.getName());
    private ActionInvocation<?> invocation;

    public SetPlaylistLoop(Service service, SetPlaylistLoopInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetPlaylistLoop"), new NextcpClientInfo()), cp);
		
        if (input.PlaylistLoop != null) {
        	getActionInvocation().setInput("PlaylistLoop", input.PlaylistLoop);
		} else {
    	    getActionInvocation().setInput("PlaylistLoop", null);
		}
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
