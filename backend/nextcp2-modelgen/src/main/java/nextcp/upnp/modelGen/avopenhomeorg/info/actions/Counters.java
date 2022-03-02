package nextcp.upnp.modelGen.avopenhomeorg.info.actions;

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
 *
 * Template: action.ftl
 *  
 */
public class Counters extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Counters.class.getName());
    private ActionInvocation<?> invocation;

    public Counters(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Counters"), new NextcpClientInfo()), cp);

    }

    public CountersOutput executeAction()
    {
        invocation = execute();

        CountersOutput result = new CountersOutput();

        result.TrackCount = ((UnsignedIntegerFourBytes) invocation.getOutput("TrackCount").getValue()).getValue();
        result.DetailsCount = ((UnsignedIntegerFourBytes) invocation.getOutput("DetailsCount").getValue()).getValue();
        result.MetatextCount = ((UnsignedIntegerFourBytes) invocation.getOutput("MetatextCount").getValue()).getValue();

        return result;
    }
}
