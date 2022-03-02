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
 *
 * Template: action.ftl
 *  
 */
public class SeekSecondAbsolute extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SeekSecondAbsolute.class.getName());
    private ActionInvocation<?> invocation;

    public SeekSecondAbsolute(Service service, SeekSecondAbsoluteInput input, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SeekSecondAbsolute"), new NextcpClientInfo()), cp);

        getActionInvocation().setInput("StreamId", new UnsignedIntegerFourBytes(input.StreamId));
        getActionInvocation().setInput("SecondAbsolute", new UnsignedIntegerFourBytes(input.SecondAbsolute));
    }

    public void executeAction()
    {
        ActionInvocation<?> invocation = execute();
    }
}
