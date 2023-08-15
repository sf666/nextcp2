package nextcp.upnp.modelGen.schemasupnporg.renderingControl2.actions;

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
public class SetMute extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetMute.class.getName());
    private ActionInvocation<?> invocation;

    public SetMute(Service service, SetMuteInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetMute"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("InstanceID", new UnsignedIntegerFourBytes(input.InstanceID));
        getActionInvocation().setInput("Channel", input.Channel);
        getActionInvocation().setInput("DesiredMute", input.DesiredMute);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
