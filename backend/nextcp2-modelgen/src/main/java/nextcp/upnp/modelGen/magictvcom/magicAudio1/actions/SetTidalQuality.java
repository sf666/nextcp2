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
public class SetTidalQuality extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SetTidalQuality.class.getName());
    private ActionInvocation<?> invocation;

    public SetTidalQuality(Service service, SetTidalQualityInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SetTidalQuality"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("Quality", input.Quality);
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
