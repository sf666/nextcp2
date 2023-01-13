package nextcp.upnp.modelGen.avopenhomeorg.transport1.actions;

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
public class StreamId extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StreamId.class.getName());
    private ActionInvocation<?> invocation;

    public StreamId(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StreamId"), new NextcpClientInfo()), cp);

    }

    public StreamIdOutput executeAction()
    {
        invocation = execute();

        StreamIdOutput result = new StreamIdOutput();

        result.StreamId = ((UnsignedIntegerFourBytes) invocation.getOutput("StreamId").getValue()).getValue();

        return result;
    }
}
