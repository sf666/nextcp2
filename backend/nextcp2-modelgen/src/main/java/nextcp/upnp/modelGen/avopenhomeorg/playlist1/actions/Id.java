package nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions;

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
public class Id extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(Id.class.getName());
    private ActionInvocation<?> invocation;

    public Id(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("Id"), new NextcpClientInfo()), cp);

    }

    public IdOutput executeAction()
    {
        invocation = execute();

        IdOutput result = new IdOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
