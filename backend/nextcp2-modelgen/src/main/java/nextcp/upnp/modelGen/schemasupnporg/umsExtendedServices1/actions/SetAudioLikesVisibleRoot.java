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
public class SetAudioLikesVisibleRoot extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetAudioLikesVisibleRoot.class.getName());
    private ActionInvocation<?> invocation;

    public SetAudioLikesVisibleRoot(Service service, SetAudioLikesVisibleRootInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetAudioLikesVisibleRoot"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("AudioLikesVisibleRoot", input.AudioLikesVisibleRoot);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
