package nextcp.upnp.modelGen.magictvcom.magicAudio1.actions;

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
public class SetTuneInLogin extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetTuneInLogin.class.getName());
    private ActionInvocation<?> invocation;

    public SetTuneInLogin(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetTuneInLogin"), new NextcpClientInfo()), cp);

    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
