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
public class IdArray extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(IdArray.class.getName());
    private ActionInvocation<?> invocation;

    public IdArray(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("IdArray"), new NextcpClientInfo()), cp);

    }

    public IdArrayOutput executeAction()
    {
        invocation = execute();

        IdArrayOutput result = new IdArrayOutput();

        result.Token = ((UnsignedIntegerFourBytes) invocation.getOutput("Token").getValue()).getValue();
        result.Array = (byte[]) invocation.getOutput("Array").getValue();

        return result;
    }
}
