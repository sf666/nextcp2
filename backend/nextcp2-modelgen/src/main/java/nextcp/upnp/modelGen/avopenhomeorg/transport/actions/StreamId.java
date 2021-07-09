package nextcp.upnp.modelGen.avopenhomeorg.transport.actions;

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
