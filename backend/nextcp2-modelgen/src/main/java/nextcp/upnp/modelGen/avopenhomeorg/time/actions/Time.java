package nextcp.upnp.modelGen.avopenhomeorg.time.actions;

import org.fourthline.cling.controlpoint.ControlPoint;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import org.fourthline.cling.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class Time extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Time.class.getName());
    private ActionInvocation<?> invocation;
  	private Base64Datatype b64 = new Base64Datatype();

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
