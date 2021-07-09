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
public class StreamInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(StreamInfo.class.getName());
    private ActionInvocation<?> invocation;

    public StreamInfo(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("StreamInfo"), new NextcpClientInfo()), cp);

    }

    public StreamInfoOutput executeAction()
    {
        invocation = execute();

        StreamInfoOutput result = new StreamInfoOutput();

        result.StreamId = ((UnsignedIntegerFourBytes) invocation.getOutput("StreamId").getValue()).getValue();
        BooleanDatatype data_CanSeek = new BooleanDatatype();
        result.CanSeek = data_CanSeek.valueOf(invocation.getOutput("CanSeek").getValue().toString());
        BooleanDatatype data_CanPause = new BooleanDatatype();
        result.CanPause = data_CanPause.valueOf(invocation.getOutput("CanPause").getValue().toString());

        return result;
    }
}
