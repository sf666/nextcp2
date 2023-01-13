package nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions;

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
public class TracksMax extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(TracksMax.class.getName());
    private ActionInvocation<?> invocation;

    public TracksMax(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("TracksMax"), new NextcpClientInfo()), cp);

    }

    public TracksMaxOutput executeAction()
    {
        invocation = execute();

        TracksMaxOutput result = new TracksMaxOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
