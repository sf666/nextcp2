package nextcp.upnp.modelGen.schemastencentcom.qPlay2.actions;

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
public class SetLyric extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetLyric.class.getName());
    private ActionInvocation<?> invocation;

    public SetLyric(Service service, SetLyricInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetLyric"), new NextcpClientInfo()), cp);
		
        getActionInvocation().setInput("Lyric", UpnpValue.forInput(getActionInvocation(), "Lyric", input.Lyric));
        getActionInvocation().setInput("LyricType", UpnpValue.forInput(getActionInvocation(), "LyricType", input.LyricType));
        getActionInvocation().setInput("SongID", UpnpValue.forInput(getActionInvocation(), "SongID", input.SongID));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
