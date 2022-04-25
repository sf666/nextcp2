package nextcp.upnp.modelGen.avopenhomeorg.radio1.actions;

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
