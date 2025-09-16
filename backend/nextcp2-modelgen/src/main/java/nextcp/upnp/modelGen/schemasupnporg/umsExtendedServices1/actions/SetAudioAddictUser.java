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
public class SetAudioAddictUser extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetAudioAddictUser.class.getName());
    private ActionInvocation<?> invocation;

    public SetAudioAddictUser(Service service, SetAudioAddictUserInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetAudioAddictUser"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("AudioAddictUser", input.AudioAddictUser);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
