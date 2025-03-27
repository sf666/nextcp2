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

        getActionInvocation().setInput("SongID", input.SongID);
        getActionInvocation().setInput("LyricType", input.LyricType);
        getActionInvocation().setInput("Lyric", input.Lyric);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
