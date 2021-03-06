package nextcp.upnp.modelGen.avopenhomeorg.playlist.actions;

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
