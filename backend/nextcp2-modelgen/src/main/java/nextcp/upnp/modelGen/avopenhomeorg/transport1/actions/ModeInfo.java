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
import nextcp.upnp.UpnpValue;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: action.ftl
 *  
 */
public class ModeInfo extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(ModeInfo.class.getName());
    private ActionInvocation<?> invocation;

    public ModeInfo(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("ModeInfo"), new NextcpClientInfo()), cp);
		
    }

    public ModeInfoOutput executeAction()
    {
        invocation = execute();

        ModeInfoOutput result = new ModeInfoOutput();

        result.CanRepeat = UpnpValue.toBoolean(invocation.getOutput("CanRepeat").getValue());
        result.CanShuffle = UpnpValue.toBoolean(invocation.getOutput("CanShuffle").getValue());
        result.CanSkipNext = UpnpValue.toBoolean(invocation.getOutput("CanSkipNext").getValue());
        result.CanSkipPrevious = UpnpValue.toBoolean(invocation.getOutput("CanSkipPrevious").getValue());

        return result;
    }
}
