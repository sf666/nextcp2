package nextcp.upnp.modelGen.avopenhomeorg.product.actions;

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
public class SourceIndex extends ActionCallback
{
    private static Logger log = LoggerFactory.getLogger(SourceIndex.class.getName());
    private ActionInvocation<?> invocation;

    public SourceIndex(Service service, ControlPoint cp)
    {
        super(new ActionInvocation(service.getAction("SourceIndex"), new NextcpClientInfo()), cp);

    }

    public SourceIndexOutput executeAction()
    {
        invocation = execute();

        SourceIndexOutput result = new SourceIndexOutput();

        result.Value = ((UnsignedIntegerFourBytes) invocation.getOutput("Value").getValue()).getValue();

        return result;
    }
}
