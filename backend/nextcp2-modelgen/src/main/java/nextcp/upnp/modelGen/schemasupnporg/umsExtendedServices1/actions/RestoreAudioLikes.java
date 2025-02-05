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
public class RestoreAudioLikes extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(RestoreAudioLikes.class.getName());
    private ActionInvocation<?> invocation;

    public RestoreAudioLikes(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("RestoreAudioLikes"), new NextcpClientInfo()), cp);

    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
