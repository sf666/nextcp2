package nextcp.upnp.modelGen.avopenhomeorg.info1.actions;

import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.meta.Service;
import org.jupnp.model.types.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.ActionCallback;
import nextcp.upnp.GenActionException;
import nextcp.upnp.NextcpClientInfo;
import nextcp.upnp.UpnpValue;

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

        result.DetailsCount = UpnpValue.toLong(invocation.getOutput("DetailsCount").getValue());
        result.MetatextCount = UpnpValue.toLong(invocation.getOutput("MetatextCount").getValue());
        result.TrackCount = UpnpValue.toLong(invocation.getOutput("TrackCount").getValue());

        return result;
    }
}
