package nextcp.upnp.modelGen.avopenhomeorg.time1.actions;

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
public class Time extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Time.class.getName());
    private ActionInvocation<?> invocation;

    public Time(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Time"), new NextcpClientInfo()), cp);

    }

    public TimeOutput executeAction()
    {
        invocation = execute();

        TimeOutput result = new TimeOutput();

        result.TrackCount = ((UnsignedIntegerFourBytes) invocation.getOutput("TrackCount").getValue()).getValue();
        result.Duration = ((UnsignedIntegerFourBytes) invocation.getOutput("Duration").getValue()).getValue();
        result.Seconds = ((UnsignedIntegerFourBytes) invocation.getOutput("Seconds").getValue()).getValue();

        return result;
    }
}
