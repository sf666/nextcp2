package nextcp.upnp.modelGen.avopenhomeorg.radio1.actions;

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
public class ChannelsMax extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ChannelsMax.class.getName());
    private ActionInvocation<?> invocation;

    public ChannelsMax(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ChannelsMax"), new NextcpClientInfo()), cp);

    }

    public ChannelsMaxOutput executeAction()
    {
        invocation = execute();

        ChannelsMaxOutput result = new ChannelsMaxOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
